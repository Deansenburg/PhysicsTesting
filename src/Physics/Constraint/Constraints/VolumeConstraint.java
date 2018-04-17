package Physics.Constraint.Constraints;

import Physics.Force3DInterface.ForceVector3D;
import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.CalculationHelper.VectorCalc;
import Physics.ForcePoint3D;
import GxEngine3D.Model.Polygon3D;
import GxEngine3D.Model.RefPoint3D;

import java.util.ArrayList;

//fundamentally this approach doesnt work as intented with lots of strange occurrences
public class VolumeConstraint extends BasePointConstraint{

    ArrayList<RefPoint3D[]> triangles;
    double minVol, maxVol, lastVol, avgDistToMid;

    double[][] prevPoints;
    ForcePoint3D[] prevNodes;

    public VolumeConstraint(ArrayList<Polygon3D> polys, double tol)
    {
        //convert polys to triangles
        triangles = new ArrayList<>();
        for (Polygon3D p:polys)
        {
            RefPoint3D[] points = p.getShape();
            for (int i=1;i<points.length-1;i++)
            {
                triangles.add(new RefPoint3D[]{
                        points[0],
                        points[i],
                        points[i+1]
                });
            }
        }

        //find startingVolume of shape
        double startingVolume = getVolume(triangles);
        minVol = startingVolume *(1-tol);
        maxVol = startingVolume *(1+tol);
        double[] cen = polys.get(0).getBelongsTo().findCentre();
        double total = 0, num = 0;
        for (RefPoint3D p:polys.get(0).getBelongsTo().getPoints())
        {
            double dist = DistanceCalc.getDistance(cen, p.toArray());
            total += dist;
            num++;
        }
        avgDistToMid = total / num;
    }

    //note that this method is highly dependant upon consistent ordering of vertices
    //note that the specific order doesnt matter, so long that its consistent
    private double getVolume(ArrayList<RefPoint3D[]> triangles)
    {
        double total = 0;
        for (RefPoint3D[] p:triangles)
        {
            double temp = VectorCalc.dot_v3v3(p[2].toArray(), VectorCalc.cross(p[0].toArray(), p[1].toArray()));
            total+= temp;
        }
        total = Math.abs(total / 6);
//        System.out.println(total);
        return total;
    }

    @Override
    public boolean isViolating(ForcePoint3D[] points) {
        //we already have the refpoints for this shape and the triangles pre-computed
        lastVol = getVolume(triangles);//for caching reasons since finding volume can be expensive
        return lastVol < minVol || lastVol > maxVol;
    }

    //can be just called now that its forcePoint3D
    private double[] findCentre(ForcePoint3D[] points)
    {
        double avX = 0, avY = 0, avZ = 0;
        for (ForcePoint3D p : points) {
            RefPoint3D temp = p.getPoint();
            avX += temp.X();
            avY += temp.Y();
            avZ += temp.Z();
        }

        avX /= points.length;
        avY /= points.length;
        avZ /= points.length;
        return new double[] { avX, avY, avZ };
    }

    @Override
    public void applySolutions(ForcePoint3D[] points) {
//        System.out.println("Volume Physics.Constraint");
        prevPoints = new double[points.length][];

        double[] midPoint = findCentre(points);
        for (int i=0;i<points.length;i++)
        {
            double[] p = points[i].getPoint().toArray();
            double[] v;
            //rather than pushing all or pulling all, we check if the distance from centre is more or less than what we started with
            //this will not work properly on irregular shapes
            double dist = DistanceCalc.getDistance(p, midPoint);
            if (avgDistToMid > dist)
            {
                v = VectorCalc.sub_v3v3(p, midPoint);
                dist = avgDistToMid - dist;
            }
            else
            {
                v = VectorCalc.sub_v3v3(midPoint, p);
                dist = dist - avgDistToMid;
            }
            v = VectorCalc.norm_v3(v);//normalised
            v = VectorCalc.mul_v3_fl(v, dist);
            v = VectorCalc.add_v3v3(p, v);
            ForceVector3D vec = points[i].predictVectorRequired(v);
            points[i].addForce(vec);
            prevPoints[i] = v;
        }
        prevNodes = points;
    }

    @Override
    public void reapplyPreviousSolution() {
        for (int i=0;i<prevNodes.length;i++)
        {
            prevNodes[i].addForce(prevNodes[i].predictVectorRequired(prevPoints[i]));
        }
    }
}
