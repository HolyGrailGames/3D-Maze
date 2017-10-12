package com.ru.tgra.lab1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ru.tgra.shapes.Maze3D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "3D Maze"; // or whatever you like
		config.width = 1365;  //experiment with
		config.height = 768;  //the window size
		config.x = 250;
		config.y = 150;
		//config.fullscreen = true;

		new LwjglApplication(new Maze3D(), config);
	}
}
