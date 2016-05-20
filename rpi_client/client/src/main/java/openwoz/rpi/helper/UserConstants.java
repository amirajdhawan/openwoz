package openwoz.rpi.helper;

/**
 * Class which defines static user definec constants.
 * This file might be modified to automatically on application startup read these values from a
 * config file and populated here.
 * 
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class UserConstants {
	
	//Robot Profile name which also doubles up as the channel name where Jedis must listen for messages
	public static String PROFILE_CHANNEL = "profile1";
	
	//IP address of the redis server
	public static String REDIS_SERVER = "54.149.230.9";
	
	//Port number of the redis server
	public static int REDIS_PORT = 6379;
	
	//Location where the robot profile can be read from 
	public static String PROFILE_LOCATION = "resources/profile.js";
	
	//Location from where the device config file can be read from
	public static String DEVICE_CONFIG_LOCATION = "resources/devices.js";
}
