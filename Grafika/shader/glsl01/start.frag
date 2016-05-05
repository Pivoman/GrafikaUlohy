#version 330
in vec3 vertColor; // vstup z predchozi casti retezce
out vec4 outColor; // vystup z fragment shaderu
void main() {
	outColor = vec4(vertColor, 1.0); 
} 
