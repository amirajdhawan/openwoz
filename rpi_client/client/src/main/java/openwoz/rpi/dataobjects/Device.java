package openwoz.rpi.dataobjects;

public class Device {
	
	String commChannel;
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
