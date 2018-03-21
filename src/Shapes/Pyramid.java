package Shapes;

import java.awt.Color;

import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.View.ViewHandler;

public class Pyramid extends ForceShape {
	
	protected static String name = "Pyramid";
	
	public Pyramid(double x, double y, double z, double width, double length,
			double height, Color c, ViewHandler v) {
		super(x, y, z, width, length, height, c, v);
	}

	@Override
	protected void createShape() {
		points.add(new RefPoint3D(x, y, z));//front left
		points.add(new RefPoint3D(x + width, y, z));//front right
		points.add(new RefPoint3D(x, y + height, z));//back left
		points.add(new RefPoint3D(x + width, y + height, z));//back right

		points.add(new RefPoint3D(x + (width / 2), y + (height / 2), z + length));//top


		//base
		addEdge(new RefPoint3D[]{points.get(0), points.get(1)});//front 0
		addEdge(new RefPoint3D[]{points.get(1), points.get(3)});//right 1
		addEdge(new RefPoint3D[]{points.get(2), points.get(3)});//back 2
		addEdge(new RefPoint3D[]{points.get(2), points.get(0)});//left 3

		addEdge(new RefPoint3D[]{points.get(0), points.get(4)});//front left 4
		addEdge(new RefPoint3D[]{points.get(1), points.get(4)});//front right 5
		addEdge(new RefPoint3D[]{points.get(2), points.get(4)});//back left 6
		addEdge(new RefPoint3D[]{points.get(3), points.get(4)});//back right 7

		//polygons
		addPoly(new int[]{0, 4, 5}, this.c);//front
		addPoly(new int[]{3, 6, 4}, this.c);//left
		addPoly(new int[]{6, 2, 7}, this.c);//back
		addPoly(new int[]{1, 5, 6}, this.c);//right
		addPoly(new int[]{2, 1, 0, 3}, this.c);//base

		super.createShape();
	}

	@Override
	protected void setupVolumetricConstraints() {
		//a pyramid is structurally strong and doesnt need this
	}

	@Override
	public String toString() {
		return getName();
	}
	public static String getName()
	{
		return Pyramid.name;
	}
}
