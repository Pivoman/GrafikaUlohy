#version 120
varying vec3 vertColor; // vstup z predchozi casti retezce
void main() {
	gl_FragColor = vec4(vertColor, 1.0); 
} 
