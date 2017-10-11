package com.ru.tgra.shapes;

public class Node {
	public Point3D position;
	public float size;
	public Node parent;
	public int dirs;
	public boolean isWall;
	
	public Node(Point3D position, float size, Node parent, int dirs, boolean isWall) {
		this.position = position;
		this.size = size;
		this.parent = parent;
		this.dirs = dirs;
		this.isWall = isWall;
	}
}
