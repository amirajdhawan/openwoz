package openwoz.rpi.startup;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import openwoz.rpi.dataobjects.RobotProfile;

/**
 * Reads the robot profile configuration file and stores as an object of class RobotProfile
 * 
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class ReadRobotProfile {
	
	public static RobotProfile robotProfile;
	
	/**
	 * Reads the robot profile configuration file from the location pointed to by PROFILE_LOCATION,
	 * uses jackson to parse the json file and sets up a class level static object of class RobotProfile
	 * 
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 */
	public static void readRobotProfile(File robotProfileFile){
		try{
			ObjectMapper mapper = new ObjectMapper();
			robotProfile = mapper.readValue(robotProfileFile, RobotProfile.class);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
