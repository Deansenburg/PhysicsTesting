package Shapes;

import Constraint.ConstraintPair;
import Constraint.Constraints.RigidConstraint;
import Constraint.Constraints.VolumeConstraint;
import Constraint.ImpulseSolver;
import Force3DInterface.Force.GenericRepel3D;
import Force3DInterface.ForcePair;
import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.Model.ForcePoint3D;
import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.View.ViewHandler;

import java.awt.*;
import java.util.ArrayList;

public class ForceShape extends BaseShape {

    ArrayList<ForcePoint3D> forcePoints;
    ArrayList<ConstraintPair> constraints;
    ArrayList<ForcePair> singleForces;

    public ForceShape(double x, double y, double z, double width, double length, double height, Color c, ViewHandler v) {
        super(x, y, z, width, length, height, c, v);
    }

    //create shape super must be called last in order to function
    @Override
    protected void createShape() {

        setupForcePoints();
        //after shape has been created, map forces from edges
        setupEdgeConstraints();
        //some shapes have a "weak" structure and need additional help staying in shape
        setupVolumetricConstraints();
    }

    //overridable setups so that each shape can have different structural properties as needed
    protected void setupForcePoints()
    {
        forcePoints = new ArrayList<>();
        for (RefPoint3D p:points)
        {
            ForcePoint3D fp = new ForcePoint3D(p);
            forcePoints.add(fp);
        }
    }
    protected void setupEdgeConstraints()
    {
        if (constraints == null) {
            constraints = new ArrayList<>();
        }
        for (RefPoint3D[] edge:edges)
        {
            ConstraintPair pair = new ConstraintPair(new ForcePoint3D[]{find(edge[0]), find(edge[1])},
                    new RigidConstraint(DistanceCalc.getDistance(edge[0].toArray(), edge[1].toArray()), 0.01));
            constraints.add(pair);
        }
    }
    protected void setupVolumetricConstraints()
    {
        ForcePoint3D[] arr = new ForcePoint3D[forcePoints.size()];
        forcePoints.toArray(arr);
        constraints.add(new ConstraintPair(arr, new VolumeConstraint(polys, 0.001)));
    }

    private ForcePoint3D find(RefPoint3D pointToFind)
    {
        for (ForcePoint3D p:forcePoints)
        {
            if (p.hasSamePoint(pointToFind))
            {
                return p;
            }
        }
        System.out.println("BaseShape-find: did not find force point 3d, this should not happen");
        System.out.println(Thread.currentThread().getStackTrace());
        return null;
    }

    //TODO rename
    public ArrayList<ConstraintPair> getConstraints()
    {
        return (ArrayList<ConstraintPair>) constraints.clone();
    }

    public ArrayList<ForcePair> getSingleForces() {
        return (ArrayList<ForcePair>) singleForces.clone();
    }

    public ArrayList<ForcePoint3D> getForcePoints() {
        return (ArrayList<ForcePoint3D>) forcePoints.clone();
    }

//    public ArrayList<ForcePair> getConnectedTo(ForcePoint3D p)
//    {
//        ArrayList<ForcePair> connected = new ArrayList<>();
//        for (ForcePair fp:constraints)
//        {
//            ForcePoint3D[] f = fp.getEdge();
//            if (f[0].equals(p) || f[1].equals(p))
//            {
//                connected.add(fp);
//            }
//        }
//        return connected;
//    }
}
