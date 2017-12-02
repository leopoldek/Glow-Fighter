//Unfortunately, we have to use 1.2 because WebGL can't handle >= 1.3
#version 100
//#extension GL_EXT_gpu_shader4 : enable
//#extension GL_EXT_framebuffer_multisample : enable

#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform bool horizontal;
uniform sampler2D u_texture;
uniform vec2 u_texelSize;//Size of single texel

void main(){
    vec3 result = texture2D(u_texture, v_texCoords).rgb; // current fragment's contribution
    if(horizontal){
        for(int i = 1; i < 10; ++i){
        	float iFloat = float(i);
            result += texture2D(u_texture, v_texCoords + vec2(u_texelSize.x * iFloat, 0.0)).rgb;
            result += texture2D(u_texture, v_texCoords - vec2(u_texelSize.x * iFloat, 0.0)).rgb;
        }
    }else{
        for(int i = 1; i < 10; ++i){
        	float iFloat = float(i);
            result += texture2D(u_texture, v_texCoords + vec2(0.0, u_texelSize.y * iFloat)).rgb;
            result += texture2D(u_texture, v_texCoords - vec2(0.0, u_texelSize.y * iFloat)).rgb;
        }
    }
    gl_FragColor = v_color * vec4(result/19.0, 1.0);
}