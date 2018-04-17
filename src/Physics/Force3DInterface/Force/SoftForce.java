package Physics.Force3DInterface.Force;

import Physics.Force3DInterface.ForceVector3D;
import Physics.ForcePoint3D;

public abstract class SoftForce implements IForce {
    protected int mForce = 100;

    public SoftForce(int force)
    {
        mForce = force;
    }

    //is expected to return a vector with appropriate force in relation to p1
    protected abstract ForceVector3D getSoftForce(double[] p1, double[] p2);

    @Override
    public void applyForce(ForcePoint3D p1, ForcePoint3D p2) {
        p1.addForce(getSoftForce(p1.getPoint().toArray(), p2.getPoint().toArray()));
        p2.addForce(getSoftForce(p2.getPoint().toArray(), p1.getPoint().toArray()));
    }

    @Override
    public void applySingleForce(ForcePoint3D p1, double[] p2) {
        p1.addForce(getSoftForce(p1.getPoint().toArray(), p2));
    }

    protected double getDifferenceFactor(double dist, double basedOn)
    {
        double differenceFactor;
        if (dist < basedOn) {
            // for shorter
            differenceFactor = 1 - Math.pow(dist / basedOn, 4);
            if (Double.isInfinite(differenceFactor))
                differenceFactor = mForce;
        } else {
            // for longer
            differenceFactor = Math.pow(dist / basedOn, 4);
        }
        return differenceFactor;
    }
}
