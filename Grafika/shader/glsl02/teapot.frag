#version 330
in vec3 vertColor; // vstup z predchozi casti retezce
out vec4 outColor;
void main() {
	//outColor = vec4(vec3(position.x,position.y,position.z),0.0);
 	outColor = vec4(vertColor, 1.0);
	//outColor = vec4(1.0); 
} 


