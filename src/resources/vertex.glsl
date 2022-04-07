#version 430 core

layout(location = 0) in vec3 vPosition;
layout(location = 1) in vec3 vColor;
layout(location = 2) in vec2 vTextureCoord;

out vec3 passColor;
out vec2 passTextureCoord;

uniform mat4 model;
uniform mat4 perspective;

void main() {

	gl_Position = vec4(vPosition, 1.0) * model * perspective;
	passColor = vColor;
	passTextureCoord = vTextureCoord;
	
}