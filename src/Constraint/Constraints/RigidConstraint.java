package Constraint.Constraints;

import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.CalculationHelper.VectorCalc;
import GxEngine3D.Model.ForcePoint3D;

public class RigidConstraint extends  DoublePointConstraint{

    double lMin, lMax, tolerance;
    double[] prevP1, prevP2;
    ForcePoint3D prevNode1, prevNode2;

    public RigidConstraint(double len, double tol)
    {
        if (tol > 0 && tol < 1)
        {
            tolerance = tol;
            lMin = (1-tol)*len;
            lMax = (1+tol)*len;
        }
        else
        {
            System.out.println("Tolerance must be between 0-1, given: "+tol);
        }
    }

    @Override
    protected boolean isViolating(ForcePoint3D p1, ForcePoint3D p2) {
        double distance = DistanceCalc.getDistance(p1.getPoint().toArray(), p2.getPoint().toArray());
        return distance < lMin || distance > lMax;
    }

    @Override
    protected void applySolutions(ForcePoint3D p1, ForcePoint3D p2) {
        double[] dp1 = p1.getPoint().toArray();
        double[] dp2 = p2.getPoint().toArray();

        double currentDist = DistanceCalc.getDistance(dp1, dp2);

        double[] v1 = VectorCalc.sub_v3v3(dp1, dp2);
        double[] v2 = VectorCalc.sub_v3v3(dp2, dp1);
        //normalise
        v1 = VectorCalc.div_v3_fl(v1, VectorCalc.len_v3(v1));
        v2 = VectorCalc.div_v3_fl(v2, VectorCalc.len_v3(v2));

        double middle = (lMax+lMin)/2;
        if (currentDist > lMax)
        {
//            the edge is too long and must be shrank
            double distToMove = (currentDist - middle) / 2;//because we're going to move in two directions the distance is halved
            v1 = VectorCalc.mul_v3_fl(v1, distToMove);
            v2 = VectorCalc.mul_v3_fl(v2, distToMove);

            prevP1 = VectorCalc.add_v3v3(v2, dp1);
            prevP2 = VectorCalc.add_v3v3(v1, dp2);
        }
        else// if (currentDist < lMin)
        {
            //the edge is too long and must be extended
            double distToMove = (middle - currentDist) / 2;
            v1 = VectorCalc.mul_v3_fl(v1, distToMove);
            v2 = VectorCalc.mul_v3_fl(v2, distToMove);

            prevP1 = VectorCalc.add_v3v3(v1, dp1);
            prevP2 = VectorCalc.add_v3v3(v2, dp2);
        }
        p1.addForce(p1.predictVectorRequired(prevP1));
        p2.addForce(p2.predictVectorRequired(prevP2));

        prevNode1 = p1;
        prevNode2 = p2;
    }

    @Override
    public void reapplyPreviousSolution() {
        prevNode1.addForce(prevNode1.predictVectorRequired(prevP1));
        prevNode2.addForce(prevNode2.predictVectorRequired(prevP2));
    }
}
