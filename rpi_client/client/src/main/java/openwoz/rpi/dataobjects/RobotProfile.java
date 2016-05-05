package openwoz.rpi.dataobjects;

import java.util.HashMap;
import java.util.Map;

public class RobotProfile {
	String profileName;
	String purpose;
	String platform;
	
	Map<String, RobotEvent> events;
	Map<String, RobotSequence> sequences;
	
	public RobotProfile(){
		events = new HashMap<String, RobotEvent>();
		sequences = new HashMap<String, RobotSequence>();
	}
	
	public Map<String, RobotSequence> getSequences() {
		return sequences;
	}

	public void setSequences(Map<String, RobotSequence> sequences) {
		this.sequences = sequences;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Map<String, RobotEvent> getEvents() {
		return events;
	}

	public void setEvents(Map<String, RobotEvent> events) {
		this.events = events;
	}
	
}
