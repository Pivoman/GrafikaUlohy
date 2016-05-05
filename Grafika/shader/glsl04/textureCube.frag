#version 330
in vec3 vertColor;
in vec3 vertPosition;
in vec2 texCoord;
out vec4 outColor;
uniform samplerCube textureCube;
void main() {
	outColor = vec4(texture(textureCube, vertPosition).rgb,1.0);
	//outColor = texture2D(texture, texCoord);
	//outColor = vec4(vertPosition,1.0); 
	//outColor = vec4(vertColor,1.0); 
}

	 
