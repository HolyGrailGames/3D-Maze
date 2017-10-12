
#ifdef GL_ES
precision mediump float;
#endif


uniform vec4 u_lightDiffuse;
uniform vec4 u_lightSpecular;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform float u_materialShininess;


varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;

void main()
{
	// Lighting

	float lambert = max(0.0, dot(v_normal, v_s) / (length(v_normal) * length(v_s)));
	float phong = max(0.0,dot(v_normal, v_h) / (length(v_normal) * length(v_h)));
	
	// vec4 1111 is material specular and u_lightDiffuse should be material
	gl_FragColor =  lambert * u_lightDiffuse * u_materialDiffuse + pow(phong, u_materialShininess) * u_lightSpecular * u_materialSpecular;  
}