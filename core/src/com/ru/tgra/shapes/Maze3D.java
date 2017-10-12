package com.ru.tgra.shapes;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.ru.tgra.graphics.Camera;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point2D;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.Vector3D;

public class Maze3D extends ApplicationAdapter implements InputProcessor {
	
	private float sunAngle;
	private Camera cam;
	private Camera orthoCam;
	
	public static Shader shader;
	// Maze generating stuff
	private MazeGenerator generator;
	private Node[] nodes;
	private List<Wall> walls;
	private static final int MAZE_WIDTH = 25;
	private static final int MAZE_HEIGHT = 25 ;
	private static final float WALL_THICKNESS = 3.0f;
	private static final float WALL_HEIGHT = 5.0f;
	private static final float MOUSE_SENSITIVITY = 20.0f;
	private static final float CAMERA_SPEED = 5.0f;
	private static final float LIGHTS_OFFSET = 5.0f;
	
	private Point2D lastMousePos;
	
	private float fov = 90.0f;

	//private ModelMatrix modelMatrix;

	/**
	 * Set up all data necessary to render scene.
	 */
	@Override
	public void create () {
	
		DisplayMode disp = Gdx.graphics.getDesktopDisplayMode();
		//Gdx.graphics.setDisplayMode(disp.width, disp.height, true);
		
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCursorCatched(true);
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
		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();

		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		// Initialize a new maze;
		generator = new MazeGenerator(MAZE_WIDTH, MAZE_HEIGHT);
		generator.init();
		generator.generate();
		nodes = generator.getNodes();
		walls = new ArrayList<>();
		initalizeWalls();
		
		cam = new Camera();
		cam.look(new Point3D(WALL_THICKNESS, 1, WALL_THICKNESS), getStartingLookAt(), new Vector3D(0,1,0));
		
		orthoCam = new Camera();
		orthoCam.orthographicProjection(-10, 10, -10, 10, 3.0f, 100);
		
		// Set the starting position of the mouse
		lastMousePos = new Point2D(0, Gdx.graphics.getHeight());
	}
	
	/**
	 * Handle the input from the user
	 * @param deltaTime time since last frame
	 */
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
			cam.slide(-CAMERA_SPEED * deltaTime,  0,  0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.slide(CAMERA_SPEED * deltaTime,  0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.slide(0, 0, -CAMERA_SPEED * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.slide(0, 0, CAMERA_SPEED * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			cam.slide(0, CAMERA_SPEED * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			cam.slide(0, -CAMERA_SPEED * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.roll(-MOUSE_SENSITIVITY * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.roll(MOUSE_SENSITIVITY * deltaTime);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			fov += 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.graphics.setDisplayMode(500, 500, false);
			Gdx.app.exit();
		}
	}
	
	/**
	 * Do all updates to the game.
	 */
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		sunAngle += 90.0f * deltaTime;
		
		input(deltaTime);
	}
	
	/**
	 * Display the scene
	 */
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// Setting the global ambient factor, this should not really be changed unless we want to implement a sun
		// With a day / night cycle, then we can gradually increase/decrease this value with deltaTime to simulate day and night.
		shader.setGlobalAmbient(0.2f, 0.2f, 0.2f, 1.0f);
		for (int viewNum = 0; viewNum < 2; viewNum++) {
			if (viewNum == 0) {
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				cam.perspectiveProjection(fov, 1.0f, 0.4f, 100.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
				shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);
			}
			else {
				Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
				Gdx.gl.glViewport(Gdx.graphics.getWidth() / 4 * 3, Gdx.graphics.getHeight()/4 * 3, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight()/4);
				orthoCam.look(new Point3D(cam.eye.x, 20.0f, cam.eye.z), cam.eye, new Vector3D(0,0,-1));
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());
				shader.setEyePosition(orthoCam.eye.x, orthoCam.eye.y, orthoCam.eye.z, 1.0f);
			}
			
			ModelMatrix.main.loadIdentityMatrix();
			
			// Can use this shit to rotate stuff
			//float s = (float)Math.sin(sunAngle * Math.PI / 180.0);
			//float c = (float)Math.cos(sunAngle * Math.PI / 180.0);
			
			// Setting up the light for the maze
			// Draw 4 spheres representing our 4 light sources in the maze!
			drawSphere((MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS), 10.0f, (MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS));
			drawSphere((0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)), 10.0f, (MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS));
			drawSphere((MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS), 10.0f, (0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)));
			drawSphere((0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)), 10.0f, (0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)));
			
			// Place light sources on the same location
			setupLightSource((MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS), 10.0f, (MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS), 1);
			setupLightSource((0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)), 10.0f, (MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS), 1);
			setupLightSource((MAZE_WIDTH * WALL_THICKNESS) - (LIGHTS_OFFSET * WALL_THICKNESS), 10.0f, (0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)), 1);
			setupLightSource((0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)), 10.0f, (0.0f + (LIGHTS_OFFSET * WALL_THICKNESS)), 1);
			
			// Draw all the walls of the maze.
			// NOTE: Not all elements of the array have walls in them, some parts are empty.
			for (Wall wall : walls) {
				wall.draw();	
			}
			
			shader.setMaterialShininess(20.0f);
			shader.setMaterialSpecular(0.5f, 0.5f, 0.5f, 1.0f);
			
			// Draw the floor of the maze.
			ModelMatrix.main.pushMatrix();
			shader.setMaterialDiffuse(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g, Color.LIGHT_GRAY.r, 1.0f);
			ModelMatrix.main.addTranslation((MAZE_WIDTH*3)/2, -0.5f, (MAZE_HEIGHT*3)/2);
			ModelMatrix.main.addScale(MAZE_WIDTH*3, 1.0f, MAZE_HEIGHT*3);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			if (viewNum == 1) {
				shader.setMaterialDiffuse(1.0f, 0.3f, 0.1f, 1.0f);
				
				ModelMatrix.main.pushMatrix();
				shader.setMaterialDiffuse(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, 1.0f);
				ModelMatrix.main.addTranslation(cam.eye.x, cam.eye.y, cam.eye.z);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
			}
		}
	}
	
	public void setupLightSource(float x, float y, float z, int lightIndex) {
		// set the position to equal the position we send in
		shader.setLightPosition(x, y, z, 1.0f, lightIndex);
		// Set the lights to point straight down from where its placed
		shader.setLightDirection(x, 0f, z, 1.0f, lightIndex);
		// Play with these parameters for effects
		shader.setLightDiffuse(1.0f,  1.0f,  1.0f,  1.0f, lightIndex);
		shader.setLightSpecular(1.0f, 1.0f, 1.0f, 1.0f, lightIndex);
	}
	
	public void drawSphere(float x, float y, float z) {
		// Drawing a sphere where the light is (is it a sun? no! is it a plane? no! its just a sphere!)
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(x, y, z);
		// Make the sphere emissive
		shader.setMaterialEmission(0.5f, 0.5f, 0.5f, 1.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		// Make sure to remove emission before leaving this function
		shader.setMaterialEmission(0f, 0f, 0f, 1.0f);
		ModelMatrix.main.popMatrix();
		
	}
	
	/**
	 * Draw the maze generated by the mazegenerator
	 */
	public void initalizeWalls() {
		for (int i = 0; i < MAZE_WIDTH; i++) {
			for (int j = 0; j < MAZE_HEIGHT; j++) {
				if (nodes[j + i * MAZE_WIDTH].c == '#') {
					walls.add(new Wall(new Point3D(i * WALL_THICKNESS, 1, j * WALL_THICKNESS), new Vector3D(WALL_THICKNESS, WALL_HEIGHT, WALL_THICKNESS))); 
				}
			}
		}
	}
	
	/**
	 * Render the scene
	 */
	@Override
	public void render () {
		Gdx.graphics.setTitle("Ultimate 3D Maze | FPS: " + Gdx.graphics.getFramesPerSecond());
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();
	}
	
	private Point3D getStartingLookAt() {
		if (nodes[1 + 2 * MAZE_WIDTH].c == ' ') {
			System.out.println("first");
			return new Point3D(2*WALL_THICKNESS, 1, WALL_THICKNESS);
		}
		System.out.println("second");
		return new Point3D(WALL_THICKNESS, 1, 2*WALL_THICKNESS);
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
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		Point2D currMousePos = new Point2D(screenX, screenY);
		
		Point2D deltaMouse = currMousePos.subtract(lastMousePos);
		lastMousePos = currMousePos;
		
		if(deltaMouse.x < 0) {
			cam.yaw(MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.x));
		}
		if(deltaMouse.x > 0) {
			cam.yaw(-MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.x));
		}
		if(deltaMouse.y < 0) {
			cam.pitch(MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.y));
		}
		if(deltaMouse.y > 0) {
			cam.pitch(-MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.y));
		}
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}