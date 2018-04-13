package Constraint.Constraints;

import GxEngine3D.Model.ForcePoint3D;

public abstract class BasePointConstraint extends BaseConstraint {
    protected abstract boolean isViolating(ForcePoint3D[] points);
    protected abstract void applySolutions(ForcePoint3D[] points);

    @Override
    public boolean isViolating(Object ob) {
        return isViolating((ForcePoint3D[])ob);
    }

    @Override
    public void applySolutions(Object ob) {
        ForcePoint3D[] arr = (ForcePoint3D[])ob;
        applySolutions(arr);
        updatePoints(arr);
    }
}
