package openwoz.rpi.vyo;

import openwoz.devices.motor.dynamixel.DynamixelMotorType;
import openwoz.rpi.startup.MotorControllerConfig;

public class VyoStartup {
	public static void setupVyoConfig(){
		MotorControllerConfig.initMotorConfig(DynamixelMotorType.MX_64, VyoConstants.MOTOR_CONFIG_LOC);
	}
}
