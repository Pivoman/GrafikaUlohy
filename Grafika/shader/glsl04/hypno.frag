#version 330
in vec2 texCoord;
out vec4 outColor;
uniform sampler2D texture;
uniform sampler2D source;
uniform float time;
void main() {

	mat2 mat = mat2(cos(time), -sin(time), sin(time), cos(time));

	vec2 texCoordRen = (1.2 + 0.05 * cos(5 * time)) * ((texCoord - 0.5) * mat) + 0.5;
	if (texCoordRen.x < 0 || texCoordRen.x > 1 ||
		texCoordRen.y < 0 || texCoordRen.y > 1) {
		outColor = texture2D(source, texCoord);
	} else {
		texCoordRen += vec2(cos(10 * time), sin(10 * time)) * 0.07;
		outColor = texture2D(texture, texCoordRen);
	} 
	//outColor = vec4(texCoord, 1.0,1.0); 
	//outColor.r = 1.0; 
} 
