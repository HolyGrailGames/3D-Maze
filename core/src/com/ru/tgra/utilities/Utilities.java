package com.ru.tgra.utilities;

import com.ru.tgra.graphics.Point3D;

public class Utilities
{
	public static float euclidianDistance(Point3D p2, Point3D p1) {
		return (float)Math.sqrt(((p2.x-p1.x)*(p2.x-p1.x)) + ((p2.z-p1.z)*(p2.z-p1.z)));
	}
}
