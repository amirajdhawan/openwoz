package vyo.devices.motor.helper;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import vyo.devices.motor.dynamixel.conn.rpi.DynamixelPiMotorController;

public class MotorControllerConfig {
	private static GpioController gpioCtrl;
	private static GpioPinDigitalOutput comLock;
	public static DynamixelPiMotorController motorConfig;
	
	public static void initMotorConfig(int motorType, String motorConfigLoc){
		initSemaphore();
		motorConfig = new DynamixelPiMotorController(comLock, motorType, motorConfigLoc);
		//contr.moveMotor("firstmotor", 1.6F);
	}
	private static GpioPinDigitalOutput initSemaphore() {
        // create gpio controller
        gpioCtrl = GpioFactory.getInstance();
        comLock = null;

        if (!gpioCtrl.getProvisionedPins().contains(RaspiPin.GPIO_01)) {
            // provision pi4j gpio pin #01 (normal = gpio18) as an output semaphore pin
            // and turn it off (unlocked)
            comLock = gpioCtrl.provisionDigitalOutputPin(RaspiPin.GPIO_01, "ComLock", PinState.LOW);

            // setColor shutdown state for this pin
            comLock.setShutdownOptions(true, PinState.LOW);
        } else {
            System.out.println("FATAL! IllegalAccessException: comLock pin (GPIO18) is unavailable");
            terminate();
        }

        return comLock;
    }
	
	public static void terminate() {
        System.out.println("Shutting down gracefully");

        gpioCtrl.shutdown();
        System.exit(0);
    }
}