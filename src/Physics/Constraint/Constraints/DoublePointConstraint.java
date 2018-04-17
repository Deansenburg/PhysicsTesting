package Physics.Constraint.Constraints;

import Physics.ForcePoint3D;

public abstract class DoublePointConstraint extends BasePointConstraint {

    protected abstract boolean isViolating(ForcePoint3D p1, ForcePoint3D p2);
    protected abstract void applySolutions(ForcePoint3D p1, ForcePoint3D p2);

    @Override
    public  boolean isViolating(ForcePoint3D[] points) {
        return isViolating(points[0], points[1]);
    }

    @Override
    public void applySolutions(ForcePoint3D[] points) {
        applySolutions(points[0], points[1]);
    }
}
