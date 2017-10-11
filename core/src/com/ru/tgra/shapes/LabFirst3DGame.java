package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.utils.BufferUtils;
import com.ru.tgra.graphics.Camera;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.Vector3D;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {
	
	private float angle;
	private Shader shader;
	private Camera cam;
	private Camera orthoCam;
	
	// Maze generating stuff
	private MazeGenerator generator;
	private Node[] nodes;
	private static final int MAZE_WIDTH = 25;
	private static final int MAZE_HEIGHT = 25;
	
	private float fov = 90.0f;

	//private ModelMatrix modelMatrix;

	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(this);
		shader = new Shader();
		
/*
		float[] mm = new float[16];

		mm[0] = 1.0f; mm[4] = 0.0f; mm[8] = 0.0f; mm[12] = 0.0f;
		mm[1] = 0.0f; mm[5] = 1.0f; mm[9] = 0.0f; mm[13] = 0.0f;
		mm[2] = 0.0f; mm[6] = 0.0f; mm[10] = 1.0f; mm[14] = 0.0f;
		mm[3] = 0.0f; mm[7] = 0.0f; mm[11] = 0.0f; mm[15] = 1.0f;

		modelMatrixBuffer = BufferUtils.newFloatBuffer(16);
		modelMatrixBuffer.put(mm);
		modelMatrixBuffer.rewind();

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);
*/
		//COLOR IS SET HERE
		
		shader.setColor(0.7f, 0.2f, 0, 1);
		
		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();

		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		 
		
		cam = new Camera();
		cam.look(new Point3D(-3f, 2f, 3f), new Point3D(0,3,0), new Vector3D(0,1,0));
		
		orthoCam = new Camera();
		orthoCam.orthographicProjection(-10, 10, -10, 10, 3.0f, 100);
		

		// Initialize a new maze;
		generator = new MazeGenerator(MAZE_WIDTH, MAZE_HEIGHT);
		generator.init();
		generator.generate();
		nodes = generator.getNodes();
	}

	private void input(float deltaTime)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.yaw(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.yaw(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.pitch(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.slide(-3.0f * deltaTime,  0,  0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.slide(3.0f * deltaTime,  0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.slide(0, 0, -3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.slide(0, 0, 3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			cam.slide(0, 3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			cam.slide(0, -3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.roll(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.roll(90.0f * deltaTime);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			fov += 30.0f * deltaTime;
		}
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		//angle += 180.0f * deltaTime;
		
		input(deltaTime);

		//do all updates to the game
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		for (int viewNum = 0; viewNum < 2; viewNum++) {
			if (viewNum == 0) {
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				cam.perspectiveProjection(fov, 1.0f, 0.4f, 100.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
			}
			else {
				Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
				Gdx.gl.glViewport(Gdx.graphics.getWidth() / 4 * 3, Gdx.graphics.getHeight()/4 * 3, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight()/4);
				orthoCam.look(new Point3D(cam.eye.x, 20.0f, cam.eye.z), cam.eye, new Vector3D(0,0,-1));
				//orthoCam.look(new Point3D(7.0f, 40.0f, -7.0f), new Point3D(7.0f, 0.0f, -7.0f), new Vector3D(0, 0, -1));
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());
			}
			
			ModelMatrix.main.loadIdentityMatrix();
			
			drawMaze();
			
			ModelMatrix.main.pushMatrix();
			shader.setColor(Color.RED.r, Color.RED.g, Color.RED.b, 1.0f);
			ModelMatrix.main.addTranslation(0.0f, -0.5f, 0.0f);
			ModelMatrix.main.addScale(100.0f, 1.0f, 100.0f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			if (viewNum == 1) {
				shader.setColor(1.0f, 0.3f, 0.1f, 1.0f);
				
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(cam.eye.x, cam.eye.y, cam.eye.z);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
			}
		}
	}

	public void drawMaze() {
		shader.setColor(0.8f, 0.8f, 0.2f, 1.0f);
		for (int i = 0; i < MAZE_WIDTH; i++) {
			for (int j = 0; j < MAZE_HEIGHT; j++) {
				if (nodes[j + i * MAZE_WIDTH].c == '#') {
					ModelMatrix.main.pushMatrix();
					ModelMatrix.main.addTranslation(i, 1, j);
					ModelMatrix.main.addScale(1.0f, 3.0f, 1.0f);
					shader.setModelMatrix(ModelMatrix.main.getMatrix());
					BoxGraphic.drawSolidCube();
					ModelMatrix.main.popMatrix();
				}
			}
		}
	}
	
	@Override
	public void render () {
		Gdx.graphics.setTitle("Ultimate 3D Maze | FPS: " + Gdx.graphics.getFramesPerSecond());
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}