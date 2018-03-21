package Force3DInterface;

/**
 * Created by Dean on 29/12/16.
 */
public class CoordinateCalc3D{

    public double[] findCoordinate(ForceVector3D aV, double[] pos, double t) {

        double v1 = aV.mForceAmount;
        double v2 = aV.mForceAmount * 0.01d;

        double displacement = 0.5d * (v1 + v2) * t;

        double xD = displacement * aV.getXComp();
        double yD = displacement * aV.getYComp();
        double zD = displacement * aV.getZComp();

        pos[0] += xD;
        pos[1] += yD;
        pos[2] += zD;

        return pos;
    }
}
