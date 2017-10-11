package com.ru.tgra.shapes;

public class Node {

	int x, y; //Node position - little waste of memory, but it allows faster generation
	Node parent; //Pointer to parent node
	char c; //Character to be displayed
	char dirs; //Directions that still haven't been explored
	
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
