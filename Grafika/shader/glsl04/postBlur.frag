#version 330
in vec2 texCoord;
out vec4 outColor;
uniform sampler2D texture;
void main() {
	float delta = 10.0/512.0;
	vec3 color = 4.0*texture2D(texture, texCoord).rgb; 
	color += 2*texture2D(texture, fract(texCoord+vec2(delta, 0))).rgb; 
	color += 2*texture2D(texture, fract(texCoord+vec2(-delta, 0))).rgb; 
	color += 2*texture2D(texture, fract(texCoord+vec2(0, -delta))).rgb; 
	color += 2*texture2D(texture, fract(texCoord+vec2(0, delta))).rgb; 
	color += texture2D(texture, fract(texCoord+vec2(delta, delta))).rgb; 
	color += texture2D(texture, fract(texCoord+vec2(delta, -delta))).rgb; 
	color += texture2D(texture, fract(texCoord+vec2(-delta, -delta))).rgb; 
	color += texture2D(texture, fract(texCoord+vec2(-delta, delta))).rgb; 
	
	outColor = vec4(color/16.0,1.0); 
} 
