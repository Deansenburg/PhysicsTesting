package Physics.Constraint;

import Physics.Constraint.Constraints.IConstraint;

public class ConstraintPair<T> {
    T affected;//what is affected by this constraint
    IConstraint constraint;

    public ConstraintPair(T af, IConstraint con) {
        affected = af;
        constraint = con;
    }

    public IConstraint getConstraint() {
        return constraint;
    }

    public T getPoints() {
        return affected;
    }
}
