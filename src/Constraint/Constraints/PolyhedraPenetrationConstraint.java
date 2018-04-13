package Constraint.Constraints;

import Force3DInterface.ForceVector3D;
import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.CalculationHelper.VectorCalc;
import GxEngine3D.Model.*;
import Shapes.BaseShape;
import Shapes.ForceShape;

import java.util.ArrayList;

public class PolyhedraPenetrationConstraint extends BaseObjectConstraint {

    ForceShape thisShape;
    ForceShape collided;

    ArrayList<Solution> prevSolutions;

    double fallBackDist = 0.25;

    private class Solution
    {
        ForcePoint3D point;
        double[] target;
        public Solution(ForcePoint3D p, double[] t)
        {
            point = p;
            target = t;
        }
    }

    public PolyhedraPenetrationConstraint(ForceShape s)
    {
        thisShape = s;
    }

    protected int whichSide(ArrayList<RefPoint3D> testPoints, double[] planeNormal, double[] planePoint)
    {
        int pos = 0, neg = 0;
        for (RefPoint3D testPoint:testPoints) {
            double[] v = VectorCalc.sub_v3v3(planePoint, testPoint.toArray());
            double dot = VectorCalc.dot_v3v3(v, planeNormal);
            if (dot > 0)
            {
                pos++;
            }
            else if(dot < 0)
            {
                neg++;
            }
            if (pos > 0 && neg > 0)
            {
                return 0;
            }
        }
        if (pos > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    protected boolean intersects(BaseShape c0, BaseShape c1)
    {
        for (Polygon3D face:c0.getShape())
        {
            RefPoint3D[] fPoints = face.getShape();
            //our shapes are ordered clockwise, this method needs anti-clock, so we flip normal vectors for this calculation
            double[] normal = VectorCalc.cross(
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[2].toArray(), fPoints[0].toArray())),
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[1].toArray(), fPoints[0].toArray())));
            if (whichSide(c1.getPoints(), normal, face.getShape()[0].toArray()) > 0)
            {
                return false;
            }
        }

        for (Polygon3D face:c1.getShape())
        {
            RefPoint3D[] fPoints = face.getShape();
            double[] normal = VectorCalc.cross(
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[2].toArray(), fPoints[0].toArray())),
                    VectorCalc.norm_v3(VectorCalc.sub_v3v3(fPoints[1].toArray(), fPoints[0].toArray())));
            if (whichSide(c0.getPoints(), normal, face.getShape()[0].toArray()) > 0)
            {
                return false;
            }
        }

        for (RefPoint3D[] e0:c0.getEdges())
        {
            double[] e0V = VectorCalc.sub_v3v3(e0[0].toArray(), e0[1].toArray());
            for (RefPoint3D[] e1:c1.getEdges())
            {
                double[] e1V = VectorCalc.sub_v3v3(e1[0].toArray(), e1[1].toArray());
                double[] cross = VectorCalc.cross(e0V, e1V);
                int side0 = whichSide(c0.getPoints(), cross, e0[0].toArray());
                if (side0 == 0)
                {
                    continue;
                }
                int side1 = whichSide(c1.getPoints(), cross, e0[0].toArray());
                if (side1 == 0)
                {
                    continue;
                }
                if (side0 * side1 < 0)
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected boolean isViolating(ForceShape[] bs) {
        for (ForceShape s:bs)
        {
            if (intersects(s, thisShape))
            {
                //probably want to cache which shapes are colliding
                collided = s;
                //probably want to check if any others are colliding as well
                return true;
            }
        }
        return false;
    }

    //not exactly the solution i want, this solution is meant to be non point based but finding closest exit is harder
    //also the bouncy nature of the simulation suggests that this is not entirely accurate, not to mention the large bounces
    /*/
    protected void applySolutions(ForceShape[] bs) {
        prevSolutions = new ArrayList<>();

        double[] thisCentroid = thisShape.findCentre();
        double[] collCentroid = collided.findCentre();

        double[] vec1 = VectorCalc.sub_v3v3(collCentroid, thisCentroid);

        //find closest exit to centroid
        double smallestDist = Double.MAX_VALUE;
        Polygon3D closest = null;
        for (Polygon3D poly:thisShape.getShape())
        {
            double[] planeNorm = new Plane(poly).getNV().toArray();
            double[] planePoint = poly.getShape()[0].toArray();
            //does this need to be perpendicular distance?
            Projection proj = VectorCalc.isect_vec_plane(collCentroid, planeNorm, planePoint, planeNorm);
            double dist = DistanceCalc.getDistanceNoRoot(collCentroid, proj.Point());
            if (dist < smallestDist)
            {
                smallestDist = dist;
                closest = poly;
            }
        }
        double[] closestNormal = new Plane(closest).getNV().toArray();
        double largestDist = 0;
        for (RefPoint3D p:collided.getPoints())
        {
            double[] v = VectorCalc.sub_v3v3(closest.getShape()[0].toArray(), p.toArray());
            double dot = VectorCalc.dot_v3v3(v, closestNormal);
            //we need farthest away from the closest plane but on the correct side
            if (dot > 0) {
                //get the projection form the p to the closest polygon plane
                Projection proj = VectorCalc.isect_vec_plane(p.toArray(), vec1, closest.getShape()[0].toArray(), closestNormal);
                //we want distance that is greatest, ie the distance the furthest node must travel
                double dist = DistanceCalc.getDistanceNoRoot(p.toArray(), proj.Point());
                if (dist > largestDist) {
                    largestDist = dist;
                }
            }
        }
        largestDist = Math.sqrt(largestDist);
        vec1 = VectorCalc.norm_v3(vec1);
        vec1 = VectorCalc.mul_v3_fl(vec1, largestDist);
        //find each vector required to move the whole object outside thisObject
        for (ForcePoint3D f:collided.getForcePoints())
        {
            double[] target = VectorCalc.add_v3v3(f.getPoint().toArray(), vec1);
            f.addForce(f.predictVectorRequired(target));
            prevSolutions.add(new Solution(f, target));
        }
        updatePoints(collided.getForcePoints());
    }
    * */
    @Override
    protected void applySolutions(ForceShape[] bs) {
        prevSolutions = new ArrayList<>();
        boolean wasInside = false;

        for (ForcePoint3D p:collided.getForcePoints())
        {
            double smallestDist = Double.MAX_VALUE;
            boolean inside = true;

            ArrayList<Solution> sols = new ArrayList<>();

            double[] p_d = p.getPoint().toArray();
            for (Polygon3D poly:thisShape.getShape())
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
                        sols.add(new Solution(p, proj.Point()));
                    }
                }
            }
            if (inside)
            {
                //if the point is inside all polygons, ie inside the shape as a whole
                prevSolutions.addAll(sols);
                wasInside = true;
            }
        }

        if (wasInside)
        {
            //we have easy points inside the object
            for (Solution pair:prevSolutions) {
                ForceVector3D pointVec = pair.point.predictVectorRequired(pair.target);
                pair.point.addForce(pointVec);
            }
            updatePoints(collided.getForcePoints());
        }
        else
        {
            //if not we fall back onto a very generic method
            double[] thisCentroid = thisShape.findCentre();
            double[] collCentroid = collided.findCentre();

            double[] v0 = VectorCalc.sub_v3v3(thisCentroid, collCentroid);
            v0 = VectorCalc.norm_v3(v0);
            v0 = VectorCalc.mul_v3_fl(v0, fallBackDist);

            for (ForcePoint3D p:collided.getForcePoints())
            {
                //add vector to point to find where that point should be, then get the predicted vector required to make it happen
                double[] newPos = VectorCalc.add_v3v3(v0, p.getPoint().toArray());
                p.addForce(p.predictVectorRequired(newPos));
                prevSolutions.add(new Solution(p, newPos));
            }

            double v1[] = VectorCalc.sub_v3v3(collCentroid, thisCentroid);
            v1 = VectorCalc.norm_v3(v1);
            v1 = VectorCalc.mul_v3_fl(v1, fallBackDist);
            for (ForcePoint3D p:collided.getForcePoints())
            {
                double[] newPos = VectorCalc.add_v3v3(v1, p.getPoint().toArray());
                p.addForce(p.predictVectorRequired(newPos));
                prevSolutions.add(new Solution(p, newPos));
            }

            updatePoints(collided.getForcePoints());
            updatePoints(thisShape.getForcePoints());
        }
    }

    private Polygon3D findClosest(double[] point, ArrayList<Polygon3D> polys)
    {
        double smallestDist = Double.MAX_VALUE;
        Polygon3D c0 = null;
        for (Polygon3D poly:thisShape.getShape())
        {
            double[] planeNorm = new Plane(poly).getNV().toArray();
            double[] planePoint = poly.getShape()[0].toArray();
            //does this need to be perpendicular distance?
            Projection proj = VectorCalc.isect_vec_plane(point, planeNorm, planePoint, planeNorm);
            double dist = DistanceCalc.getDistanceNoRoot(point, proj.Point());
            if (dist < smallestDist)
            {
                smallestDist = dist;
                c0 = poly;
            }
        }
        return c0;
    }

    @Override
    public void reapplyPreviousSolution() {
        for (Solution s:prevSolutions)
        {
            s.point.addForce(s.point.predictVectorRequired(s.target));
        }
    }
}
