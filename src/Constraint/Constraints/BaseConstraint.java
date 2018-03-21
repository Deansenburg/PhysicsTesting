package Constraint.Constraints;

public abstract class BaseConstraint implements IConstraint {
    protected double getForceModifier(double dist)
    {
        return 1d - (1d/dist);
    }
}
