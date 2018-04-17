package Physics.Constraint.Constraints;

import Physics.Force3DInterface.ForceVector3D;
import Physics.ForcePoint3D;
import GxEngine3D.Model.RefPoint3D;

public class FloorConstraint extends SinglePointConstraint{

    double zPosition;
    double[] previousTarget;
    ForcePoint3D previousNode;

    public FloorConstraint(double pos)
    {
        zPosition = pos;
    }

    @Override
    protected boolean isViolating(ForcePoint3D p) {
        if(p.getPoint().Z() < zPosition)
        {
//            System.out.println("Violation");
            return true;
        }
        return false;
    }

    @Override
    protected void applySolution(ForcePoint3D p) {

        RefPoint3D temp = p.getPoint();

        previousTarget = new double[]{temp.X(), temp.Y(), zPosition};

        ForceVector3D vec = p.predictVectorRequired(previousTarget);
        p.addForce(vec);

        previousNode = p;
    }

    @Override
    public void reapplyPreviousSolution() {
        previousNode.addForce(previousNode.predictVectorRequired(previousTarget));
    }
}
