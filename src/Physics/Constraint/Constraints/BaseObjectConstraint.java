package Physics.Constraint.Constraints;

import Shapes.ForceShape;

public abstract class BaseObjectConstraint extends BaseConstraint{

    protected abstract boolean isViolating(ForceShape[] bs);
    protected abstract void applySolutions(ForceShape[] bs);
    @Override
    public boolean isViolating(Object ob) {
        return isViolating((ForceShape[])ob);
    }

    @Override
    public void applySolutions(Object ob) {
        applySolutions((ForceShape[])ob);
    }
}
