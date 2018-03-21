package Force3DInterface.Force;

import Force3DInterface.ForceVector3D;
import GxEngine3D.Model.ForcePoint3D;
import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.Model.Vector;

public class FloorForce extends HardForce {

    double zPosition = 0;
    ForceVector3D reverseGravity;

    public FloorForce(double z, ForceVector3D g)
    {
        zPosition = z;
        reverseGravity = new ForceVector3D(new Vector(g.getVector().multiply(new Vector(-1, -1, -1)).toArray()), g.getForceValue());
    }

    @Override
    protected void movePoint(ForcePoint3D p1) {
        RefPoint3D point = p1.getPoint();
        if (point.Z() < zPosition)
        {
//            point.setZ(zPosition);
            p1.addForce(reverseGravity);
        }
    }
}
