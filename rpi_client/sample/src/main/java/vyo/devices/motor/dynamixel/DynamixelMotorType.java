package vyo.devices.motor.dynamixel;

/**
 * Dynamixel motor type definition
 *
 * @author Benny
 */
public class DynamixelMotorType {
    // motor types
    public static final int XL_320 = 0;
    public static final int AX_12W = 1;     // AX-12W
    public static final int AX_12 = 2;      // AX-12A / AX-12+ / AX-12
    public static final int AX_18 = 3;      // AX-18F / AX-18A
    public static final int MX_12 = 4;      // MX-12W  todo
    public static final int MX_28 = 5;      // MX-28T / MX-28AT
    public static final int MX_64 = 6;      // MX-64T / MX-64AT
    public static final int MX_106 = 7;     // MX-106T

    // operation modes
    public static final int WHEEL_MODE = 0;
    public static final int JOINT_MODE= 1;
    public static final int MULTI_TURN_MODE = 2;

    public static int getMotorType(String motorTypeName) {
        int motorType = -1;     // Unknown motor

        switch (motorTypeName.toUpperCase()) {
            case "XL_320":
                motorType = XL_320;
                break;
            case "AX_12W":
                motorType = AX_12W;
                break;
            case "AX_12":
                motorType = AX_12;
                break;
            case "AX_18":
                motorType = AX_18;
                break;
            case "MX_12":
                motorType = MX_12;
                break;
            case "MX_28":
                motorType = MX_28;
                break;
            case "MX_64":
                motorType = MX_64;
                break;
            case "MX_106":
                motorType = MX_106;
                break;
        }

        return motorType;
    }

    public static int getMotorType(int id) {
        // todo - only temp solution for vyo only
        int motorType;     // Unknown motor

        switch (id) {
            case 1:
                motorType = MX_64;
                break;
            case 2:
                motorType = MX_28;
                break;
            case 3:
            case 4:
                motorType = XL_320;
                break;
            case 5:
                motorType = MX_28;
                break;
            default:
                motorType = XL_320;
                break;
        }

        return motorType;
    }
}
