package vyo.devices.motor.generic.conn.rpi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import vyo.devices.motor.dynamixel.DynamixelUtil;
import vyo.devices.motor.generic.GenericServoMotor;
import vyo.devices.motor.helper.Interpolator;

/**
 * Control all the basic functionalities of a generic pwm servo motor
 * generic pwm servo motor does not need a motor controller
 *
 * @author Benny on 3/18/2016.
 */
public class GenericPiServoMotor extends GenericServoMotor {
    private GpioPinPwmOutput mMotorGpioPin;

    // todo - maybe add motor controller for consistency

    public GenericPiServoMotor(int id, int motorType, GpioController gpioController, Pin pin) {
        super(id, motorType);

        // provision gpio pin as an pwm output pin
        mMotorGpioPin = gpioController.provisionPwmOutputPin(pin, pin.getName(), 0);

        // set shutdown state for this pin
        mMotorGpioPin.setShutdownOptions(true, PinState.LOW);
    }

    @Override
    protected void move(int angle) {
        int cappedAngle = capAngle(angle);
        int dutyCycle = (int)map(cappedAngle, -mMaxAngle, mMaxAngle, (int)getMinDutyCycle(), (int)getMaxDutyCycle());

        mMotorGpioPin.setPwm(dutyCycle);
    }

    @Override
    protected void move(int angle, int function) {
        move(angle, function, Interpolator.DEFAULT_SAMPLER, 1);
    }

    @Override
    protected void move(int angle, int function, int numOfSamples) {
        move(angle, function, numOfSamples, 1);
    }

    @Override
    protected void move(int angle, int function, int numOfSamples, final int durationMultiplier) {
        int cappedAngle = capAngle(angle);

        // Generate interpolation table
        final float[] interpolationTable = Interpolator.interpolate(mCurrentPosition, cappedAngle, function, numOfSamples);

        Thread movementThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (float value : interpolationTable) {
                    int dutyCycle = (int)map((int)value, -mMaxAngle, mMaxAngle, (int)getMinDutyCycle(), (int)getMaxDutyCycle());
                    mMotorGpioPin.setPwm(dutyCycle);
                    DynamixelUtil.sleep(durationMultiplier);
                }
            }
        });

        movementThread.start();
    }

    private int capAngle(int angle) {
        int cappedAngle = Math.min(mMaxAngle, Math.max(0, angle));

        if (D) System.out.println("Capped angle " + angle + " to " + cappedAngle);

        return cappedAngle;
    }
}
