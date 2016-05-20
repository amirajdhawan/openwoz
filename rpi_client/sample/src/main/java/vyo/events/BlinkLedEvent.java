package vyo.events;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class BlinkLedEvent {
	public void blinkLed() throws InterruptedException{
		final GpioController gpio = GpioFactory.getInstance();
	        
        // provision gpio pin #01 & #03 as an output pins and blink
        final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);

        // create and register gpio pin listener
        int i = 0;
        while(i < 10){
        	led1.setState(PinState.HIGH);
        	Thread.sleep(250);
        	led1.setState(PinState.LOW);
        	Thread.sleep(250);
        	i++;
        }
	}
}
