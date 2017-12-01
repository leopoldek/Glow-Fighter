#version 130

uniform bool horizontal;
//uniform float weight[6] = float[] (0.198596, 0.175713, 0.121703, 0.065984, 0.028002, 0.0093);

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main(){             
    /*
    vec2 tex_offset = 1.0 / textureSize(u_texture, 0); // gets size of single texel
    vec3 result = texture(u_texture, v_texCoords).rgb * weight[0]; // current fragment's contribution
    if(horizontal){
        for(int i = 1; i < 6; ++i){
            result += texture(u_texture, v_texCoords + vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
            result += texture(u_texture, v_texCoords - vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
        }
    }else{
        for(int i = 1; i < 6; ++i){
            result += texture(u_texture, v_texCoords + vec2(0.0, tex_offset.y * i)).rgb * weight[i];
            result += texture(u_texture, v_texCoords - vec2(0.0, tex_offset.y * i)).rgb * weight[i];
        }
    }
    gl_FragColor = v_color * vec4(result, 1.0);
    */
	
	
	
	
	
	vec2 tex_offset = 1.0 / textureSize(u_texture, 0); // gets size of single texel
    vec3 result = texture(u_texture, v_texCoords).rgb; // current fragment's contribution
    if(horizontal){
        for(int i = 1; i < 10; ++i){
            result += texture(u_texture, v_texCoords + vec2(tex_offset.x * i, 0.0)).rgb;
            result += texture(u_texture, v_texCoords - vec2(tex_offset.x * i, 0.0)).rgb;
        }
    }else{
        for(int i = 1; i < 10; ++i){
            result += texture(u_texture, v_texCoords + vec2(0.0, tex_offset.y * i)).rgb;
            result += texture(u_texture, v_texCoords - vec2(0.0, tex_offset.y * i)).rgb;
        }
    }
    gl_FragColor = v_color * vec4(result/19.0, 1.0);
}