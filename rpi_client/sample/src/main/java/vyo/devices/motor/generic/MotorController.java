package vyo.devices.motor.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vyo.devices.motor.dynamixel.DynamixelMotorType;

/**
 * Handle loading each motor presets,
 * Keeps track of previous commands.
 *
 * @author hoffman (refactored by Benny)
 */
public abstract class MotorController {

    protected Map<String, Motor> mMotors;
    protected Map<String, Float> mLastCmdPos;  // in radians

    protected final boolean D = true;         // enable log on debug mode

    public void loadMotors(String motorConfigURL) {
        // Getting the list of motorsWWWWWWW
        MotorLoader loader = new MotorLoader();

        mMotors = loader.load(this, motorConfigURL);
        mLastCmdPos = new HashMap<>(mMotors.size());

        for (String m: mMotors.keySet()) {
            mLastCmdPos.put(m, 0f);
        }
    }

    /*
     * This needs to be overridden by each specific motor controller to actually create the motor
     * using the parameters from the config file.
     */
    public abstract Motor createMotor(Map<String, String> motorMap);

    /**
     * Moves a motor with maximum velocity and acceleration
     *
     * @param name motor name
     * @param pos position
     */
    public void moveMotor(String name, float pos) {
        if (D) System.out.println("Moving [" + name + "] to " + pos);

        Motor m = mMotors.get(name);
        //m.move(pos, m.getMaxVel(), m.getMaxAcc());
        // m.move(pos, m.getDefaultVel(), m.getDefaultAcc());
        m.move(pos);
        mLastCmdPos.put(name, pos);
    }

    public void moveMotor(String name, float pos, float vel, float acc) {
        if (D) System.out.println("Moving [" + name + "] to " + pos
                + ", vel: " + vel + ", acc: " + acc);

        mMotors.get(name).move(pos, vel, acc);
        mLastCmdPos.put(name, pos);
    }

    public void moveMotor(String name, float pos, float vel, float acc, int duration) {
        if (D) System.out.println("Moving [" + name + "] to " + pos
                + ", vel: " + vel + ", acc: " + acc + " in " + duration + " ms");

        mMotors.get(name).move(pos, vel, acc, duration);
        mLastCmdPos.put(name, pos);
    }

    /**
     * Moves a motor with custom interpolation function
     *
     * @param name motor name
     * @param pos position
     * @param interpolator interpolation function
     */
    public void moveMotor(String name, float pos, int interpolator) {
        if (D) System.out.println("Moving [" + name + "] to " + pos + ", interpolator: " + interpolator);

        Motor m = mMotors.get(name);
        m.move(pos, mLastCmdPos.get(name), interpolator);
        mLastCmdPos.put(name, pos);
    }

    /**
     * Move a motor in multi position mode
     *
     * @param name motor name
     * @param offsetInRads offset from the current position in rad
     */
    public void moveMotorContinuously(String name, float offsetInRads) {
        Motor m = mMotors.get(name);
        float currentPosition = (mLastCmdPos.get(name) + offsetInRads) % m.getMaxPos();  // in rad

        m.moveContinuously(name, offsetInRads, currentPosition);
        mLastCmdPos.put(name, currentPosition);

        if (D) System.out.println("Moving [" + name + "] with offset: " + offsetInRads
                + ", current position: " + currentPosition);
    }

    /**
     * Moves all the motor in one command with the sync mode function
     */
    public void moveAll() {
        if (D) System.out.println("Moving all motors");

        Motor m = mMotors.get("hip"); // todo find a better way
        m.moveAll();
    }

    /**
     * Adds a move to the sync move queue
     *
     * @param name motor name
     * @param pos position
     */
    public void addSyncMove(String name, float pos) {
        if (D) System.out.println("Moving [" + name + "] to " + pos);

        Motor m = mMotors.get(name);
        m.addSyncMove(pos);
        mLastCmdPos.put(name, pos);
    }

    public void addSyncMove(String name, float pos, float vel) {
        if (D) System.out.println("Moving [" + name + "] to " + pos);

        Motor m = mMotors.get(name);
        m.addSyncMove(pos, vel);
        mLastCmdPos.put(name, pos);
    }

    public Motor getMotor(String name) {
        return mMotors.get(name);
    }

    public int getMotorId(String name) {
        int id = -1;

        if (mMotors.containsKey(name)) {
            id = mMotors.get(name).mId;
        }

        return id;
    }

    public List<String> getMotorNames() {
        return new ArrayList<>(mMotors.keySet());
    }

    public String getMotorName(int motorId) {
        String motorName = null;

        for (Map.Entry<String, Motor> entry : mMotors.entrySet()) {
            if (entry.getValue().mId == motorId) {
                motorName = entry.getKey();
                break;
            }
        }

        return motorName;
    }

    public float getLastCmdPos(String motorName) {
        if (mLastCmdPos.containsKey(motorName))
            return mLastCmdPos.get(motorName);
        else
            return 0;
    }

    @Override
    public String toString() {
        return "{" +
                "mMotors=" + mMotors +
                ", mLastCmdPos=" + mLastCmdPos +
                '}';
    }


    /**
     * Holds motors specific setting
     */
    public abstract class Motor {

        protected String mName;
        protected byte mId;
        protected int mZeroPos;
        protected float mMinPos;      // in Rad/s from the zero pos
        protected float mMaxPos;      // in Rad/s from the zero pos
        protected boolean mFlipped;   // Flip the motor's direction
        protected int mMotorType;     // DynamixelMotorType
        protected float mDefaultAcc;  // in Rad/s
        protected float mDefaultVel;  // in Rad/s
        protected float mMaxVel;      // in Rad/s
        protected float mMaxAcc;      // from 0~254, When it is set to 254, it becomes 2180 Degree / sec^2

        /**
         * This is the motor constructor it does not deal with any types of
         * errors so the data must be correct and flawless

         */
        public Motor(Map<String, String> motorMap) {
            mName = motorMap.get(MotorLoader.MOTOR_NAME);
            mId = Byte.parseByte(motorMap.get(MotorLoader.MOTOR_ID));
            mZeroPos = Integer.parseInt(motorMap.get(MotorLoader.MOTOR_ZERO_POS));
            mMinPos = Float.parseFloat(motorMap.get(MotorLoader.MOTOR_MIN_POS));
            mMaxPos = Float.parseFloat(motorMap.get(MotorLoader.MOTOR_MAX_POS));
            mFlipped = motorMap.get(MotorLoader.MOTOR_FLIPPED).toLowerCase().equals("true");
            mMotorType = DynamixelMotorType.getMotorType(motorMap.get(MotorLoader.MOTOR_TYPE));
            mDefaultAcc = Float.parseFloat(motorMap.get(MotorLoader.MOTOR_DEFAULT_ACC));
            mDefaultVel = Float.parseFloat(motorMap.get(MotorLoader.MOTOR_DEFAULT_VEL));
            mMaxVel = Float.parseFloat(motorMap.get(MotorLoader.MOTOR_MAX_VEL));
            // mMaxAcc todo load from config
        }

        /**
         * Move the motor after setting the correct values
         *
         * @param goalPos position in radians from zero position
         * @param vel velocity in radians per second
         * @param acc acceleration in radians per second squared
         * @param duration duration in milliseconds
         */
        public abstract void move(float goalPos, float vel, float acc, int duration);

        public abstract void move(float goalPos, float vel, float acc);

        public abstract void move(float goalPos);

        public abstract void move(float goalPos, float lastPos, int interpolator);

        public abstract void move(float goalPos, float lastPos, int interpolator, int numOfSamples);

        public abstract void moveContinuously(String name, float offsetInRads, float position);

        public abstract void moveAll();

        public abstract void addSyncMove(float pos);

        public abstract void addSyncMove(float pos, float vel);

        public abstract void setAcceleration(float radss);

        public abstract void setVelocity(float radss);

        public abstract void setDefaultVelocity();

        public float getCenter() {
            return mMaxPos - mMinPos;
        }

        public String getName() {
            return mName;
        }

        public byte getmId() {
            return mId;
        }

        public float getMaxPos() {
            return mMaxPos;
        }

        public float getMinPos() {
            return mMinPos;
        }

        public float getMaxVel() {
            return mMaxVel;
        }

        public int getMotorType() {
            return mMotorType;
        }

        public float getDefaultAcc() {
            return mDefaultAcc;
        }

        public float getDefaultVel() {
            return mDefaultVel;
        }

        public float getMaxAcc() {
            return 0;
        }

        // makes sure we don't divert from the motors position range
        protected float capPosition(float position) {
            float cappedPos = Math.min(mMaxPos, Math.max(mMinPos, position));

            if (D) System.out.println("Capped pos " + position + " to " + cappedPos);

            return cappedPos;
        }

        // makes sure we don't divert from the motors velocity range
        protected float capVel(float vel) {
            float cappedVel = Math.min(mMaxVel, Math.max(0, vel));

            if (D) System.out.println("Capped vel " + vel + " to " + cappedVel);

            return cappedVel;
        }


        // makes sure we don't divert from the motors acceleration range
        protected float capAcc(float acc) {
            float cappedAcc = (acc == 0) ? 0 : Math.min(mMaxAcc, Math.max(1, acc));

            if (D) System.out.println("Capped acc " + acc + " to " + cappedAcc);

            return cappedAcc;
        }

        protected boolean isAccelerationSupported(int motorType) {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                case DynamixelMotorType.AX_12:
                case DynamixelMotorType.AX_18:
                    return false;

                case DynamixelMotorType.AX_12W:
                case DynamixelMotorType.MX_12:
                case DynamixelMotorType.MX_28:
                case DynamixelMotorType.MX_64:
                case DynamixelMotorType.MX_106:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public String toString() {
            return "Motor{" +
                    "mName=" + mName +
                    ", mId=" + mId +
                    ", mZeroPos=" + mZeroPos +
                    ", mMinPos=" + mMinPos +
                    ", mMaxPos=" + mMaxPos +
                    ", mFlipped=" + mFlipped +
                    ", mMotorType=" + mMotorType +
                    ", mDefaultAcc=" + mDefaultAcc +
                    ", mDefaultVel=" + mDefaultVel +
                    ", mMaxVel=" + mMaxVel +
                    '}';
        }
    }
}