package vyo.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import openwoz.rpi.comm.RobotProfileSubscriber;
import vyo.devices.motor.helper.MotorControllerConfig;

public class HeadNodEvent {
	
	private static Logger logger = LoggerFactory.getLogger(RobotProfileSubscriber.class);
	private static String loggingPrefix = "";
	
	public void nodHead(){
		
		logger.info(loggingPrefix + "Got call for nodHead method!");
		try{
			int i = 0;
			while(i < 5){
				System.out.println(loggingPrefix + "nodHead moving motors " + i + " times!");
				MotorControllerConfig.motorConfig.moveMotor("headmotor", 0.1F);
				MotorControllerConfig.motorConfig.moveMotor("neckmotor", 1.0F);
				Thread.sleep(1500);
				MotorControllerConfig.motorConfig.moveMotor("headmotor", 1.5F);
				MotorControllerConfig.motorConfig.moveMotor("neckmotor", -0.2F);
				Thread.sleep(1500);
				i++;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
 