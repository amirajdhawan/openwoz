package vyo.devices.motor.dynamixel.conn.rpi;

import java.util.Map;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import vyo.devices.motor.dynamixel.DynamixelMotorController;
import vyo.devices.motor.dynamixel.DynamixelMotorType;
import vyo.devices.motor.generic.MotorLoader;

/**
 * Controller for ANY XL/AX/MX Dynamixel motor (TTL only)
 * Should handle one motor type per class object
 *
 * @author Benny
 */
public class DynamixelPiMotorController extends DynamixelMotorController {
    private static DynamixelTTLConnection mConnection;

    public DynamixelPiMotorController(GpioPinDigitalOutput comLock, int motorType, String motorConfigURL) {
        this(new DynamixelTTLConnection(comLock), motorType, motorConfigURL);
    }

    public DynamixelPiMotorController(DynamixelTTLConnection connection, int motorType, String motorConfigURL)
            throws NullPointerException {

        if (mConnection == null) {
            if (connection != null) {
                DynamixelPiMotorController.mConnection = connection;
            } else {
                throw new NullPointerException("connection cant be null");
                //mConnection = new raspi.DynamixelTTLConnection(comLock);
            }
        }

        if (mConnection != null && !mConnection.isConnected()) {
            try {
                DynamixelPiMotorController.mConnection.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.motorType = motorType;
        loadMotorTypePreset();
        loadMotors(motorConfigURL);
        //System.out.println();
    }

    public DynamixelTTLConnection getConnection() {
        return mConnection;
    }

    @Override
    public Motor createMotor(Map<String, String> motorMap) {
        return new Motor(motorMap);
    }


    public class Motor extends DynamixelMotorController.Motor {
        //DynamixelPiMotor motor;

        public Motor(Map<String, String> motorMap) {
            super(motorMap);

            motor = mConnection.createMotor(mId, motorType);
            String motorTypeString = motorMap.get(MotorLoader.MOTOR_TYPE);

            // todo fix!! multiple instances problem
            switch (motorTypeString) {
                case "xl_320":
                    if (DynamixelMotorType.getMotorType(motorTypeString) == 0) {
                        motor.setSpeed(getVelocityValue(getDefaultVel()));
                        motor.setAcceleration(getAccelerationValue(getDefaultAcc()));
                    }
                    break;
                case "mx_64":
                case "mx_28":
                    if (DynamixelMotorType.getMotorType(motorTypeString) >= 3) {
                        motor.setSpeed(getVelocityValue(getDefaultVel()));
                        motor.setAcceleration(getAccelerationValue(getDefaultAcc()));
                    }
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "{" +
                "motorType=" + motorType +
                ", velUnit=" + velUnit +
                ", posUnit=" + posUnit +
                ", accUnit=" + accUnit +
                ", mConnection=" + mConnection +
                '}';
    }
}
