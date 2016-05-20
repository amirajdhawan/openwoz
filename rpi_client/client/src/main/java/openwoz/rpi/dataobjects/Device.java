package openwoz.rpi.dataobjects;

/**
 * Class which defines the data structure of a device connected to the robot
 * Not used as of now and bound to be deleted or heavily modified.
 * 
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class Device {
	
	//Communication channel where the device resides
	String commChannel;
	
	//Device id of this device
	String deviceId;
	
	public Device(){
		
	}
	
	public String getCommChannel() {
		return commChannel;
	}
	public void setCommChannel(String communicationChannel) {
		this.commChannel = communicationChannel;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
