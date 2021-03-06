package com.ru.tgra.graphics;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BufferUtils;
import com.ru.tgra.utilities.Utilities;

public class Camera 
{
	public Vector3D velocity;
	public float speed;
	public float jumpFactor;
	public Point3D eye;
	Vector3D u;
	Vector3D v;
	Vector3D n;
	
	private static final float GRAVITY = 9.81f;
	
	public Point3D north, east, south, west;
	
	boolean orthographic;
	
	float left;
	float right;
	float bottom;
	float top;
	float near;
	float far;
	
	public float radius = 0.7f;
	
	private FloatBuffer matrixBuffer;
	
	public Camera() {
		matrixBuffer = BufferUtils.newFloatBuffer(16);
		
		eye = new Point3D();
		u = new Vector3D(1, 0, 0);
		v = new Vector3D(0, 1, 0);
		n = new Vector3D(0, 0, 1);
		
		orthographic = true;
		
		velocity = new Vector3D();
		speed = 1.1f;
		jumpFactor = 3.5f;
		
		this.left = -1;
		this.right = 1;
		this.bottom = -1;
		this.top = 1;
		this.near = -1;
		this.far = 1;
		
		this.east = new Point3D(eye.x+radius, eye.y, eye.z);
		this.south = new Point3D(eye.x, eye.y, eye.z+radius);
		this.west = new Point3D(eye.x-radius, eye.y, eye.z);
		this.north = new Point3D(eye.x, eye.y, eye.z-radius);
	}
	
	public void look(Point3D eye, Point3D center, Vector3D up) {
		this.eye.set(eye.x, eye.y, eye.z);
		this.east.set(eye.x+radius, eye.y, eye.z);
		this.south.set(eye.x, eye.y, eye.z+radius);
		this.west.set(eye.x-radius, eye.y, eye.z);
		this.north.set(eye.x, eye.y, eye.z-radius);
		n = Vector3D.difference(eye,  center);
		u = up.cross(n);
		n.normalize();
		u.normalize();
		v = n.cross(u);
	}
	
	public void applyGravity(float deltaTime) {
		
		if (eye.y > 1.0f || velocity.y > 0.0f) {
			velocity.y -= GRAVITY * deltaTime;
			eye.y += velocity.y * deltaTime * speed;
		}
		else {
			velocity.y = 0.0f;
		}
	}
	
	public void setEye(float x, float y, float z) {
		eye.set(x, y, z);
		this.east.set(eye.x+radius, eye.y, eye.z);
		this.south.set(eye.x, eye.y, eye.z+radius);
		this.west.set(eye.x-radius, eye.y, eye.z);
		this.north.set(eye.x, eye.y, eye.z-radius);
	}
	
	public void slide(float delU, float delV, float delN) {
		eye.translate(delU*u.x + delV*v.x + delN*n.x, delU*u.y + delV*v.y + delN*n.y, delU*u.z + delV*v.z + delN*n.z);
		
		north.translate(delU*u.x + delV*v.x + delN*n.x, delU*u.y + delV*v.y + delN*n.y, delU*u.z + delV*v.z + delN*n.z);
		east.translate(delU*u.x + delV*v.x + delN*n.x, delU*u.y + delV*v.y + delN*n.y, delU*u.z + delV*v.z + delN*n.z);
		south.translate(delU*u.x + delV*v.x + delN*n.x, delU*u.y + delV*v.y + delN*n.y, delU*u.z + delV*v.z + delN*n.z);
		west.translate(delU*u.x + delV*v.x + delN*n.x, delU*u.y + delV*v.y + delN*n.y, delU*u.z + delV*v.z + delN*n.z);
	}
	
	public void roll(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);
		Vector3D t = new Vector3D(u.x, u.y, u.z);
		
		u.set(t.x * c - v.x * s,  t.y * c - v.y * s,  t.z * c - v.z * s);
		v.set(t.x * s + v.x * c,  t.y * s + v.y * c,  t.z * s + v.z * c);
	}
	
	public void yaw(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);
		Vector3D t = new Vector3D(u.x, u.y, u.z);
		
		u.set(t.x * c - n.x * s,  t.y * c - n.y * s,  t.z * c - n.z * s);
		n.set(t.x * s + n.x * c,  t.y * s + n.y * c,  t.z * s + n.z * c);
	}
	
	public void pitch(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);
		Vector3D t = new Vector3D(n.x, n.y, n.z);
		
		n.set(t.x * c - v.x * s,  t.y * c - v.y * s,  t.z * c - v.z * s);
		v.set(t.x * s + v.x * c,  t.y * s + v.y * c,  t.z * s + v.z * c);
	}
	
	public void orthographicProjection(float left, float right, float bottom, float top, float near, float far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
		orthographic = true;
	}
	
	public void perspectiveProjection(float fov, float ratio, float near, float far) {
		this.top = near * (float)Math.tan(((double)fov / 2.0f) * Math.PI / 180.0f);	 // N*tan(fov/2)
		this.bottom = -top;
		this.right = ratio * top;
		this.left = -right;
		this.near = near;
		this.far = far;
		
		orthographic = false;
	}
	
	public FloatBuffer getViewMatrix() {
		float[] pm = new float[16];
		
		Vector3D minusEye = new Vector3D(-eye.x, -eye.y, -eye.z);
		
		pm[0] = u.x; pm[4] = u.y; pm[8] = u.z; pm[12] = minusEye.dot(u);
		pm[1] = v.x; pm[5] = v.y; pm[9] = v.z; pm[13] = minusEye.dot(v);
		pm[2] = n.x; pm[6] = n.y; pm[10] = n.z; pm[14] = minusEye.dot(n);
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;
		
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		
		return matrixBuffer;
	}
	
	public FloatBuffer getProjectionMatrix() {
		float[] pm = new float[16];
		
		if (orthographic) {
			pm[0] = 2.0f / (right - left); pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = -(right + left) / (right - left);
			pm[1] = 0.0f; pm[5] = 2.0f / (top - bottom); pm[9] = 0.0f; pm[13] = -(top + bottom) / (top - bottom);
			pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = 2.0f / (near - far); pm[14] = (near + far) / (near - far);
			pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;
		}
		else {
			pm[0] = (2.0f * near) / (right - left); pm[4] = 0.0f; pm[8] = (right + left) / (right - left); pm[12] = 0.0f;
			pm[1] = 0.0f; pm[5] = (2.0f * near) / (top - bottom); pm[9] = (top + bottom) / (top - bottom); pm[13] = 0.0f;
			pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = -(far + near) / (far - near); pm[14] = -(2.0f * far * near) / (far - near);
			pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = -1.0f; pm[15] = 0.0f;
		}
		
		
		matrixBuffer = BufferUtils.newFloatBuffer(16);
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		
		return matrixBuffer;
	}
	
	public void jump() {
		if (eye.y <= 1.0f) {
			velocity.y += jumpFactor;
		}
	}
 }
