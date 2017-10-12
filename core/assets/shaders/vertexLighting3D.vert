
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
uniform vec4 u_light1Position;
uniform vec4 u_light2Position;
uniform vec4 u_light3Position;
uniform vec4 u_light4Position;

uniform vec4 u_light1Direction;
uniform vec4 u_light2Direction;
uniform vec4 u_light3Direction;
uniform vec4 u_light4Direction;

uniform vec4 u_light1Diffuse;
uniform vec4 u_light2Diffuse;
uniform vec4 u_light3Diffuse;
uniform vec4 u_light4Diffuse;

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
	vec4 v = u_eyePosition - position; // Direction to the camera	
	
	// Duplicate the following stuff for adding more lights to the game!
	// LIGHT 1!!	
	vec4 s = u_light1Position - position; // Direction to the light
	vec4 h = s + v;
	
	float lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	float phong = max(0.0, dot(normal, h) / (length(normal) * length(h)));
	
	vec4 diffuseColor = lambert * u_light1Diffuse * u_materialDiffuse; 
	vec4 specularColor = pow(phong, u_materialShininess) * u_light1Specular * u_materialSpecular; 
	
	
	//float spotAttenuation = max(0.0, dot(-s, u_light1Direction) / (length(s) * length(u_light1Direction)));
	
	//vec4 light1CalcColor = spotAttenuation * (diffuseColor + specularColor)
	vec4 light1CalcColor = (diffuseColor + specularColor);
	
	// LIGHT 2!!
	s = u_light2Position - position; // Direction to the light
	h = s + v;
	
	lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	phong = max(0.0, dot(normal, h) / (length(normal) * length(h)));
	
	diffuseColor = lambert * u_light2Diffuse * u_materialDiffuse; 
	specularColor = pow(phong, u_materialShininess) * u_light2Specular * u_materialSpecular; 
	
	//spotAttenuation = max(0.0, dot(-s, u_light2Direction) / (length(s) * length(u_light2Direction)));
	
	//vec4 light2CalcColor = spotAttenuation * (diffuseColor + specularColor);
	vec4 light2CalcColor = (diffuseColor + specularColor);
	
	// LIGHT 3!!
	s = u_light3Position - position; // Direction to the light
	h = s + v;
	
	lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	phong = max(0.0, dot(normal, h) / (length(normal) * length(h)));
	
	diffuseColor = lambert * u_light3Diffuse * u_materialDiffuse; 
	specularColor = pow(phong, u_materialShininess) * u_light3Specular * u_materialSpecular; 
	
	//spotAttenuation = max(0.0, dot(-s, u_light3Direction) / (length(s) * length(u_light3Direction)));
	
	//vec4 light3CalcColor = spotAttenuation * (diffuseColor + specularColor);
	vec4 light3CalcColor = (diffuseColor + specularColor);
	
	// LIGHT 4!!
	s = u_light4Position - position; // Direction to the light
	h = s + v;
	
	lambert = max(0.0, dot(normal, s) / (length(normal) * length(s)));
	phong = max(0.0, dot(normal, h) / (length(normal) * length(h)));
	
	diffuseColor = lambert * u_light4Diffuse * u_materialDiffuse; 
	specularColor = pow(phong, u_materialShininess) * u_light4Specular * u_materialSpecular; 
	
	//spotAttenuation = max(0.0, dot(-s, u_light4Direction) / (length(s) * length(u_light4Direction)));
	
	//vec4 light4CalcColor = spotAttenuation * (diffuseColor + specularColor);
	vec4 light4CalcColor = (diffuseColor + specularColor);
		
	v_color = u_globalAmbient * u_materialDiffuse + u_materialEmission + light1CalcColor + light2CalcColor + light3CalcColor + light4CalcColor;
	
	// Pos
	position = u_viewMatrix * position;
	gl_Position = u_projectionMatrix * position;
}