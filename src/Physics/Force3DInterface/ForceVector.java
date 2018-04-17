package Physics.Force3DInterface;

public class ForceVector {

    protected float mXComp;
	protected float mYComp;

	protected float mDirection;

	protected float mForceAmount;

    public ForceVector(float direction, float force) {
	mDirection = direction;
	mXComp = (float) Math.cos(direction);
	mYComp = (float) Math.sin(direction);
	mForceAmount = force;
	if (mForceAmount < 0)
	{
	    mForceAmount = 0;
	}
    }

    public ForceVector(double direction, double force) {
	mDirection = (float) direction;
	mXComp = (float) Math.cos(direction);
	mYComp = (float) Math.sin(direction);
	mForceAmount = (float) force;
    }

    public float getDirection()
    {
	return mDirection;
    }
    public float getXComp()
    {
	return mXComp;
    }
    public float getYComp()
    {
	return mYComp;
    }

    public float getForceValue() {
	return mForceAmount;
    }
}
