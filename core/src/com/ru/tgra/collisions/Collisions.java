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
		
		boolean collision = false;
		
		if (mazeX > 0 && mazeX < Settings.MAZE_WIDTH && mazeY > 0 && mazeY < Settings.MAZE_HEIGHT) {
			
			checkElevatorCollisions(cam);
			
			if (cam.eye.x-cam.radius < leftBound && Maze3D.nodes[mazeY + (mazeX-1) * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(leftBound+cam.radius, cam.eye.y, cam.eye.z);
				collision = true;
			}
			if (cam.eye.x+cam.radius > rightBound && Maze3D.nodes[mazeY + (mazeX+1) * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(rightBound-cam.radius, cam.eye.y, cam.eye.z);
				collision = true;
			}
			if (cam.eye.z-cam.radius < topBound && Maze3D.nodes[(mazeY-1) + mazeX * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(cam.eye.x, cam.eye.y, topBound+cam.radius);
				collision = true;
			}
			if (cam.eye.z+cam.radius > bottomBound && Maze3D.nodes[(mazeY+1) + mazeX * Settings.MAZE_WIDTH].c == '#') {
				cam.setEye(cam.eye.x, cam.eye.y, bottomBound-cam.radius);
				collision = true;
			}
			
			// Only check the corners if we haven't collided yet
			if (!collision) {
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
	
	// We got help from Smari and Darri implementing this function
	private static void checkElevatorCollisions(Camera cam) {
		float maxX = Maze3D.bobbingBlock.position.x+Maze3D.bobbingBlock.scale.x/2+cam.radius;
		float minX = Maze3D.bobbingBlock.position.x-Maze3D.bobbingBlock.scale.x/2-cam.radius;
		float maxZ = Maze3D.bobbingBlock.position.z+Maze3D.bobbingBlock.scale.z/2+cam.radius;
		float minZ = Maze3D.bobbingBlock.position.z-Maze3D.bobbingBlock.scale.z/2-cam.radius;
		float maxY = Maze3D.bobbingBlock.position.y+Maze3D.bobbingBlock.scale.y/2+cam.radius;
		float minY = Maze3D.bobbingBlock.position.y-Maze3D.bobbingBlock.scale.y/2-cam.radius;
		
		if(cam.eye.x >= minX && cam.eye.x <= maxX 
				&& cam.eye.z >= minZ && cam.eye.z <= maxZ
				&& cam.eye.y >= minY && cam.eye.y <= maxY){
			float minDis = Integer.MAX_VALUE;
			int d = 0;
			
			if(Math.abs(maxX-cam.eye.x) < minDis) {
				minDis = Math.abs(maxX-cam.eye.x);
				d = 1;
			}
			if(Math.abs(minX-cam.eye.x) < minDis) {
				minDis = Math.abs(minX-cam.eye.x);
				d = 2;
			}
			if(Math.abs(maxZ-cam.eye.z) < minDis) {
				minDis = Math.abs(maxZ-cam.eye.z);
				d = 3;
			}
			if(Math.abs(minZ-cam.eye.z) < minDis) {
				minDis = Math.abs(minZ-cam.eye.z);
				d = 4;
			}
			if(Math.abs(maxY-cam.eye.y) < minDis) {
				minDis = Math.abs(maxY-cam.eye.y);
				d = 5;
			}
			if(Math.abs(minY-cam.eye.y) < minDis) {
				minDis = Math.abs(minY-cam.eye.y);
				d = 6;
			}
			
			if(d == 1){ cam.setEye(maxX, cam.eye.y, cam.eye.z); }
			if(d == 2){ cam.setEye(minX, cam.eye.y, cam.eye.z); }
			if(d == 3){ cam.setEye(cam.eye.x, cam.eye.y, maxZ); }
			if(d == 4){ cam.setEye(cam.eye.x, cam.eye.y, minZ); }
			if(d == 5) { 
				cam.setEye(cam.eye.x, maxY, cam.eye.z);
				cam.onElevator = true;
			}
			if(d == 6) { 
				cam.setEye(cam.eye.x, minY, cam.eye.z);
				cam.onElevator = true;
			}
		}
		else {
			cam.onElevator = false;
		}
	}
}
