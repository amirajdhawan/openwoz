package openwoz.rpi.comm;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import openwoz.rpi.dataobjects.RobotEvent;
import redis.clients.jedis.JedisPubSub;

/**
 * Class which describes all the event handlers for the Jedis Server
 * 
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class RobotProfileSubscriber extends JedisPubSub {
	
	private static Logger logger = LoggerFactory.getLogger(RobotProfileSubscriber.class);
	private static String loggingPrefix = "";

	public RobotProfileSubscriber(){
	}

	/**
	 * Event handler for onUnsubscribe
	 * 
	 * @param String channel: Name of the channel
	 * @param int subscribedChannels: total number of subscribed channels
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 */
	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.info(loggingPrefix  + " onUnsubscribe function");
	}
	
	/**
	 * Event handler for onSubscribe
	 * 
	 * @param String channel: Name of the channel
	 * @param int subscribedChannels: total number of subscribed channels
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 */
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		logger.info(loggingPrefix + "onSubscribe");
	}
	
	/**
	 * Event handler for onPUnsubscribe
	 * 
	 * @param String pattern: pattern matched against a channel
	 * @param int subscribedChannels: total number of subscribed channels
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 */
	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
	}

	/**
	 * Event handler for onPSubscribe
	 * 
	 * @param String pattern: pattern matched against a channel
	 * @param int subscribedChannels: total number of subscribed channels
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 */
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
	}

	/**
	 * Event handler for onPMessage
	 * 
	 * @param String pattern: pattern matched against a channel
	 * @param String channel: name of the channel
	 * @param String message: the actual message received
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 */
	@Override
	public void onPMessage(String pattern, String channel, String message) {
	}

	/**
	 * Event handler for onMessage. Executes the code to be executed when a message on the channel is
	 * received. Currently moves the first motor in the motor configuration with the value received.
	 * In the future, this will do a reflection based function call based on an event.
	 * 
	 * @param String channel: name of the channel
	 * @param String message: the actual message received
	 * @return void
	 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
	 */
	@Override
	public void onMessage(String channel, String message) {
		logger.info(loggingPrefix + "Message received: " + message);
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			RobotEvent event = mapper.readValue(message, RobotEvent.class);
			
			Class<?> classInstance = Class.forName(event.getClassName());
			Object objectReflect = classInstance.newInstance();
			Method methodInstance = classInstance.getDeclaredMethod(event.getMethodName());
			
			logger.info(loggingPrefix + "Invoking method: " + event.getMethodName() + " from class: " + event.getClassName());
			methodInstance.invoke(objectReflect);
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}	
	}
}
