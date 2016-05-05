#version 330
in vec2 texCoord;
out vec4 outColor;
uniform sampler2D texture;
void main() {
	float delta = 1.0/512.0;
	float value1 = texture2D(texture, texCoord).r; 
	float value2 = texture2D(texture, texCoord+vec2( delta, 0.0)).r; 
	//outColor = vec4(texCoord, 1.0,1.0); 
	
	float value = max(value1,value2);
	outColor = vec4(value, 0.0, 0.0, 1.0);
} 
