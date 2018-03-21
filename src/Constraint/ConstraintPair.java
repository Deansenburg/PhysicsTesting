package Constraint;

import Constraint.Constraints.IConstraint;
import GxEngine3D.Model.ForcePoint3D;

public class ConstraintPair {
    ForcePoint3D[] points;
    IConstraint constraint;

    public ConstraintPair(ForcePoint3D[] p, IConstraint con) {
        points = p;
        constraint = con;
    }

    public IConstraint getConstraint() {
        return constraint;
    }

    public ForcePoint3D[] getPoints() {
        return points;
    }
}
