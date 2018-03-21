package Force3DInterface;

import Force3DInterface.Force.IForce;
import GxEngine3D.Model.ForcePoint3D;
import GxEngine3D.Model.RefPoint3D;

import java.io.InvalidObjectException;

public class ForcePair {

    IForce force;
    ForcePoint3D[] edge;

    public ForcePair(ForcePoint3D p1, ForcePoint3D p2, IForce f) {
        edge = new ForcePoint3D[2];
        edge[0] = p1;
        edge[1] = p2;
        force = f;
    }

    public IForce getForce() {
        return force;
    }

    public ForcePoint3D[] getEdge() {
        return edge;
    }
}
