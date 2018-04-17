package Physics.Constraint.Constraints;

import Physics.ForcePoint3D;

public abstract class SinglePointConstraint extends BasePointConstraint {
    protected abstract boolean isViolating(ForcePoint3D p);
    protected abstract void applySolution(ForcePoint3D p);

    @Override
    public boolean isViolating(ForcePoint3D[] points) {
        return isViolating(points[0]);
    }

    @Override
    public void applySolutions(ForcePoint3D[] points) {
        applySolution(points[0]);
    }
}
