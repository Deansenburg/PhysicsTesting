package Constraint.Constraints;

import Force3DInterface.ForceVector3D;
import GxEngine3D.Model.ForcePoint3D;

public interface IConstraint {
    boolean isViolating(ForcePoint3D[] points);
    void applySolutions(ForcePoint3D[] points);
    void reapplyPreviousSolution();
}
