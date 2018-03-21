package Force3DInterface.Force;

import Force3DInterface.ForceVector3D;
import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.Model.Vector;

public class GenericRepel3D extends SoftForce {

    double radius;
    public GenericRepel3D(int force, double rad) {
        super(force);
        this.radius = rad;
    }

    @Override
    protected ForceVector3D getSoftForce(double[] p1, double[] p2) {
        double distanceBetween = DistanceCalc.getDistanceNoRoot(p1, p2);

        if (distanceBetween > radius && radius != -1)
            return new ForceVector3D(new Vector(0, 0, 0), 0);

        return new ForceVector3D(new Vector(new double[]{p1[0]-p2[0], p1[1]-p2[1], p1[2]-p2[2]}), (float)(mForce * (getDifferenceFactor(distanceBetween, radius))));
    }
}