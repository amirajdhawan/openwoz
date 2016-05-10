package openwoz.rpi.comm;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import openwoz.rpi.dataobjects.Device;
import openwoz.rpi.dataobjects.RobotEvent;
import openwoz.rpi.helper.UserConstants;
import openwoz.rpi.startup.MotorControllerConfig;
import redis.clients.jedis.JedisPubSub;

public class RobotProfileSubscriber extends JedisPubSub {

	private static Logger logger = LoggerFactory.getLogger(RobotProfileSubscriber.class);
	private static String loggingPrefix = "";

	public RobotProfileSubscriber(){
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.info(loggingPrefix  + " onUnsubscribe function");
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		logger.info(loggingPrefix + "onSubscribe");
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
	}

	@Override
	public void onMessage(String channel, String message) {
		System.out.println(loggingPrefix + "Message received: " + message);
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			RobotEvent event = mapper.readValue(message, RobotEvent.class);
			
			MotorControllerConfig.motorConfig.moveMotor(event.getDeviceName(), (float)event.getValue());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	/*public boolean setupSubscriber(){

			new Thread(new Runnable() {

				@Override
				public void run() {
					Jedis jedis = null;
					try {
						System.out.println("Connecting");
						jedis = new Jedis(server);
						System.out.println("subscribing");
						jedis.subscribe(jedisPubSub, profileCh);
					} catch (Exception e) {
						System.out.println(">>> Sub - " + e.getMessage());
					}
					finally{
						if(jedis != null)
							jedis.close();
					}
				}
			}, "subscriberThread").start();
		}*/
}
