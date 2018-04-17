package Physics;

import GxEngine3D.Model.RefPoint3D;
import GxEngine3D.Model.Vector;
import Physics.Force3DInterface.CoordinateCalc3D;
import Physics.Force3DInterface.ForceVector3D;
import Physics.Force3DInterface.ForceVector3DCalc;
import GxEngine3D.CalculationHelper.DistanceCalc;
import GxEngine3D.CalculationHelper.VectorCalc;

import java.util.ArrayList;
import java.util.Iterator;

public class ForcePoint3D {

    RefPoint3D point;
    protected int mass = 3;
    protected float dragCoefficient = 0.01f, time = 0.05f;


    protected ArrayList<ForceVector3D> forces = new ArrayList<ForceVector3D>();
    protected ArrayList<ForceVector3D> accelerationForces = new ArrayList<ForceVector3D>();

    ForceVector3D acceleration = new ForceVector3D(new Vector(0,  0, 0), 0);
    ForceVector3D velocity = new ForceVector3D(new Vector(0,  0, 0), 0);


    protected ArrayList<ForceVector3D> snapshotForces;

    public ForcePoint3D(RefPoint3D p) {
        point = p;
    }

    public void addForce(ForceVector3D vec)
    {
        if (vec.getForceValue() > 0)
        {
            forces.add(vec);
        }
    }
    public void addAccelerationForce(ForceVector3D vec)
    {
        if (vec.getForceValue() > 0)
        {
            accelerationForces.add(vec);
        }
    }

    public void updateCoordinates(CoordinateCalc3D calc, boolean update)
    {
        //evaluate soft forces
        ForceVector3D finalVec = getFinalVector(update);
        if (finalVec.getForceValue() != 0) {
            double[] newPoints = calc.findCoordinate(finalVec, point.toArray(), time);
            point.setX(newPoints[0]);
            point.setY(newPoints[1]);
            point.setZ(newPoints[2]);
        }
    }

    public ForceVector3D getFinalVector(boolean update) {
        ForceVector3D fVec = ForceVector3DCalc.getResultantVector(forces);
        if (update) {
            forces.clear();
        }
        ForceVector3D aVec = ForceVector3DCalc.getResultantVector(accelerationForces);
        if (update) {
            accelerationForces.clear();
        }


        //update acceleration
        ForceVector3D accel = new ForceVector3D(fVec.getVector(), fVec.getForceValue() / mass);
        //our decay value is 0.01 at velocity, hence divide by time
        ForceVector3D oldAccel = new ForceVector3D(acceleration.getVector(), acceleration.getForceValue() * (0.01d / time));
        ForceVector3D newAccel = ForceVector3DCalc.getResultantVector(new ArrayList<ForceVector3D>(){{
            add(oldAccel);
            add(accel);
            add(aVec);
        }});
        if (update)
        {
            acceleration = newAccel;
        }

        //update velocity
        ForceVector3D vel = new ForceVector3D(newAccel.getVector(), newAccel.getForceValue() * time);
        ForceVector3D oldVel = velocity;
        ForceVector3D newVel = ForceVector3DCalc.getResultantVector(new ArrayList<ForceVector3D>(){{
            add(oldVel);
            add(vel);
        }});

        if (update) {
            velocity = newVel;
            //air resistance formula for calculating drag
            //D = Cd * 0.5 * r * V^2 * A
            //cD is drag coefficient, r is air density, as we are not changing drag based on object, this can be collapsed into one
            //A refers to area of the object, since all of our objects are points this becomes irrelevant to the equation
            //D = V^2 * Cd
            double dragValue = (newVel.getForceValue()*newVel.getForceValue()) * dragCoefficient;
            //we scale by -1 to reverse the direction of the vector
            ForceVector3D drag = new ForceVector3D(newVel.getVector().scale(-1), dragValue);
            addAccelerationForce(drag);
        }
//        System.out.println("V " + velocity + " A " + acceleration);
        return newVel;
    }

    public ForceVector3D predictVectorRequired(double[] endPos)
    {
        //if end = start + x, then x = end - start
        double[] newDirection = VectorCalc.sub_v3v3(endPos, point.toArray());

        //to find force required, we need displacement
        double displacement = DistanceCalc.getDistance(endPos, point.toArray());
        //D = 0.5d * (v1 + v2) * t, we need v1 and v2
        //2D/t = v1 + v2, v1 == F, v2 == Fx, 2D/t = F + Fx
        //2D/t = F(x+1) => F = 2D/t/(x+1), x == 0.01
        //force needs to be final, so squashed formula
        //also this is force of velocity, we need acceleration at least
        //V = a * t => a = V/t
        //this is acceleration we need initial force
        //a = F / m => F = a*m
        double velocity = ((2 * displacement) / time) / (0.01+1);
        double accel = velocity / time;
        double force = accel * mass;

        ForceVector3D oldAccelCancel = new ForceVector3D(acceleration.getVector().scale(-1), (acceleration.getForceValue()*(0.01d / time))*mass);
        ForceVector3D oldVelocityCancel = new ForceVector3D(this.velocity.getVector().scale(-1), this.velocity.getForceValue()*mass/time);

        //to end up at the correct position we need to cancel out future velocity from both acceleration forces and actual forces
        ForceVector3D finalForceVec = ForceVector3DCalc.getResultantVector(forces);
        ForceVector3D forceCancel = new ForceVector3D(finalForceVec.getVector().scale(-1), finalForceVec.getForceValue());

        ForceVector3D finalAccelerationVec = ForceVector3DCalc.getResultantVector(accelerationForces);
        //a = f/m => f = a*m
        ForceVector3D accelerationCancel = new ForceVector3D(finalAccelerationVec.getVector().scale(-1), finalAccelerationVec.getForceValue()*mass);

        ForceVector3D predictedVector = ForceVector3DCalc.getResultantVector(new ArrayList<ForceVector3D>(){{
            add(new ForceVector3D(new Vector(newDirection), force));
            add(forceCancel);
            add(accelerationCancel);
            add(oldAccelCancel);
            add(oldVelocityCancel);
        }});

        return predictedVector;
    }

    public RefPoint3D getPoint()
    {
        return point;
    }

    @Override
    public String toString() {
        return point.toString();
    }

    public boolean hasSamePoint(RefPoint3D point)
    {
        return this.point.equals(point);
    }


    public void snapshotForces()
    {
        if (snapshotForces == null) {
            snapshotForces = (ArrayList<ForceVector3D>) forces.clone();
        }
        else
        {
            System.out.println("ForcePoint3D-snapshotForces: snapshot already taken");
        }
    }
    public void revertForces()
    {
        if (snapshotForces != null)
        {
            forces = snapshotForces;
            snapshotForces = null;
        }
        else
        {
            System.out.println("ForcePoint3D-revertForces: cannot revert forces if null");
        }
    }
    public void purgeForces(int flag)
    {
        if (forces.size() > 1 || accelerationForces.size() > 1)
        {
            getFinalVector(true);
        }
        Iterator<ForceVector3D> i = forces.iterator();
        while(i.hasNext())
        {
            ForceVector3D v = i.next();
            if (v.getFlag() == flag)
            {
                i.remove();
            }
        }
    }
    public void purgeAcceleration(int flag)
    {
        if (forces.size() > 1 || accelerationForces.size() > 1)
        {
            getFinalVector(true);
        }
        Iterator<ForceVector3D> i = accelerationForces.iterator();
        while(i.hasNext())
        {
            ForceVector3D v = i.next();
            if (v.getFlag() == flag)
            {
                i.remove();
            }
        }
    }
}
