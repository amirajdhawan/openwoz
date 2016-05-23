package vyo.startup;

import openwoz.rpi.client.OpenWoz;
import vyo.devices.motor.dynamixel.DynamixelMotorType;
import vyo.devices.motor.helper.MotorControllerConfig;

public class VyoStartup {
	public static void main(String args[]){
		OpenWoz openWozClient = null;
		
		try{
			MotorControllerConfig.initMotorConfig(DynamixelMotorType.MX_64, VyoConstants.MOTOR_CONFIG_LOC);
			String redisIP = "54.200.150.195";
			String redisPass = "";
			int redisPort = 6379;
			String profileLoc = "resources/vyo_robot.js";
			
			//OpenWoz start should be the last call in your main function 
			openWozClient = new OpenWoz();
			openWozClient.start(profileLoc, redisIP, redisPort, redisPass);
		}
		catch(Exception ex){
			
		}
		finally{
			if(openWozClient != null)
				openWozClient.shutdown();
		}
	}
}
