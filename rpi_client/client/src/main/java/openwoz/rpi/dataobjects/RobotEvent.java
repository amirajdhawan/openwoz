package openwoz.rpi.dataobjects;

/**
 * Class which defines the data structure of a robot event
 * 
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class RobotEvent {
	
	//Name of the event
	String name;
	String className;
	String methodName;
	//Name of the device which is to be modified in this event
	//String deviceName;
	//Value of the device to be communicated
	//double value;
	
	public RobotEvent(){
		
	}
	
	/*public RobotEvent(String name, String device, double start){
		this.name = name;
		this.deviceName = device;
		this.value = start;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceType) {
		this.deviceName = deviceType;
	}*/
	
	
	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setName(String name) {
		this.name = name;
	}
}