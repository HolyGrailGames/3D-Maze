package com.ru.tgra.graphics;

public class Point3D {

	public float x;
	public float y;
	public float z;

	public Point3D()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Point3D(Point3D point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}
	
	public Point3D(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void add(Vector3D v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public void set(float x, float y, float z)  
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void translate(float dx, float dy, float dz)  
	{
		this.x += dx;
		this.y += dy;
		this.z += dz;
	}
}
