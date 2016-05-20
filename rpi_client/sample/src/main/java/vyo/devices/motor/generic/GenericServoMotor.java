package vyo.devices.motor.generic;

/**
 * generic pwm servo motor abstract class
 *
 * @author Benny on 3/18/2016.
 */
public abstract class GenericServoMotor {
    protected int id;
    protected int motorType;
    protected int mMaxAngle;              // in degrees
    protected int mCurrentPosition;       // in degrees
    protected int mFrequency;             // in hertz
    private double mPeriod;
    private double mMinPeriod, mMaxPeriod;
    private double mMinDutyCycle, mMaxDutyCycle;
    protected boolean D = false;

    public GenericServoMotor(int id, int motorType) {
        this.id = id;                     // not verifying if the id is in use
        this.motorType = motorType;
        this.mMaxAngle = getMotorMaxAngle();
        this.mCurrentPosition = getZeroPosition();
        this.mFrequency = getFrequency();
        setPeriod(1 / mFrequency);
        setMinPeriod(0.1);
        setMaxPeriod(0.2);
    }
    
    public static long map(long x, long inMin, long inMax, long outMin, long outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    protected abstract void move(int angle);

    protected abstract void move(int angle, int interpolator);

    protected abstract void move(int angle, int interpolator, int numOfSamples);

    protected abstract void move(int angle, int interpolator, int numOfSamples, int durationMultiplier);

    private int getMotorMaxAngle() {

        switch (motorType) {
            case GenericServoMotorType.MG_996R:
            case GenericServoMotorType.MG_995:
            case GenericServoMotorType.MG_945:
                return 180;

            default:
                return 120;
        }
    }

    private int getFrequency() {
        switch (motorType) {
            case GenericServoMotorType.MG_996R:
            case GenericServoMotorType.MG_995:
            case GenericServoMotorType.MG_945:
                return 50;

            default:
                return 50;
        }
    }

    private int getZeroPosition() {
        return 0;
    }

    public void moveToAngle(int angle) {
        int relativeMaxAngle = mMaxAngle / 2;
        if (angle < -relativeMaxAngle || angle > relativeMaxAngle) {
            System.out.println("Angle [" + angle + "] out of bounds!");
            return;
        }

        if (D) System.out.println("Move to angle " + angle);
        this.mCurrentPosition = angle;
        move(angle);
    }

    public void moveToCenter() { moveToAngle(getZeroPosition()); }

    public void setPeriod(double period) {
        this.mPeriod = period;
        if (mMaxPeriod != 0) setMaxPeriod(mMaxPeriod);
        if (mMinPeriod != 0) setMinPeriod(mMinPeriod);
    }

    public void setMaxPeriod(double maxPeriod) {
        this.mMaxPeriod = maxPeriod;
        this.mMaxDutyCycle = maxPeriod / mPeriod;
    }

    public void setMinPeriod(double minPeriod) {
        this.mMinPeriod = minPeriod;
        this.mMinDutyCycle = minPeriod / mPeriod;
    }

    public double getMinDutyCycle() {
        return mMinDutyCycle;
    }

    public double getMaxDutyCycle() {
        return mMaxDutyCycle;
    }
}
