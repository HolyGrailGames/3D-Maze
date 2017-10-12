
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
uniform vec4 u_globalAmbient;

// Light specific variables
// We omit ambient for each light as it simplifies the light model.
uniform vec4 u_lightPosition;
uniform vec4 u_lightDiffuse;
uniform vec4 u_lightSpecular;

// Material variables
uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform vec4 u_materialEmission;
uniform float u_materialShininess;

varying vec4 v_color;

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	normal = u_modelMatrix * normal;
	
	
	
	// Preparation for lighting 
	vec4 v = u_eyePosition - position; // Direction to the camera	
	
	// Duplicate the following stuff for adding more lights to the game!
		
	vec4 s = u_lightPosition - position; // Direction to the light
	vec4 h = s + v;
	
	float lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	float phong = max(0.0,dot(normal, h) / (length(normal) * length(h)));
	
	vec4 diffuseColor = lambert * u_lightDiffuse * u_materialDiffuse; 
	vec4 specularColor = pow(phong, u_materialShininess) * u_lightSpecular * u_materialSpecular; 
	
	v_color = u_globalAmbient * diffuseColor + u_materialEmission + specularColor;
	// End of stuff to duplicate for additional lights!
	
	
	// Pos
	position = u_viewMatrix * position;
	gl_Position = u_projectionMatrix * position;
}