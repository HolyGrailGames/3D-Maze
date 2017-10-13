package com.ru.tgra.utilities;

public class Settings {
	
	public static int MAZE_WIDTH = 25;
	public static int MAZE_HEIGHT = 25;
	public static final float WALL_THICKNESS = 3.0f;
	public static final float WALL_HEIGHT = 5.0f;
	public static final float MOUSE_SENSITIVITY = 20.0f;
	public static final float CAMERA_SPEED = 5.0f;
	public static final float LIGHTS_OFFSET = 5.0f;

	/**
	 * Private constructor to prevent anyone from creating an instance of this class.
	 * This is done because the Settings class is to be used as a static class.
	 */
	private Settings() {}
}
