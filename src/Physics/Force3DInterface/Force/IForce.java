package Physics.Force3DInterface.Force;

import Physics.ForcePoint3D;

public interface IForce {
    //should apply force to both points
    void applyForce(ForcePoint3D p1, ForcePoint3D p2);
    //should apply a single sided force to p1 based on p2
    void applySingleForce(ForcePoint3D p1, double[] p2);
}
