
#ifdef GL_ES
precision mediump float;
#endif




varying vec4 v_color;

void main()
{
	// Lighting
	gl_FragColor = v_color;    
}