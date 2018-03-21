package Constraint.Constraints;

import Force3DInterface.ForceVector3D;
import GxEngine3D.Model.ForcePoint3D;

//this class heavily assumes that there will be two points in the parameter arrays
public abstract class DoublePointConstraint extends BaseConstraint {

    protected abstract boolean isViolating(ForcePoint3D p1, ForcePoint3D p2);
    protected abstract void applySolutions(ForcePoint3D p1, ForcePoint3D p2);

    @Override
    public boolean isViolating(ForcePoint3D[] points) {
        return isViolating(points[0], points[1]);
    }

    @Override
    public void applySolutions(ForcePoint3D[] points) {
        applySolutions(points[0], points[1]);
    }
}
