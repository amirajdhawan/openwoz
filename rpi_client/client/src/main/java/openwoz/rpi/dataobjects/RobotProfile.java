package openwoz.rpi.dataobjects;

import java.util.HashMap;
import java.util.Map;

/**
 * Class which defines the data structure of a robot profile
 * 
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class RobotProfile {
	
	//Name of the profile
	String profileName;
	//Name of the channel
	String channelName;
	String profileKey;
	String image;
	//Purpose of the robot profile
	String purpose;
	//Platform supported by this robot profile
	String platform;
	
	//Map of events that this robot profile defines
	Map<String, RobotEvent> events;
	
	//Map of events that this robot profile defines
	Map<String, RobotSequence> sequences;
	
	public RobotProfile(){
		events = new HashMap<String, RobotEvent>();
		sequences = new HashMap<String, RobotSequence>();
	}
	
	public String getProfileKey() {
		return profileKey;
	}

	public void setProfileKey(String profileKey) {
		this.profileKey = profileKey;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Map<String, RobotSequence> getSequences() {
		return sequences;
	}

	public void setSequences(Map<String, RobotSequence> sequences) {
		this.sequences = sequences;
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
