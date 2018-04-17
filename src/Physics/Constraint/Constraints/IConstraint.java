package Physics.Constraint.Constraints;

public interface IConstraint {
    boolean isViolating(Object ob);
    //apply solution must update points now
    void applySolutions(Object ob);
    void reapplyPreviousSolution();
}
