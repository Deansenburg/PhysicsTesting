package Physics.Force3DInterface;

import GxEngine3D.Model.Vector;

/**
 * Created by Dean on 29/12/16.
 */
public class ForceVector3D extends ForceVector {
    Vector vDirection;
    double mZComp;

    int flag = 0;
    public ForceVector3D(Vector direction, double force) {
        super(0, force);
        vDirection = direction;
        mXComp = (float)direction.X();
        mYComp = (float)direction.Y();
        mZComp = (float)direction.Z();
    }

    public ForceVector3D(Vector direction, double force, int flag) {
        super(0, force);
        vDirection = direction;
        mXComp = (float)direction.X();
        mYComp = (float)direction.Y();
        mZComp = (float)direction.Z();
        this.flag = flag;
    }
    public double getZComp()
    {
        return mZComp;
    }
    public Vector getVector() {
        return vDirection;
    }
    public int getFlag()
    {
        return flag;
    }

    @Override
    public String toString() {
        return vDirection.toString() + " " + mForceAmount;
    }
}
