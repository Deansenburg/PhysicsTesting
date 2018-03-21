package Programs;

import Force3DInterface.CoordinateCalc3D;
import Force3DInterface.ForceVector3D;
import GxEngine3D.Model.ForcePoint3D;
import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.Model.Vector;

//to get a vector that will move the point to a target location within 1 simulation tick
public class PointManipulation {

    public static void main(String[] a)
    {
        //first simulation frame is not effected by drag
        ForcePoint3D p1 = new ForcePoint3D(new RefPoint3D(0, 0, 0));

        ForceVector3D startingVector = new ForceVector3D(new Vector(1, 0, 1), 1000);

        ForceVector3D gravity = new ForceVector3D(new Vector(0, -1, 0), 10);

        int x = 10000;

        p1.addForce(startingVector);
        for(int i=0;i<10;i++) {
            p1.addAccelerationForce(gravity);

            ForceVector3D v1 = p1.predictVectorRequired(new double[]{x, 1, 1});
//            System.out.println(v1);

            p1.addForce(v1);
            p1.updateCoordinates(new CoordinateCalc3D());
            System.out.println(i+" " + p1.getPoint());
        }
    }
}
