package Physics.Force3DInterface.Force;

import Physics.ForcePoint3D;

public abstract class HardForce implements IForce {

    //is expected to move the point in some direction, or to a specific coordinate
    protected abstract void movePoint(ForcePoint3D p1);

    @Override
    public void applyForce(ForcePoint3D p1, ForcePoint3D p2) {
        movePoint(p1);
        movePoint(p2);
    }

    @Override
    public void applySingleForce(ForcePoint3D p1, double[] p2) {
        movePoint(p1);
    }
}
