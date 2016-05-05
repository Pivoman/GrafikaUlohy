#version 330
in vec3 inPosition; // vstup z vertex bufferu
in vec3 inNormal; // vstup z vertex bufferu
out vec3 vertColor; // vystup do dalsich casti retezce
uniform mat4 mat;
void main() {
	gl_Position = mat * vec4(inPosition, 1.0);
	vertColor = inNormal * 0.5 + 0.5;
} 
