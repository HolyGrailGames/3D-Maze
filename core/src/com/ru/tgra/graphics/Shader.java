package com.ru.tgra.graphics;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Shader
{
	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;
	
	private int eyePosLoc;
	private int globalAmbLoc;

	private int light1DirLoc;
	private int light2DirLoc;
	private int light3DirLoc;
	private int light4DirLoc;
	
	private int light1PosLoc;
	private int light2PosLoc;
	private int light3PosLoc;
	private int light4PosLoc;
	
	private int light1DifLoc;
	private int light2DifLoc;
	private int light3DifLoc;
	private int light4DifLoc;
	
	private int light1SpecLoc;
	private int light2SpecLoc;
	private int light3SpecLoc;
	private int light4SpecLoc;
	
	private int matDifLoc;
	private int matShineLoc;
	private int matSpecLoc;
	private int matEmissionLoc;
	
	public Shader() {
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/vertexLighting3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/vertexLighting3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);
		
		Gdx.gl.glGetShaderInfoLog(vertexShaderID);
		Gdx.gl.glGetShaderInfoLog(fragmentShaderID);
	
		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);
		
		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");


		eyePosLoc 				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");
		globalAmbLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");
		
		light1PosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light1Position");
		light2PosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light2Position");
		light3PosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light3Position");
		light4PosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light4Position");
		
		light1DirLoc 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light1Direction");
		light2DirLoc 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light2Direction");
		light3DirLoc 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light3Direction");
		light4DirLoc 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light4Direction");
		
		light1DifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light1Diffuse");
		light2DifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light2Diffuse");
		light3DifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light3Diffuse");
		light4DifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light4Diffuse");
		
		light1SpecLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light1Specular");
		light2SpecLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light2Specular");
		light3SpecLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light3Specular");
		light4SpecLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light4Specular");
		
		matDifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
		matSpecLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
		matEmissionLoc 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");
		matShineLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");

		Gdx.gl.glUseProgram(renderingProgramID);
	}
	
	public void setEyePosition(float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(eyePosLoc, x, y, z, w);
	}

	public void setGlobalAmbient(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(globalAmbLoc, r, g, b, a);
	}
	
	// Light functions
	
	public void setLightPosition(float x, float y, float z, float w, int lightIndex) {
		switch (lightIndex) {
			case (1): {
				Gdx.gl.glUniform4f(light1PosLoc, x, y, z, w);
				break;
			}
			case (2): {
				Gdx.gl.glUniform4f(light2PosLoc, x, y, z, w);
				break;
			}
			case (3): {
				Gdx.gl.glUniform4f(light3PosLoc, x, y, z, w);
				break;
			}
			case (4): {
				Gdx.gl.glUniform4f(light4PosLoc, x, y, z, w);
				break;
			}
			default:
				break;
			}
	}

	public void setLightDirection(float x, float y, float z, float w, int lightIndex) {
		switch(lightIndex) {
			case (1): {	
				Gdx.gl.glUniform4f(light1DirLoc, x, y, z, w);
				break;
				}
			case (2): {
				Gdx.gl.glUniform4f(light2DirLoc, x, y, z, w);
				break;
			}
			case (3): {
				Gdx.gl.glUniform4f(light3DirLoc, x, y, z, w);
				break;
			}
			case (4): {
				Gdx.gl.glUniform4f(light4DirLoc, x, y, z, w);
				break;
			}
			default:
				break;
			}
	}
	
	public void setLightDiffuse(float r, float g, float b, float a, int lightIndex) {
		switch (lightIndex) {
			case (1): {
				Gdx.gl.glUniform4f(light1DifLoc, r, g, b, a);
				break;
			}
			case (2): {
				Gdx.gl.glUniform4f(light2DifLoc, r, g, b, a);
				break;
			}
			case (3): {
				Gdx.gl.glUniform4f(light3DifLoc, r, g, b, a);
				break;
			}
			case (4): {
				Gdx.gl.glUniform4f(light4DifLoc, r, g, b, a);
				break;
			}
			default:
				break;
			}
	}
	
	public void setLightSpecular(float r, float g, float b, float a, int lightIndex) {
		switch (lightIndex) {
		case (1): {
			Gdx.gl.glUniform4f(light1SpecLoc, r, g, b, a);
			break;
		}
		case (2): {
			Gdx.gl.glUniform4f(light2SpecLoc, r, g, b, a);
			break;
		}
		case (3): {
			Gdx.gl.glUniform4f(light3SpecLoc, r, g, b, a);
			break;
		}
		case (4): {
			Gdx.gl.glUniform4f(light4SpecLoc, r, g, b, a);
			break;
		}
		default:
			break;
		}
	}
	
	
	public void setMaterialDiffuse(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(matDifLoc, r, g, b, a);
	}
	

	public void setMaterialEmission(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(matEmissionLoc, r, g, b, a);
	}
	
	public void setMaterialSpecular(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(matSpecLoc, r, g, b, a);
	}
	

	public void setMaterialShininess(float shine) {
		Gdx.gl.glUniform1f(matShineLoc, shine);
	}
	
	
	public int getVertexPointer() {
		return this.positionLoc;
	}
	
	public int getNormalPointer() {
		return this.normalLoc;
	}
	
	public void setModelMatrix(FloatBuffer matrix)  {
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
	}
	
	public void setViewMatrix(FloatBuffer matrix)  {
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
	}
	
	public void setProjectionMatrix(FloatBuffer matrix)  {
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
	}
}
