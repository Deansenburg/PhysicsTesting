package GxEngine3D.Controller;

import Constraint.ConstraintPair;
import Constraint.Constraints.FloorConstraint;
import Constraint.ImpulseSolver;
import Force3DInterface.CoordinateCalc3D;
import Force3DInterface.ForceVector3D;
import GxEngine3D.Model.ForcePoint3D;
import GxEngine3D.Model.Vector;
import Shapes.ForceShape;

import java.util.ArrayList;

public class ForceController {

    ArrayList<ForceShape> shapes = new ArrayList<>();
    CoordinateCalc3D calc = new CoordinateCalc3D();

    ImpulseSolver solver;

    public void add(ForceShape fs)
    {
        shapes.add(fs);
    }

    public void update()
    {
        //add forces
        for (ForceShape fs:shapes)
        {
            ForceVector3D gravity = new ForceVector3D(new Vector(0,0, -1), 9.8f);
            for (ForcePoint3D fp:fs.getForcePoints())
            {
                fp.addAccelerationForce(gravity);
            }
        }
        //evaluate forces
        for (ForceShape fs:shapes) {
            for (ForcePoint3D fp:fs.getForcePoints())
            {
                fp.updateCoordinates(calc);
            }
        }
        //constraint solver
        solver = new ImpulseSolver();
        FloorConstraint floor = new FloorConstraint(0);
        for (ForceShape fs:shapes)
        {
            //structural constraints
            for (ConstraintPair pair:fs.getConstraints())
            {
                solver.add(pair);
            }
            //floor constraint
            for (ForcePoint3D p:fs.getForcePoints())
            {
                solver.add(new ConstraintPair(new ForcePoint3D[]{p}, floor));
            }
        }
        solver.solve();
//        for (ForceShape fs:shapes)
//        {
//            for (ForcePoint3D p:fs.getForcePoints())
//            {
//                p.purgeForces(1);
//            }
//        }
    }
}
