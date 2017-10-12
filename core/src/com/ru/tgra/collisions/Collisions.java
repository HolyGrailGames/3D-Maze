package com.ru.tgra.collisions;

import com.ru.tgra.graphics.Camera;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Vector3D;
import com.ru.tgra.shapes.Maze3D;
import com.ru.tgra.utilities.Settings;
import com.ru.tgra.utilities.Utilities;

public class Collisions {
	
	static float leftBound, rightBound, topBound, bottomBound;
	static Point3D topLeft     = new Point3D();
	static Point3D topRight    = new Point3D();
	static Point3D bottomLeft  = new Point3D();
	static Point3D bottomRight = new Point3D();
	
	public static void checkCollisions(Camera cam) {
		
		int mazeX = (int) ((cam.eye.x+(Settings.WALL_THICKNESS/2)) / Settings.WALL_THICKNESS);
		int mazeY = (int) ((cam.eye.z+(Settings.WALL_THICKNESS/2)) / Settings.WALL_THICKNESS);
		
		leftBound   = mazeX*3 - (Settings.WALL_THICKNESS/2);
		rightBound  = mazeX*3 + (Settings.WALL_THICKNESS/2);
		topBound    = mazeY*3 - (Settings.WALL_THICKNESS/2);
		bottomBound = mazeY*3 + (Settings.WALL_THICKNESS/2);
		
		if (mazeX > 0 && mazeX < Settings.MAZE_WIDTH && mazeY > 0 && mazeY < Settings.MAZE_HEIGHT) {
			
			if (cam.eye.x-cam.radius < leftBound && Maze3D.nodes[mazeY + (mazeX-1) * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(leftBound+cam.radius, cam.eye.y, cam.eye.z);
			}
			if (cam.eye.x+cam.radius > rightBound && Maze3D.nodes[mazeY + (mazeX+1) * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(rightBound-cam.radius, cam.eye.y, cam.eye.z);
			}
			if (cam.eye.z-cam.radius < topBound && Maze3D.nodes[(mazeY-1) + mazeX * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(cam.eye.x, cam.eye.y, topBound+cam.radius);
			}
			if (cam.eye.z+cam.radius > bottomBound && Maze3D.nodes[(mazeY+1) + mazeX * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(cam.eye.x, cam.eye.y, bottomBound-cam.radius);
			}
			
			topLeft.set(leftBound, 1, topBound);
			topRight.set(rightBound, 1, topBound);
			bottomLeft.set(leftBound, 1, bottomBound);
			bottomRight.set(rightBound, 1, bottomBound);
			
			checkCornerCollision(topLeft, cam);
			checkCornerCollision(topRight, cam);
			checkCornerCollision(bottomLeft, cam);
			checkCornerCollision(bottomRight, cam);
		}
	}
	
	private static void checkCornerCollision(Point3D corner, Camera cam) {
		if (Utilities.euclidianDistance(corner, cam.eye) < cam.radius) {
			Vector3D cornerDirection = Vector3D.difference(cam.eye, corner);
			
			float distance = cornerDirection.length();
			
			if(distance < cam.radius) {
				cornerDirection.normalize();
				cornerDirection.scale(cam.radius - distance);
				
				cam.setEye(cam.eye.x + cornerDirection.x, cam.eye.y, cam.eye.z + cornerDirection.z);
			}
		}
	}
}
