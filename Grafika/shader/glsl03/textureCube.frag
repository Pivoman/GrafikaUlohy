#version 330
in vec3 vertColor;
in vec3 vertPosition;
out vec4 outColor;
uniform samplerCube textureBox;
void main() {
	outColor = vec4(texture(textureBox, vertPosition).rgb,1.0);
}

	 
