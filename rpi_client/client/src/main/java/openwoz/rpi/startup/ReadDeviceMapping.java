package openwoz.rpi.startup;

import java.io.File;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import openwoz.rpi.dataobjects.Device;
import openwoz.rpi.helper.UserConstants;

public class ReadDeviceMapping {
	public static HashMap<String, Device> deviceMapping;
	
	public static void readDeviceConf(){
		try{
			ObjectMapper mapper = new ObjectMapper();
			deviceMapping = mapper.readValue(new File(UserConstants.DEVICE_CONFIG_LOCATION), 
					new TypeReference<HashMap<String, Device>>(){});
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
