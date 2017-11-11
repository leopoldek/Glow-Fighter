#version 130

uniform bool u_showEdges = false;
uniform bool u_fxaaOn = true;

const float lumaThreshold = 0.5f;
const float mulReduce = 8.0f;
const float minReduce = 128.0f;
const float maxSpan = 8.0f;

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

// see FXAA
// http://developer.download.nvidia.com/assets/gamedev/files/sdk/11/FXAA_WhitePaper.pdf
// http://iryoku.com/aacourse/downloads/09-FXAA-3.11-in-15-Slides.pdf
// http://horde3d.org/wiki/index.php5?title=Shading_Technique_-_FXAA

void main(void){
	vec2 tex_offset = 1.0 / textureSize(u_texture, 0); // gets size of single texel
	vec4 color = texture(u_texture, v_texCoords);
    vec3 rgbM = color.rgb;
    
	// Possibility to toggle FXAA on and off.
	if (!u_fxaaOn){
		gl_FragColor = vec4(rgbM, color.a);
		return;
	}

	// Sampling neighbour texels. Offsets are adapted to OpenGL texture coordinates. 
	vec3 rgbNW = texture(u_texture, v_texCoords + vec2(-tex_offset.x, tex_offset.y)).rgb;
    vec3 rgbNE = texture(u_texture, v_texCoords + vec2(tex_offset.x, tex_offset.y)).rgb;
    vec3 rgbSW = texture(u_texture, v_texCoords + vec2(-tex_offset.x, -tex_offset.y)).rgb;
    vec3 rgbSE = texture(u_texture, v_texCoords + vec2(tex_offset.x, -tex_offset.y)).rgb;

	// see http://en.wikipedia.org/wiki/Grayscale
	const vec3 toLuma = vec3(0.299, 0.587, 0.114);
	//const vec3 toLuma = vec3(0.333, 0.333, 0.333);
	
	// Convert from RGB to luma.
	float lumaNW = dot(rgbNW, toLuma);
	float lumaNE = dot(rgbNE, toLuma);
	float lumaSW = dot(rgbSW, toLuma);
	float lumaSE = dot(rgbSE, toLuma);
	float lumaM = dot(rgbM, toLuma);

	// Gather minimum and maximum luma.
	float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
	float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));
	
	if (lumaMax - lumaMin < lumaMax * lumaThreshold){//If true, do no AA on pixel.
		gl_FragColor = vec4(rgbM, color.a);
		return;
	}  
	
	// Sampling is done along the gradient.
	vec2 samplingDirection = vec2(-((lumaNW + lumaNE) - (lumaSW + lumaSE)), ((lumaNW + lumaSW) - (lumaNE + lumaSE)));
    
    // Sampling step distance depends on the luma: The brighter the sampled texels, the smaller the final sampling step direction.
    // This results, that brighter areas are less blurred/more sharper than dark areas.
    float samplingDirectionReduce = max((lumaNW + lumaNE + lumaSW + lumaSE) * 0.25 * mulReduce, minReduce);

	// Factor for norming the sampling direction plus adding the brightness influence.
	float minSamplingDirectionFactor = 1.0 / (min(abs(samplingDirection.x), abs(samplingDirection.y)) + samplingDirectionReduce);
    
    // Calculate final sampling direction vector by reducing, clamping to a range and finally adapting to the texture size.
    samplingDirection = clamp(samplingDirection * minSamplingDirectionFactor, vec2(-maxSpan, -maxSpan), vec2(maxSpan, maxSpan)) * tex_offset;
	
	// Inner samples on the tab.
	vec3 rgbSampleNeg = texture(u_texture, v_texCoords + samplingDirection * (1.0/3.0 - 0.5)).rgb;
	vec3 rgbSamplePos = texture(u_texture, v_texCoords + samplingDirection * (2.0/3.0 - 0.5)).rgb;

	vec3 rgbTwoTab = (rgbSamplePos + rgbSampleNeg) * 0.5;

	// Outer samples on the tab.
	vec3 rgbSampleNegOuter = texture(u_texture, v_texCoords + samplingDirection * (0.0/3.0 - 0.5)).rgb;
	vec3 rgbSamplePosOuter = texture(u_texture, v_texCoords + samplingDirection * (3.0/3.0 - 0.5)).rgb;
	
	vec3 rgbFourTab = (rgbSamplePosOuter + rgbSampleNegOuter) * 0.25 + rgbTwoTab * 0.5;
	
	// Calculate luma for checking against the minimum and maximum value.
	float lumaFourTab = dot(rgbFourTab, toLuma);
	
	// Are outer samples of the tab beyond the edge ...
	if (lumaFourTab < lumaMin || lumaFourTab > lumaMax){
		// ... yes, so use only two samples.
		gl_FragColor = vec4(rgbTwoTab, color.a);
	}else{
		// ... no, so use four samples. 
		gl_FragColor = vec4(rgbFourTab, color.a);
	}

	// Show edges for debug purposes.	
	if (u_showEdges){
		gl_FragColor.r = 1.0;
	}
}