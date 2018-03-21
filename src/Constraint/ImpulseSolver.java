package Constraint;

import Constraint.Constraints.IConstraint;
import Force3DInterface.CoordinateCalc3D;
import GxEngine3D.Model.ForcePoint3D;

import java.util.ArrayList;

public class ImpulseSolver {
    ArrayList<ConstraintPair> constraints = new ArrayList<>();
    int maxIter = 10;

    CoordinateCalc3D calc = new CoordinateCalc3D();

    public void add(ConstraintPair p)
    {
        constraints.add(p);
    }

    public void solve()
    {
        int curIter = 0;
        boolean isViolating;
        do {
            isViolating  = false;
            for (ConstraintPair c:constraints) {
                IConstraint constraint = c.getConstraint();
                ForcePoint3D[] points = c.getPoints();
                if (constraint.isViolating(points))
                {
                    isViolating = true;
                    constraint.applySolutions(points);
                    //update coords, this may need to happen twice to stop momentum after reaching location
                    for (ForcePoint3D p:c.getPoints())
                    {
                        p.updateCoordinates(calc);
                    }
                    constraint.reapplyPreviousSolution();
                }
            }
            if (curIter > maxIter)
            {
                break;
            }
            curIter++;
        } while(isViolating);
    }
}
