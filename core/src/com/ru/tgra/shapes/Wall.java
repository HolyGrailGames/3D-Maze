package com.ru.tgra.shapes;

import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Vector3D;

public class Wall
{
	private Point3D position;
	private Vector3D scale;
	
	public Wall(Point3D position, Vector3D scale) {
		this.position = position;
		this.scale = scale;
	}
	
	public void draw() {
		Maze3D.shader.setMaterialDiffuse(0.7f, 0.4f, 0.0f, 1.0f);
		Maze3D.shader.setMaterialSpecular(1.0f, 0.95f, 0.9f, 1.0f);
		
		//
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addScale(scale.x, scale.y, scale.z);
		Maze3D.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
}
