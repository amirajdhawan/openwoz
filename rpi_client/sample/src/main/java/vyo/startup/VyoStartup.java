package vyo.startup;

import openwoz.rpi.client.OpenWoz;
import vyo.devices.motor.dynamixel.DynamixelMotorType;
import vyo.devices.motor.helper.MotorControllerConfig;

public class VyoStartup {
	public static void main(String args[]){
		
		Thread openWozThread  = null;
		try{
			
			openWozThread = new Thread(new Runnable(){
				@Override
				public void run() {
					OpenWoz openWozClient = new OpenWoz();
					openWozClient.start();
				}
			});
			openWozThread.start();
			MotorControllerConfig.initMotorConfig(DynamixelMotorType.MX_64, VyoConstants.MOTOR_CONFIG_LOC);
			openWozThread.join();
		}
		catch(Exception ex){
			if(openWozThread != null)
				openWozThread.interrupt();
		}
	}
}
