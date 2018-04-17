package Physics.Constraint.Constraints;

import Physics.Force3DInterface.ForceVector3D;
import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.CalculationHelper.VectorCalc;
import Physics.ForcePoint3D;
import GxEngine3D.Model.Plane;
import GxEngine3D.Model.Polygon3D;
import GxEngine3D.Model.Projection;

import java.util.ArrayList;

public class PointPenetrationConstraint extends BasePointConstraint {
    ArrayList<Polygon3D> polys;

    ArrayList<PenetrationPair> prevSolution;

    protected class PenetrationPair
    {
        ForcePoint3D point;
        double[] projPoint;
        Polygon3D closestPoly;
        public PenetrationPair(ForcePoint3D point3D, double[] proj, Polygon3D poly)
        {
            point = point3D;
            projPoint = proj;
            closestPoly = poly;
        }
    }

    public PointPenetrationConstraint(ArrayList<Polygon3D> p)
    {
        polys = p;
    }

    @Override
    public boolean isViolating(ForcePoint3D[] points) {
        prevSolution = new ArrayList<>();
        boolean hasViolation = false;
        for (ForcePoint3D p:points)
        {
            double smallestDist = Double.MAX_VALUE;
            boolean inside = true;

            ArrayList<PenetrationPair> pairs = new ArrayList<>();

            double[] p_d = p.getPoint().toArray();
            for (Polygon3D poly:polys)
            {
                double[] planePoint = poly.getShape()[0].toArray();
                double[] planeNorm = new Plane(poly).getNV().toArray();
                double[] v = VectorCalc.sub_v3v3(planePoint, p_d);
                double dot = VectorCalc.dot_v3v3(v, planeNorm);
                if (dot <= 0)
                {
                    //point is outside/on the plane
                    inside = false;
                    break;
                }
                else
                {
                    //point must be on inside face of the poly
                    //this should give us the intersection from the point to the plane going in the direction of the plane normal
                    //ie the point closest to the test point but still on the plane
                    Projection proj = VectorCalc.isect_vec_plane(p_d, planeNorm, planePoint, planeNorm);
                    double dist = DistanceCalc.getDistanceNoRoot(p_d, proj.Point());
                    if (dist < smallestDist)
                    {
                        smallestDist = dist;
                        pairs.add(new PenetrationPair(p, proj.Point(), poly));
                    }
                }
            }
            if (inside)
            {
                //if the point is inside all polygons, ie inside the shape as a whole
                prevSolution.addAll(pairs);
                hasViolation = true;
            }
        }
        return hasViolation;
    }

    @Override
    public void applySolutions(ForcePoint3D[] points) {
        //the points given will be all other points in the force system
        //but our solution really only needs the points which were inside the shapes

        //two things need to happen
        //1 - the point needs to move in the same direction as the polys normal, that would be the direction away from the shape
        for (PenetrationPair pair:prevSolution) {
            ForceVector3D pointVec = pair.point.predictVectorRequired(pair.projPoint);
            pair.point.addForce(pointVec);
        }


        //2 - the polygon needs to move away from the point in reverse direction from the plane normal
    }

    @Override
    public void reapplyPreviousSolution() {
        for (PenetrationPair pair:prevSolution) {
            ForceVector3D pointVec = pair.point.predictVectorRequired(pair.projPoint);
            pair.point.addForce(pointVec);
        }
    }
}
