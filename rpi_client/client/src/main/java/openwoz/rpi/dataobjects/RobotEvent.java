package openwoz.rpi.dataobjects;

public class RobotEvent {
	String name;
	String deviceName;
	double value;
	
	public RobotEvent(){
		
	}
	
	public RobotEvent(String name, String device, double start){
		this.name = name;
		this.deviceName = device;
		this.value = start;
	}
	
	//public RobotEvent(String device, double end){
		//this.eventName = event;
	//	this.deviceName = device;
	//}
/*
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}*/

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceType) {
		this.deviceName = deviceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
