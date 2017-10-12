package com.ru.tgra.collisions;

import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.shapes.Maze3D;
import com.ru.tgra.utilities.Settings;

public class Collisions {
	public static void checkCollisions(Point3D eye) {
		
		int i = (int) (eye.x+1.5f) / 3;
		int j = (int) (eye.z+1.5f) / 3;
		
		if (Maze3D.nodes[j-1 + i * Settings.MAZE_WIDTH].c == '#') {
			//System.out.println("wall north");
		}
		
		//Maze3D.nodes[j + i * Maze3D.MAZE_WIDTH];
	}
}
