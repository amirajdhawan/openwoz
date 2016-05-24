package openwoz.rpi.startup;

/*
 * Reads the device configuration file and stores it in a hashmap for easy access for other functions.
 * 
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class ReadDeviceMapping {
	/*public static HashMap<String, Device> deviceMapping;
	
	*//**
	 * Reads the device configuration file from the location pointed to by DEVICE_CONFIG_LOCATION,
	 * uses jackson to parse the json file and sets up the class level static hashmap deviceMapping 
	 * which maps DEVICE_NAME -> DEVICE Object
	 * 
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 *//*
	public static void readDeviceConf(){
		try{
			ObjectMapper mapper = new ObjectMapper();
			deviceMapping = mapper.readValue(new File(UserConstants.DEVICE_CONFIG_LOCATION), 
					new TypeReference<HashMap<String, Device>>(){});
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}*/
}
