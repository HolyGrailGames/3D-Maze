package com.ru.tgra.shapes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.ru.tgra.collisions.Collisions;
import com.ru.tgra.graphics.Camera;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point2D;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.Vector3D;
import com.ru.tgra.utilities.Settings;
import com.ru.tgra.utilities.Utilities;

public class Maze3D extends ApplicationAdapter implements InputProcessor {
	
	private Camera cam;
	private Camera orthoCam;
	
	//public static BobbingBlock bobbingBlock;
	
	public static Shader shader;
	// Maze generating stuff
	private MazeGenerator generator;
	public static Node[] nodes;
	private List<Wall> walls = new ArrayList<>();
	
	private Point2D lastMousePos = null;
	private Point3D playerStartPos = new Point3D();
	private Point3D playerStartLookAt = new Point3D();
	
	private float fov = 90.0f;
	
	private String filename = null;
	
	Random rand = new Random();
	
	public static List<BobbingBlock> elevators = new ArrayList<>();
	//public static List<Coin> coins = new ArrayList<>();
	
	public static HashMap<Integer, Coin> coins = new HashMap<>();

	//private ModelMatrix modelMatrix;
	
	public static Point3D topLeft = new Point3D();
	
	public Maze3D(String filename) {
		if (filename != null) {
			this.filename = filename;
		}
	}

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
		if (filename == null) {
			System.out.println("Randomly Generating maze...");
			generator = new MazeGenerator(Settings.MAZE_WIDTH, Settings.MAZE_HEIGHT);
			generator.init();
			generator.generate();
			nodes = generator.getNodes();
			fillMaze();
			Utilities.writeNodesToFile(nodes, Settings.MAZE_WIDTH);
			drawAscii(nodes);
		}
		else {
			System.out.println("Reading maze from file...");
			nodes = Utilities.readNodesFromFile(filename);
			drawAscii(nodes);
		}
		
		initializeMaze();
		
		cam = new Camera();
		//cam.look(new Point3D(Settings.WALL_THICKNESS, 1, Settings.WALL_THICKNESS), lookAtPoint, new Vector3D(0,1,0));
		cam.look(playerStartPos, playerStartLookAt, new Vector3D(0,1,0));
		orthoCam = new Camera();
		orthoCam.orthographicProjection(-10, 10, -10, 10, 3.0f, 100);
		
		
		
		
		
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
		/*
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.pitch(-90.0f * deltaTime);
		}
		*/
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.slide(-Settings.CAMERA_SPEED * deltaTime,  0,  0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.slide(Settings.CAMERA_SPEED * deltaTime,  0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.slide(0, 0, -Settings.CAMERA_SPEED * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.slide(0, 0, Settings.CAMERA_SPEED * deltaTime);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			cam.jump();
		}
		
		/*
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			cam.slide(0, Settings.CAMERA_SPEED * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			cam.slide(0, -Settings.CAMERA_SPEED * deltaTime, 0);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.roll(-Settings.MOUSE_SENSITIVITY * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.roll(Settings.MOUSE_SENSITIVITY * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			fov += 30.0f * deltaTime;
		}
		*/
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
		cam.applyGravity(deltaTime);
		input(deltaTime);
		
		for (BobbingBlock block : elevators) {
			block.update(deltaTime);
		}
		for (Coin coin : coins.values()) {
			coin.update(deltaTime);
		}
		
		
		Collisions.checkCollisions(cam);
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
			drawSphere((Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS), 
					10.0f, (Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS));
			drawSphere((0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)), 
					10.0f, (Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS));
			drawSphere((Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS), 
					10.0f, (0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)));
			drawSphere((0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)), 
					10.0f, (0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)));
			
			// Place light sources on the same location
			setupLightSource((Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS), 
					10.0f, (Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS), 1);
			setupLightSource((0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)), 
					10.0f, (Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS), 1);
			setupLightSource((Settings.MAZE_WIDTH * Settings.WALL_THICKNESS) - (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS), 
					10.0f, (0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)), 1);
			setupLightSource((0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)), 
					10.0f, (0.0f + (Settings.LIGHTS_OFFSET * Settings.WALL_THICKNESS)), 1);
			
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
			ModelMatrix.main.addTranslation((Settings.MAZE_WIDTH*3)/2, -0.5f, (Settings.MAZE_HEIGHT*3)/2);
			ModelMatrix.main.addScale(Settings.MAZE_WIDTH*3, 1.0f, Settings.MAZE_HEIGHT*3);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			// Draw elevators
			for (BobbingBlock block : elevators) {
				block.draw(Color.YELLOW, Color.YELLOW);
			}
			for (Coin coin : coins.values()) {
				coin.draw(Color.OLIVE, Color.YELLOW, Color.BLUE);
			}
			
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
		for (int i = 0; i < Settings.MAZE_WIDTH; i++) {
			for (int j = 0; j < Settings.MAZE_HEIGHT; j++) {
				if (nodes[j + i * Settings.MAZE_WIDTH].c == '#') {
					walls.add(new Wall(new Point3D(i * Settings.WALL_THICKNESS, 1, j * Settings.WALL_THICKNESS), 
							new Vector3D(Settings.WALL_THICKNESS, Settings.WALL_HEIGHT, Settings.WALL_THICKNESS))); 
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
		
		//System.out.println("("+cam.eye.x+", "+cam.eye.y+", "+cam.eye.z+")");
	}
	
	private Point3D getStartingLookAt() {
		if (nodes[1 + 2 * Settings.MAZE_WIDTH].c == ' ' 
				|| nodes[1 + 2 * Settings.MAZE_WIDTH].c == 'E'
				|| nodes[1 + 2 * Settings.MAZE_WIDTH].c == 'c') {
			nodes[1 + 2 * Settings.MAZE_WIDTH].c = 'c';
			return new Point3D(2*Settings.WALL_THICKNESS, 1, Settings.WALL_THICKNESS);
		}
		nodes[2 + 1 * Settings.MAZE_WIDTH].c = 'E';
		return new Point3D(Settings.WALL_THICKNESS, 1, 2*Settings.WALL_THICKNESS);
	}
	
	private void fillMaze() {
		nodes[1 + 1 * Settings.MAZE_WIDTH].c = 'P';
		for (int i = 1; i < Settings.MAZE_HEIGHT-1; i++) {
			// Set elevators
				// get random int within the outer walls of the maze
				int randIndex = rand.nextInt(Settings.MAZE_WIDTH-2)+1;
				while (randIndex < Settings.MAZE_WIDTH-2 && nodes[randIndex + i * Settings.MAZE_WIDTH].c != ' ') {
					randIndex++;
				}
				// Only set Elevator if we haven't hit the end of the width
				if(nodes[randIndex + i * Settings.MAZE_WIDTH].c != '#') {
					nodes[randIndex + i * Settings.MAZE_WIDTH].c = 'E';
				}
			
			// set coins
			for (int j = 1; j < Settings.MAZE_WIDTH-1; j++) {
				// 33% chance to spawn a coin on in an empty cell
				if (rand.nextInt(3) == 0 && nodes[j + i * Settings.MAZE_WIDTH].c == ' ') {
					nodes[j + i * Settings.MAZE_WIDTH].c = 'c';
				}
			}
        }
	}
	
	private void initializeMaze() {
		boolean playerIsSet = false;
		for (int i = 0; i < Settings.MAZE_WIDTH; i++) {
			for (int j = 0; j < Settings.MAZE_HEIGHT; j++) {
				// Add wall
				if (nodes[j + i * Settings.MAZE_WIDTH].c == '#') {
					walls.add(new Wall(new Point3D(i * Settings.WALL_THICKNESS, 1, j * Settings.WALL_THICKNESS), 
							new Vector3D(Settings.WALL_THICKNESS, Settings.WALL_HEIGHT, Settings.WALL_THICKNESS))); 
				}
				// Add elevator
				if (nodes[j + i * Settings.MAZE_WIDTH].c == 'E') {
					elevators.add(new BobbingBlock(new Point3D(i*Settings.WALL_THICKNESS, -1.0f, j*Settings.WALL_THICKNESS), new Vector3D(Settings.WALL_THICKNESS, 4.5f, Settings.WALL_THICKNESS)));
				}
				// Add coin
				if (nodes[j + i * Settings.MAZE_WIDTH].c == 'c') {
					coins.put(j+i*Settings.MAZE_WIDTH, new Coin(new Point3D(i*Settings.WALL_THICKNESS, 1.0f, j*Settings.WALL_THICKNESS)));
				}
				// Set player starting position and look at
				if (!playerIsSet && nodes[j + i * Settings.MAZE_WIDTH].c == 'P') {
					playerStartPos.set(i*Settings.WALL_THICKNESS, 1.0f, j*Settings.WALL_THICKNESS);
					
					if (i > 0 && i < Settings.MAZE_WIDTH && j > 0 && j < Settings.MAZE_HEIGHT) {
						// Look left
						if (Maze3D.nodes[j + (i-1) * Settings.MAZE_WIDTH].c != '#') {
							playerStartLookAt.set((i-1)*Settings.WALL_THICKNESS, 1, j*Settings.WALL_THICKNESS);
						}
						// Look right
						else if (Maze3D.nodes[j + (i+1) * Settings.MAZE_WIDTH].c != '#') {
							playerStartLookAt.set((i+1)*Settings.WALL_THICKNESS, 1, j*Settings.WALL_THICKNESS);
						}
						// Look up
						else if (Maze3D.nodes[(j-1) + i * Settings.MAZE_WIDTH].c != '#') {
							playerStartLookAt.set(i*Settings.WALL_THICKNESS, 1, (j-1)*Settings.WALL_THICKNESS);
						}
						// Look down
						else if (Maze3D.nodes[(j+1) + i * Settings.MAZE_WIDTH].c != '#') {
							playerStartLookAt.set(i*Settings.WALL_THICKNESS, 1, (j+1)*Settings.WALL_THICKNESS);
						}
						System.out.println("("+playerStartLookAt.x+", "+playerStartLookAt.y+", "+playerStartLookAt.z+")");
					}
					else {
						playerStartLookAt.set(playerStartPos.x+Settings.WALL_THICKNESS, 1, playerStartPos.z);
					}
					playerIsSet = true;
				}
			}
		}
	}
	
	void drawAscii(Node nodes[])
	{
		//Outputs maze to terminal - nothing special
		for (int i = 0; i < Settings.MAZE_HEIGHT; i++ )
		{
			for (int j = 0; j < Settings.MAZE_WIDTH; j++ )
			{
				System.out.print(nodes[i + j * Settings.MAZE_HEIGHT].c + " ");
			}
			System.out.println();
		}
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
		
		if (lastMousePos == null) {
			lastMousePos = new Point2D(screenX, screenY);
			return false;
		}
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		Point2D currMousePos = new Point2D(screenX, screenY);
		
		Point2D deltaMouse = currMousePos.subtract(lastMousePos);
		lastMousePos = currMousePos;
		
		if(deltaMouse.x < 0) {
			cam.yaw(Settings.MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.x));
		}
		if(deltaMouse.x > 0) {
			cam.yaw(-Settings.MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.x));
		}
		/*
		if(deltaMouse.y < 0) {
			cam.pitch(MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.y));
		}
		if(deltaMouse.y > 0) {
			cam.pitch(-MOUSE_SENSITIVITY * deltaTime * Math.abs(deltaMouse.y));
		}
		*/
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}