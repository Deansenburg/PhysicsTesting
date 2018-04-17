package Physics.Force3DInterface.Force;

import Physics.Force3DInterface.ForceVector3D;
import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.Model.Vector;

/**
 * Created by Dean on 29/12/16.
 */
public class Spring3D extends SoftForce {

    protected double mSpringLength;

    public Spring3D( int force, double len) {
        super(force);
        mSpringLength = len;
    }

    @Override
    protected ForceVector3D getSoftForce(double[] p1, double[] p2) {
        double distanceBetween = DistanceCalc.getDistanceNoRoot(p1, p2);
        //force of spring is based on current size of spring and default size
        double force = mForce * (getDifferenceFactor(distanceBetween, mSpringLength));
        return getForceVector(distanceBetween, mSpringLength, p2, p1, force);
    }

    protected ForceVector3D getForceVector(double dist, double basedOn, double[] f1, double[] f2, double force) {
        ForceVector3D n1Vector = null;
        Vector dir;
        if (dist >= basedOn)// too far apart
        {
            dir = new Vector(new double[]{f1[0]-f2[0], f1[1]-f2[1], f1[2]-f2[2]});
            n1Vector = new ForceVector3D(dir, (float)force);
        } else if (dist < basedOn) {
            dir = new Vector(new double[]{f2[0]-f1[0], f2[1]-f1[1], f2[2]-f1[2]});
            n1Vector = new ForceVector3D(dir, (float)force);
        }
        if (n1Vector != null) {
            return n1Vector;
        }
        return new ForceVector3D(new Vector(0, 0,0), 0);
    }
}
