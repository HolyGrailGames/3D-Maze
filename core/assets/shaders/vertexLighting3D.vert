
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

// The position of the camera
uniform vec4 u_eyePosition;

// Global lighting variables
uniform vec4 u_globalAmbient;

// Light specific variables
// We omit ambient for each light as it simplifies the light model, and the ambient of multiple lights might wash out our global effects.

// The positions of the 4 lights
uniform vec4 u_light1Position;
uniform vec4 u_light2Position;
uniform vec4 u_light3Position;
uniform vec4 u_light4Position;

// The diffuse part of the lights. Diffuse reflection 
// is the reflection of light or other waves or particles from 
// a surface such that a ray incident on the surface is scattered at many 
// angles rather than at just one angle as in the case of specular reflection. 
// ... Many common materials exhibit a mixture of specular and diffuse reflection.
uniform vec4 u_light1Diffuse;
uniform vec4 u_light2Diffuse;
uniform vec4 u_light3Diffuse;
uniform vec4 u_light4Diffuse;

// The specular part of the lights. 
// Specular reflection, also known as regular reflection, 
// is the mirror-like reflection of waves, such as light, 
// from a surface. In this process, each incident ray is 
// reflected, with the reflected ray having the same angle to 
// the surface normal as the incident ray.
uniform vec4 u_light1Specular;
uniform vec4 u_light2Specular;
uniform vec4 u_light3Specular;
uniform vec4 u_light4Specular;

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
	// We only need to calculate v ones, as it does only 
	// depend on the position of the camera and the vertex sent in to the shader
	vec4 v = u_eyePosition - position; // Direction to the camera	
	
	// The following code is duplicated for each of our four lights
	// LIGHT 1!!	
	vec4 s = u_light1Position - position; // Direction to the light
	vec4 h = s + v; // The halfway vector between s and v
	
	float lambert = max(0.0, dot(normal, s) / (length(normal) * length(s))); // Calculate lambert
	float phong = max(0.0, dot(normal, h) / (length(normal) * length(h))); // Calculate phong
	
	vec4 diffuseColor = lambert * u_light1Diffuse * u_materialDiffuse;  // 
	vec4 specularColor = pow(phong, u_materialShininess) * u_light1Specular * u_materialSpecular; 
	
	// The actual color value of light1, depends upon the position of the player, the position of the light
	// the diffuse of the light, the specular of the light, and the specular, diffuse and shininess of the material.
	vec4 light1CalcColor = (diffuseColor + specularColor); 
	
	// LIGHT 2!!
	s = u_light2Position - position; // Direction to the light
	h = s + v;
	
	lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	phong = max(0.0, dot(normal, h) / (length(normal) * length(h)));
	
	diffuseColor = lambert * u_light2Diffuse * u_materialDiffuse; 
	specularColor = pow(phong, u_materialShininess) * u_light2Specular * u_materialSpecular; 
	
	vec4 light2CalcColor = (diffuseColor + specularColor);
	
	// LIGHT 3!!
	s = u_light3Position - position; // Direction to the light
	h = s + v;
	
	lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	phong = max(0.0, dot(normal, h) / (length(normal) * length(h)));
	
	diffuseColor = lambert * u_light3Diffuse * u_materialDiffuse; 
	specularColor = pow(phong, u_materialShininess) * u_light3Specular * u_materialSpecular; 
	
	vec4 light3CalcColor = (diffuseColor + specularColor);
	
	// LIGHT 4!!
	s = u_light4Position - position; // Direction to the light
	h = s + v;
	
	lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	phong = max(0.0, dot(normal, h) / (length(normal) * length(h)));
	
	diffuseColor = lambert * u_light4Diffuse * u_materialDiffuse; 
	specularColor = pow(phong, u_materialShininess) * u_light4Specular * u_materialSpecular; 
	
	vec4 light4CalcColor = (diffuseColor + specularColor);
		
	// The final color represented in the game, it is affected by the global ambient and the color value of each light, with respect to the players position
	v_color = u_globalAmbient * u_materialDiffuse + u_materialEmission + light1CalcColor + light2CalcColor + light3CalcColor + light4CalcColor;
	
	// Pos
	position = u_viewMatrix * position;
	gl_Position = u_projectionMatrix * position;
}