#version 330
in vec3 vertColor;
in vec2 texCoord;
out vec4 outColor;
uniform sampler2D texture;
void main() {
//	outColor = texture2D(texture, texCoord);
	//outColor = vec4(vertColor, 1.0) * texture2D(texture, texCoord); 
} 
