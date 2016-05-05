#version 330
in vec2 inPosition; // vstup z vertex bufferu
in vec3 inColor; // vstup z vertex bufferu
out vec3 vertColor; // vystup do dalsich casti retezce
uniform float time;
void main() {
	vec2 position = inPosition;
	position.x += 0.1;
	position.y += cos(position.x + time);
	gl_Position = vec4(position, 0.0, 1.0); 
	vertColor = inColor;
} 
