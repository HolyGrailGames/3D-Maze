
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;

// Global lighting variables
uniform vec4 u_lightPosition;

varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	v_normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	v_normal = u_modelMatrix * v_normal;
	
	
	// Preparation for lighting 
	
	v_s = u_lightPosition - position; // Direction to the light
	vec4 v = u_eyePosition - position; // Direction to the camera	
	
	v_h = v_s + v;

	// Pos
	position = u_viewMatrix * position;
	gl_Position = u_projectionMatrix * position;
}