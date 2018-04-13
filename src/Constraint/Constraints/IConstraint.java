package Constraint.Constraints;

import Constraint.ConstraintPair;
import Force3DInterface.ForceVector3D;
import GxEngine3D.Model.ForcePoint3D;

public interface IConstraint {
    boolean isViolating(Object ob);
    //apply solution must update points now
    void applySolutions(Object ob);
    void reapplyPreviousSolution();
}
