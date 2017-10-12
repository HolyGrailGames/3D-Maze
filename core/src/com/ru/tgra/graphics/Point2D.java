package com.ru.tgra.graphics;

public class Point2D {
	public float x;
	public float y;

	public Point2D()
	{
		this.x = 0;
		this.y = 0;
	}

	public Point2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point2D subtract(Point2D p) {
		return new Point2D(x-p.x, y-p.y);
	}
	
	public void set(float x, float y, float z)  
	{
		this.x = x;
		this.y = y;
	}
}
