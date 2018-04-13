package Programs;

import GxEngine3D.CalculationHelper.VectorCalc;
import GxEngine3D.Model.Plane;
import GxEngine3D.Model.Polygon3D;
import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.Model.Vector;
import Shapes.BaseShape;
import Shapes.Cube;

import java.awt.*;
import java.util.ArrayList;

public class ObjectCollision01 {
    public int whichSide(ArrayList<RefPoint3D> testPoints, double[] planeNormal, double[] planePoint)
    {
        int pos = 0, neg = 0;
        for (RefPoint3D testPoint:testPoints) {
            double[] v = VectorCalc.sub_v3v3(planePoint, testPoint.toArray());
            double dot = VectorCalc.dot_v3v3(v, planeNormal);
            if (dot > 0)
            {
                pos++;
            }
            else if(dot < 0)
            {
                neg++;
            }
            if (pos > 0 && neg > 0)
            {
                return 0;
            }
        }
        if (pos > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public boolean intersects(BaseShape c0, BaseShape c1)
    {
        for (Polygon3D face:c0.getShape())
        {
            RefPoint3D[] fPoints = face.getShape();
            //our shapes are ordered clockwise, this method needs anti-clock, so we flip normal vectors for this calculation
            double[] normal = VectorCalc.cross(
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[2].toArray(), fPoints[0].toArray())),
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[1].toArray(), fPoints[0].toArray())));
            if (whichSide(c1.getPoints(), normal, fPoints[0].toArray()) > 0)
            {
                System.out.println("Test01");
                return false;
            }
        }

        for (Polygon3D face:c1.getShape())
        {
            RefPoint3D[] fPoints = face.getShape();
            double[] normal = VectorCalc.cross(
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[2].toArray(), fPoints[0].toArray())),
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[1].toArray(), fPoints[0].toArray())));
            if (whichSide(c0.getPoints(), normal, fPoints[0].toArray()) > 0)
            {
                System.out.println("Test02");
                return false;
            }
        }

        for (RefPoint3D[] e0:c0.getEdges())
        {
            double[] e0V = VectorCalc.sub_v3v3(e0[0].toArray(), e0[1].toArray());
            for (RefPoint3D[] e1:c1.getEdges())
            {
                double[] e1V = VectorCalc.sub_v3v3(e1[0].toArray(), e1[1].toArray());
                double[] cross = VectorCalc.cross(e0V, e1V);
                int side0 = whichSide(c0.getPoints(), cross, e0[0].toArray());
                if (side0 == 0)
                {
                    continue;
                }
                int side1 = whichSide(c1.getPoints(), cross, e0[0].toArray());
                if (side1 == 0)
                {
                    continue;
                }
                if (side0 * side1 < 0)
                {
                    System.out.println("Test03");
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] arg)
    {
        Cube c0 = new Cube(1, 1, 0, 5, 5, 5, Color.decode("#FF00FF"), null);

        Cube c1 = new Cube(1.5, 1.5, 4.9, 4, 4, 4, Color.decode("#0000FF"), null);

        ObjectCollision01 prog = new ObjectCollision01();
        if (prog.intersects(c0, c1))
        {
            System.out.println("Intersects");
        }
        else
        {
            System.out.println("Doesnt Intersect");
        }
    }
}
