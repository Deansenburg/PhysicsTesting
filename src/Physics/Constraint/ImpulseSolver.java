package Physics.Constraint;

import Physics.Constraint.Constraints.IConstraint;
import Physics.Force3DInterface.CoordinateCalc3D;

import java.util.ArrayList;

public class ImpulseSolver {
    ArrayList<ConstraintPair> constraints = new ArrayList<>();
    int maxIter = 1;

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
                if (constraint.isViolating(c.getPoints()))
                {
                    isViolating = true;
                    constraint.applySolutions(c.getPoints());
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
