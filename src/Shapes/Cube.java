package Shapes;

import java.awt.Color;

import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.View.ViewHandler;

public class Cube extends ForceShape {

	protected static String name = "Cube";
	
	public Cube(double x, double y, double z, double width, double length,
			double height, Color c, ViewHandler v) {

		super(x, y, z, width, length, height, c, v);
	}

	protected void createShape(Color[] c) {
		//add points
		points.add(new RefPoint3D(x, y, z));// front bottom left
		points.add(new RefPoint3D(x + width, y, z));// front bottom right
		points.add(new RefPoint3D(x, y + length, z));// front top left
		points.add(new RefPoint3D(x + width, y + length, z));// front top right
		points.add(new RefPoint3D(x, y, z + height));// back bottom left
		points.add(new RefPoint3D(x + width, y, z + height));// back bottom right
		points.add(new RefPoint3D(x, y + length, z + height));// back top left
		points.add(new RefPoint3D(x + width, y + length, z + height));// back top right

		//edges
		addEdge(new RefPoint3D[]{points.get(2), points.get(3)});//front top		0
		addEdge(new RefPoint3D[]{points.get(3), points.get(1)});//front right	1
		addEdge(new RefPoint3D[]{points.get(0), points.get(1)});//front bottom	2
		addEdge(new RefPoint3D[]{points.get(0), points.get(2)});//front left	3

		addEdge(new RefPoint3D[]{points.get(7), points.get(6)});//back top		4
		addEdge(new RefPoint3D[]{points.get(7), points.get(5)});//back right	5
		addEdge(new RefPoint3D[]{points.get(4), points.get(5)});//back bottom	6
		addEdge(new RefPoint3D[]{points.get(6), points.get(4)});//back left		7

		addEdge(new RefPoint3D[]{points.get(3), points.get(7)});//top right		8
		addEdge(new RefPoint3D[]{points.get(2), points.get(6)});//top left		9
		addEdge(new RefPoint3D[]{points.get(0), points.get(4)});//bottom left	10
		addEdge(new RefPoint3D[]{points.get(5), points.get(1)});//bottom right	11

		//clockwise
		addPoly(new int[] {0, 1, 2, 3}, c[0]);//front
		addPoly(new int[] {4, 7, 6, 5}, c[1]);//back

		addPoly(new int[] {9, 4, 8, 0}, c[2]);//top
		addPoly(new int[] {2, 10, 6, 11}, c[3]);//bottom

		addPoly(new int[] {10, 7, 9, 3}, c[4]);//left
		addPoly(new int[] {11, 1, 8, 5}, c[5]);//right
	}

	@Override
	protected void createShape() {
		createShape(new Color[] { c, c, c, c, c, c });
		super.createShape();
	}
	public static String getName()
	{
		return Cube.name;
	}
	@Override
	public String toString() {
		return getName();
	}

}
