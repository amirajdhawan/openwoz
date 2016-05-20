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
	
	public RobotEvent(){
		
	}
	
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