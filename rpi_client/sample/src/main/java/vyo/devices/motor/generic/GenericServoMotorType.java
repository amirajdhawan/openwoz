package vyo.devices.motor.generic;

/**
 * generic pwm servo motor type definition
 *
 * @author Benny on 3/18/2016.
 */
public class GenericServoMotorType {
    public static final int MG_996R = 0;
    public static final int MG_995 = 1;
    public static final int MG_945 = 2;


    public static int getMotorType(String motorTypeName) {
        int motorType = -1;     // Unknown motor

        switch (motorTypeName.toUpperCase()) {
            case "MG_996R":
                motorType = MG_996R;
                break;
            case "MG_995":
                motorType = MG_995;
                break;
            case "MG_945":
                motorType = MG_945;
                break;
        }

        return motorType;
    }
}
