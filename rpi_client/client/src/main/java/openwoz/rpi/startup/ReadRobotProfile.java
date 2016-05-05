package openwoz.rpi.startup;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import openwoz.rpi.dataobjects.RobotProfile;
import openwoz.rpi.utilities.UserConstants;

public class ReadRobotProfile {
	
	public static RobotProfile robotProfile;
	
	public static void readRobotProfile(){
		try{
			ObjectMapper mapper = new ObjectMapper();
			robotProfile = mapper.readValue(new File(UserConstants.PROFILE_LOCATION), RobotProfile.class);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
