package Force3DInterface.Force;

import GxEngine3D.Model.ForcePoint3D;
import GxEngine3D.Model.RefPoint3D;

public interface IForce {
    //should apply force to both points
    void applyForce(ForcePoint3D p1, ForcePoint3D p2);
    //should apply a single sided force to p1 based on p2
    void applySingleForce(ForcePoint3D p1, double[] p2);
}
