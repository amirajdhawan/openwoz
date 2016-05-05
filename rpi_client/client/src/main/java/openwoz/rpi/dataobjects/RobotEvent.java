package openwoz.rpi.dataobjects;

public class RobotEvent {
	String deviceName;
	double startValue;
	double endValue;
	
	public RobotEvent(){
		
	}
	
	public RobotEvent(String device, double start, double end){
		//this.eventName = event;
		this.deviceName = device;
		this.startValue = start;
		this.endValue = end;
	}
	
	public RobotEvent(String device, double end){
		//this.eventName = event;
		this.deviceName = device;
		this.endValue = end;
	}
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

	public double getStartValue() {
		return startValue;
	}

	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}

	public double getEndValue() {
		return endValue;
	}

	public void setEndValue(double endValue) {
		this.endValue = endValue;
	}
}
