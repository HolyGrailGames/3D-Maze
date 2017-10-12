package com.ru.tgra.shapes;

public class Node {

	public int x, y; //Node position - little waste of memory, but it allows faster generation
	public Node parent; //Pointer to parent node
	public char c; //Character to be displayed
	public char dirs; //Directions that still haven't been explored
	
	public Node(int x, int y, Node parent, char c, char dirs) {
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.c = c;
		this.dirs = dirs;
	}
	
	public Node() {
		// Default constructor intentionally left empty
	}
}
