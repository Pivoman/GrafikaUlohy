#version 330
in vec2 inPosition;
out vec3 vertColor;
out vec2 texCoord;
uniform mat4 mat;
uniform vec3 lightPos; 

float PI = 3.1415927;

vec3 paramSurf(vec2 uv){
	float a = 2 * PI * uv.x; //azimut
	float t = 1 * uv.y; //zenit
	return vec3(t * cos(a), t * sin(a), t);
}

void main() {
	vec3 position = paramSurf(inPosition);
	vec3 tux = paramSurf(inPosition + vec2(1.0, 0.0)) - position;
	vec3 tvy = paramSurf(inPosition + vec2(0.0, 1.0)) - position;
	vec3 n = normalize(cross(tvy, tux)); //vektorovy soucit
	vec3 lightVec = normalize(lightPos-position);
	float diffuse = max(dot(n, lightVec), 0.0); //skalarni soucin
	gl_Position = mat * vec4(position, 1.0);
	//vertColor = vec3(inPosition, 0.0);
	//vertColor = n;
	//vertColor = vec3(diffuse);
	//texCoord = inPosition;
	
	//odvozeni souradnice do textury z pozice a normaly: 
	//int aux = int(dot(abs(inNormal) * vec3(0, 1, 2), vec3(1, 1, 1)));
	//texCoord = vec2(inPosition[(aux + 1) % 3], inPosition[(aux + 2) % 3]);
} 
