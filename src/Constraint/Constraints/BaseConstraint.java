package Constraint.Constraints;

import Force3DInterface.CoordinateCalc3D;
import GxEngine3D.Model.ForcePoint3D;

import java.util.ArrayList;

public abstract class BaseConstraint implements IConstraint{

    static CoordinateCalc3D calc = new CoordinateCalc3D();

    protected void updatePoints(ForcePoint3D[] points)
    {
        for (ForcePoint3D p:points)
        {
            p.updateCoordinates(calc, false);
        }
        reapplyPreviousSolution();
    }
    protected void updatePoints(ArrayList<ForcePoint3D> points)
    {
        for (ForcePoint3D p:points)
        {
            p.updateCoordinates(calc, false);
        }
        reapplyPreviousSolution();
    }
}
