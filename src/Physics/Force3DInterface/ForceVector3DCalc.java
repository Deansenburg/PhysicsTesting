package Physics.Force3DInterface;

import GxEngine3D.CalculationHelper.FastMath;
import GxEngine3D.Model.Vector;

import java.util.ArrayList;

/**
 * Created by Dean on 29/12/16.
 */
public class ForceVector3DCalc {

    public static ForceVector3D getResultantVector(ArrayList<ForceVector3D> forces) {
        if (forces.size() == 1)
        {
            return forces.get(0);
        }
        float sumOfXComponent = 0;
        float sumOfYComponent = 0;
        float sumOfZComponent = 0;

        for (ForceVector3D fV : forces) {
            sumOfXComponent += fV.getForceValue() * fV.getXComp();
            sumOfYComponent += fV.getForceValue() * fV.getYComp();
            sumOfZComponent += fV.getForceValue() * fV.getZComp();
        }

        Vector resultantDirection = new Vector(sumOfXComponent, sumOfYComponent, sumOfZComponent);

        float resultantForce;
        // reuse of variables - this point on sumX/sumY == sumX^2/sumY^2
        sumOfXComponent = sumOfXComponent * sumOfXComponent;
        sumOfYComponent = sumOfYComponent * sumOfYComponent;
        sumOfZComponent = sumOfZComponent * sumOfZComponent;

        // resultant force == ((sumX^2)+(sumY^2))^1/2(sqrt)
        resultantForce = sumOfXComponent + sumOfYComponent + sumOfZComponent;

        resultantForce = FastMath.sqrt(resultantForce);

        return new ForceVector3D(resultantDirection, resultantForce);
    }
}
