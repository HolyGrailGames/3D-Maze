package com.ru.tgra.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.shapes.Node;

public class Utilities
{
	public static float euclidianDistance(Point3D p2, Point3D p1) {
		return (float)Math.sqrt(((p2.x-p1.x)*(p2.x-p1.x)) + ((p2.z-p1.z)*(p2.z-p1.z)));
	}
	
	public static float Clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
	
	public static void writeNodesToFile(Node nodes[], int size) {

		try {
	        FileWriter writer = new FileWriter("myMaze.txt", false);
	        writer.write(new Integer(size).toString() + "\n");
	        for (int i = 0; i < size; i++) {
	            for (int j = 0; j < size; j++) {
	                  writer.write(nodes[i + j * size].c);
	            }
	            writer.write("\n");
	        }
	        writer.flush();
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static Node[] readNodesFromFile(String filename) {
		Node nodes[] = new Node[1];
		try {
			Scanner scanner = new Scanner(new File(filename));
			
			int size = scanner.nextInt();
	        Settings.MAZE_WIDTH = size;
	        Settings.MAZE_HEIGHT = size;
	        
	        nodes = new Node[Settings.MAZE_WIDTH * Settings.MAZE_HEIGHT];
	        
	        scanner.useDelimiter("");
	        // remove first trailing newline
	        char next = scanner.next().charAt(0);
	        
	        
	        for (int i = 0; i < Settings.MAZE_HEIGHT; i++) {
	        	int j = 0;
	        	next = scanner.next().charAt(0);
	        	while (next != '\n') {
	        		
	        		Node node = new Node();
	        		node.c = next;
	        		nodes[i + j * Settings.MAZE_HEIGHT] = node;
	        		if (!scanner.hasNext()) {
	        			break;
	        		}
	        		next = scanner.next().charAt(0);
	        		j++;
	        	}
	        }
	        scanner.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return nodes;
	}
}
