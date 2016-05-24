package vyo.startup;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class PinConfig {
	public static GpioPinDigitalOutput led1 = null;
	
	public static void config(){
		final GpioController gpio = GpioFactory.getInstance();
		led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
	}
	
}
