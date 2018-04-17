package Physics.Constraint;

import Physics.Force3DInterface.CoordinateCalc3D;
import Physics.ForcePoint3D;

import java.util.ArrayList;

public class ImpulseSolverLegacy {
    ArrayList<ConstraintPair> constraints = new ArrayList<>();
    float force = 3;
    int maxIter = 10;

    CoordinateCalc3D calc = new CoordinateCalc3D();

    public void add(ConstraintPair p)
    {
        constraints.add(p);
    }

    //this may not be needed / getting in the way now
    public static double[][] convert(ForcePoint3D[] arr)
    {
        double[][] newArr = new double[arr.length][];
        for (int i=0;i<arr.length;i++)
        {
            newArr[i] = arr[i].getPoint().toArray();
        }
        return newArr;
    }

    public void solve()
    {
//        int curIter = 0;
//        boolean isViolating;
//        do {
//            isViolating  = false;
//            for (ConstraintPair c:constraints) {
//                IConstraint constraint = c.getConstraint();
//                double[][] points = convert(c.getPoints());
//                if (constraint.isViolating(points))
//                {
//                    isViolating = true;
//                    ForceVector3D[][] solutions = constraint.getSolutions(points);
//                    for (int i=0;i<solutions.length;i++)
//                    {
//                        ForcePoint3D point3d = c.getPoints()[i];
//                        for (ForceVector3D v:solutions[i]) {
//                            point3d.addForce(new ForceVector3D(v.getVector(), force, 1));
//                        }
//                    }
//                    for (ForcePoint3D p:c.getPoints())
//                    {
//                        p.updateCoordinates(calc);
//                    }
//                }
//            }
//            if (curIter > maxIter)
//            {
//                break;
//            }
//            curIter++;
//        } while(isViolating);
    }
}
