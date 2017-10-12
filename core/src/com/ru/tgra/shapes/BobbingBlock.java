package com.ru.tgra.shapes;

import com.badlogic.gdx.graphics.Color;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Vector3D;

public class BobbingBlock
{
	private Point3D position;
	private Vector3D scale;
	
	private float bobFactor;
	private float bobSpeed = 2.5f;
	
	
	public BobbingBlock(Point3D position, Vector3D scale) {
		this.position = position;
		this.scale = scale;
		bobFactor = 0.0f;
	}
	
	public void update(float deltaTime) {
        bobFactor += deltaTime * bobSpeed;
        position.y += Math.sin(bobFactor) * bobSpeed * deltaTime;
	}
	
	public void draw(Color diffuse, Color specular) {
		Maze3D.shader.setMaterialDiffuse(diffuse.r, diffuse.g, diffuse.b, diffuse.a);
		Maze3D.shader.setMaterialSpecular(specular.r, specular.g, specular.b, specular.a);
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addScale(scale.x, scale.y, scale.z);
		Maze3D.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
}
