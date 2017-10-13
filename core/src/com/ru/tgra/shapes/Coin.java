package com.ru.tgra.shapes;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Vector3D;

public class Coin {
	
	float angle = 0;
	float spinSpeed = 180.0f;
	public Point3D position;
	Vector3D scale;
	Random rand = new Random();
	
	public Coin(Point3D position) {
		this.position = position;
		this.scale = new Vector3D(0.3f,0.3f,0.01f);
		this.spinSpeed *= rand.nextFloat();
		
	}

	public void update(float deltaTime) {
		angle += spinSpeed * deltaTime;
	}
	
	public void draw(Color diffuse, Color specular, Color emission) {
		Maze3D.shader.setMaterialDiffuse(diffuse.r, diffuse.g, diffuse.b, diffuse.a);
		Maze3D.shader.setMaterialSpecular(specular.r, specular.g, specular.b, specular.a);
		Maze3D.shader.setMaterialEmission(emission.r, emission.g, emission.b, emission.a);
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addRotationY(angle);
		ModelMatrix.main.addScale(scale.x, scale.y, scale.z);
		Maze3D.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
}
