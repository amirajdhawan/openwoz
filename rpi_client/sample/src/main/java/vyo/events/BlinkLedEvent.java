package vyo.events;

import com.pi4j.io.gpio.PinState;

import vyo.startup.PinConfig;

public class BlinkLedEvent {
	public void blinkLed() throws InterruptedException{
        int i = 0;
        while(i < 10){
        	PinConfig.led1.setState(PinState.HIGH);
        	Thread.sleep(250);
        	PinConfig.led1.setState(PinState.LOW);
        	Thread.sleep(250);
        	i++;
        }
	}
}
