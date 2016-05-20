package vyo.devices.motor.dynamixel;

import java.util.Map;

import vyo.devices.motor.generic.MotorController;
import vyo.devices.motor.helper.Interpolator;

/**
 * Controller for ANY XL/AX/MX Dynamixel motor
 *
 * @author Benny
 */
public abstract class DynamixelMotorController extends MotorController {
    protected int motorType;
    protected float velUnit;  // RPM
    protected float posUnit;  // Degrees
    protected float accUnit;  // Degrees


    protected void loadMotorTypePreset() {
        switch (motorType) {
            case DynamixelMotorType.XL_320:
            case DynamixelMotorType.AX_12W:
            case DynamixelMotorType.AX_12:
            case DynamixelMotorType.AX_18:
                velUnit = .111f;
                posUnit = 0.29f;
                accUnit = 0f;
                break;

            case DynamixelMotorType.MX_12:
            case DynamixelMotorType.MX_28:
            case DynamixelMotorType.MX_64:
            case DynamixelMotorType.MX_106:
                velUnit = .114f;
                posUnit = 0.088f;
                accUnit = 8.583f;
                break;
        }
    }



    public abstract class Motor extends MotorController.Motor {
        protected DynamixelMotor motor;

        public Motor(Map<String, String> motorMap) {
            super(motorMap);
        }

        @Override
        public void move(float pos) { move(pos, Float.NaN, Float.NaN, DynamixelUtil.UNLIMITED); }

        @Override
        public void move(float pos, float vel, float acc) {
            move(pos, vel, acc, DynamixelUtil.UNLIMITED);
        }

        @Override
        /** move motor in duration */
        public void move(float pos, float vel, float acc, int duration) {
            // Converts the position from radians to Dynamixel unit value
            int positionValue = getPositionValue(pos);

            if (duration == DynamixelUtil.UNLIMITED) {
                if (!Float.isNaN(acc)) { setAcceleration(acc); }
                if (!Float.isNaN(vel)) {
                    //setVelocity(vel);
                    motor.moveToPosition(positionValue, getVelocityValue(vel));
                } else {
                    motor.moveToPosition(positionValue);
                }
            } else {
                motor.moveToPositionInTime(positionValue, duration);
            }
        }

        @Override
        public void move(float goalPos, float lastPos, int function) {
            move(goalPos, lastPos, function, Interpolator.DEFAULT_SAMPLER);
        }

        @Override
        /** move motor with interpolation */
        public void move(float goalPos, float lastPos, int function, int numOfSamples) {
            // Converts the position from radians to Dynamixel unit value
            int currentPosition = getPositionValue(lastPos);
            int goalPosition = getPositionValue(goalPos);

            // Generate interpolation table
            float[] interpolationTable = Interpolator.interpolate(currentPosition, goalPosition, function, numOfSamples);

            for (float value : interpolationTable) {
                motor.moveToPosition(Math.round(value));
            }
        }

        @Override
        /** move motor in multi step mode */
        public void moveContinuously(String name, float offset, float position) {
            // Converts the position from radians to Dynamixel unit value
            float factor = mFlipped ? -1 : 1;
            int offsetPositionValue = (int) (Math.ceil(mZeroPos + factor * offset * 180.0 / Math.PI / posUnit));
            int positionValue = getPositionValue(position);

            motor.moveContinuously(offsetPositionValue, positionValue);
        }

        @Override
        /** move all motors in syncWrite mode */
        public void moveAll() {
            motor.syncMove();
        }

        @Override
        public void addSyncMove(float pos) {
            // Converts the position from radians to Dynamixel unit value
            int positionValue = getPositionValue(pos);

            motor.addSyncMove(positionValue);
        }

        @Override
        public void addSyncMove(float pos, float vel) {
            // Converts the position from radians to Dynamixel unit value
            int positionValue = getPositionValue(pos);
            int velocityValue = getVelocityValue(vel);

            motor.addSyncMove(positionValue, velocityValue);
        }

        /** Converts the position from Radians to Dynamixel unit values */
        public int getPositionValue(float pos) {
            // Converts the position from radians to Dynamixel unit value
            float factor = mFlipped ? -1 : 1;
            int positionValue = (int) (Math.ceil(mZeroPos + factor * capPosition(pos) * 180.0 / Math.PI / posUnit));

            if (D) {
                System.out.println("Position:  " + pos);
                System.out.println("mFlipped = " + mFlipped);
                System.out.println("Capped Position:  " + capPosition(pos));
                System.out.println("Zero:  " + mZeroPos + " / Capped: " + (capPosition(pos) * 180.0 / Math.PI / posUnit));
                System.out.println("PositionValue:  " + positionValue);
            }

            return positionValue;
        }

        /** Converts the velocity from Rad/s to Dynamixel unit values */
        public int getVelocityValue(float vel) {

            // Calculate the velocity from Rad/s to RPM
            float velocityRPM = (float) (60f * capVel(vel) / (2f * Math.PI));

            // Convert to Dynamixel unit values
            int velocityValue = (int) (float) Math.floor(velocityRPM / velUnit);
            if (velocityValue == 0) velocityValue = 1; // Vel = 0 means no limit

            if (D) {
                System.out.println("Velocity:  " + vel);
                System.out.println("VelocityRPM:  " + velocityRPM);
                System.out.println("VelocityValue:  " + velocityValue);
            }

            return velocityValue;
        }

        /** Converts the acceleration from Rad/s to Dynamixel unit values */
        public int getAccelerationValue(float acc) {
            // acceleration is not supported in this models
            if (!isAccelerationSupported(motorType)) return 0;

            // Calculate the Acceleration from Rad/s to RPM
            float AccelerationRPM = (float) (60f * acc / (2f * Math.PI));

            // Convert to Dynamixel unit values
            int AccelerationValue = (int) (float) Math.floor(AccelerationRPM / accUnit);

            if (D) {
                System.out.println("Acceleration:  " + acc);
                System.out.println("AccelerationRPM:  " + AccelerationRPM);
                System.out.println("AccelerationValue:  " + AccelerationValue);
            }

            return AccelerationValue;
        }

        @Override
        public void setAcceleration(float radss) {
            // acceleration is not supported in this models
            if (!isAccelerationSupported(motorType)) return;

            // Convert to Dynamixel acceleration value
            int acclValue;

            if (radss == -1)
                acclValue = DynamixelUtil.UNLIMITED;
            else
                acclValue = (int) (Math.round(Math.min(254, Math.max(1, radss * 180 / Math.PI / accUnit))));

            if (D) {
                System.out.println("Acceleration:  " + radss);
                System.out.println("AccelerationValue:  " + acclValue);
            }

            motor.setAcceleration(acclValue);
        }

        @Override
        public void setVelocity(float radss) {
            int velocity = getVelocityValue(radss);
            if (D) System.out.println("Seting vel of motor " + motor.getId() + " to " + velocity);
            motor.setSpeed(velocity);
        }

        @Override
        public void setDefaultVelocity() {
            setVelocity(getDefaultVel());
        }
    }

    @Override
    public String toString() {
        return "{" +
                "motorType=" + motorType +
                ", velUnit=" + velUnit +
                ", posUnit=" + posUnit +
                ", accUnit=" + accUnit +
                '}';
    }
}
