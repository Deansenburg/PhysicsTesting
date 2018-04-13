package Programs;

import GxEngine3D.CalculationHelper.VectorCalc;
import GxEngine3D.Model.Plane;
import GxEngine3D.Model.Polygon3D;
import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.Model.Vector;
import Shapes.Cube;

public class PlaneSidedTest {
    public static void main(String[] arg)
    {
        //just to get the polygon3D to function, not actually needed
        Cube c = new Cube(0, 0, 0, 1, 1, 1, null, null);

        //clockwise orientation of the points
        Polygon3D poly = new Polygon3D(
                new RefPoint3D[]{
                        new RefPoint3D(0, 0, 0),
                        new RefPoint3D(0, 1, 0),
                        new RefPoint3D(1, 1, 0),
                        new RefPoint3D(1, 0, 0)},
                null, null, c);
        Plane plane = new Plane(poly);

        //positive z will be inside the object
        double[] testPoint = new double[]{0, 0, 1};

        double[] v = VectorCalc.sub_v3v3(poly.getShape()[0].toArray(), testPoint);

        //the normal vector's direction is reliant on the order of the points
        //assuming clockwise ordering of points, then the normal vector will point away from the object
        Vector planeNorm = plane.getNV();
        System.out.println(planeNorm);

        double dot = VectorCalc.dot_v3v3(v, planeNorm.toArray());
        if (dot > 0)
        {
            System.out.println("Point is inside object");
        }
        else if (dot < 0)
        {
            System.out.println("Point is outside object");
        }
        else
        {
            System.out.println("Point is on the plane");
        }

        //to check if a point is inside a complex object of polygons, we can check if the point is on the inside side of all polygons as planes
        //if the point is on the outside side of the polygon plane then it is not inside the polygon

        //some examples of how to use this
        //      a single plane can be used to model a floor or ceiling of infinite size
        //      two planes can be used to thick wall or floor of infinite size
        //      complex arrays of planes can model shapes for collision checking purposes
        //          on this, you would also need to be keeping track of the closest plane to the point so that we have a shortest route for collision solutions to take
    }
}
