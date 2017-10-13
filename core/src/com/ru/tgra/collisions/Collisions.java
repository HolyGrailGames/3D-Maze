package com.ru.tgra.collisions;

import com.ru.tgra.graphics.Camera;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Vector3D;
import com.ru.tgra.shapes.BobbingBlock;
import com.ru.tgra.shapes.Maze3D;
import com.ru.tgra.utilities.Settings;
import com.ru.tgra.utilities.Utilities;

public class Collisions {
	
	
	
	static float leftBound, rightBound, topBound, bottomBound;
	static Point3D topLeft     = new Point3D();
	static Point3D topRight    = new Point3D();
	static Point3D bottomLeft  = new Point3D();
	static Point3D bottomRight = new Point3D();
	
	static Point3D coinPosition = new Point3D();
	
	public static void checkCollisions(Camera cam) {
		
		int mazeX = (int) ((cam.eye.x+(Settings.WALL_THICKNESS/2)) / Settings.WALL_THICKNESS);
		int mazeY = (int) ((cam.eye.z+(Settings.WALL_THICKNESS/2)) / Settings.WALL_THICKNESS);
		
		// Calculate bounding box around the cell that the player is currently in
		leftBound   = mazeX*3 - (Settings.WALL_THICKNESS/2);
		rightBound  = mazeX*3 + (Settings.WALL_THICKNESS/2);
		topBound    = mazeY*3 - (Settings.WALL_THICKNESS/2);
		bottomBound = mazeY*3 + (Settings.WALL_THICKNESS/2);
		
		boolean collision = false;
		
		if (mazeX > 0 && mazeX < Settings.MAZE_WIDTH && mazeY > 0 && mazeY < Settings.MAZE_HEIGHT) {
			
			// Check collision against elevators
			for (BobbingBlock elevator : Maze3D.elevators) {
				checkElevatorCollisions(elevator, cam);
			}
			
			if (Maze3D.nodes[mazeY + mazeX * Settings.MAZE_WIDTH].c == 'c') {
				coinPosition.set(mazeX*Settings.WALL_THICKNESS, 1.0f, mazeY*Settings.WALL_THICKNESS);
				
				if (Utilities.euclidianDistance(cam.eye, coinPosition) < cam.radius) {
					if (Maze3D.coins.remove(mazeY + mazeX * Settings.MAZE_WIDTH) != null) {
						Maze3D.nodes[mazeY + mazeX * Settings.MAZE_WIDTH].c = ' ';
					}
				}
				
			}
			
			// Check collision against walls
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
			
			// Only check the corners of walls if we haven't collided with any walls yet
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
	private static void checkElevatorCollisions(BobbingBlock elevator, Camera cam) {
		float maxX = elevator.position.x+elevator.scale.x/2+cam.radius;
		float minX = elevator.position.x-elevator.scale.x/2-cam.radius;
		float maxZ = elevator.position.z+elevator.scale.z/2+cam.radius;
		float minZ = elevator.position.z-elevator.scale.z/2-cam.radius;
		float maxY = elevator.position.y+elevator.scale.y/2+cam.radius;
		float minY = elevator.position.y-elevator.scale.y/2-cam.radius;
		
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
				cam.velocity.y = 0.0f;
			}
			if(d == 6) { 
				cam.setEye(cam.eye.x, minY, cam.eye.z);
				cam.velocity.y = 0.0f;
			}
		}
	}
}
