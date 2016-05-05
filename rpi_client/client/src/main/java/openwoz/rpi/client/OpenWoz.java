package openwoz.rpi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import openwoz.rpi.comm.RobotProfileSubscriber;
import openwoz.rpi.startup.ReadDeviceMapping;
import openwoz.rpi.startup.ReadRobotProfile;
import openwoz.rpi.utilities.UserConstants;
import redis.clients.jedis.Jedis;

/**
 *
 */
public class OpenWoz
{
	private static Logger logger = LoggerFactory.getLogger(OpenWoz.class);

	public static void main(String[] args)
	{
		Thread subsTh = null;
		
		try{
			logger.info("Read robot profiles");
			ReadRobotProfile.readRobotProfile();
			
			logger.info("Read device configuration");
			ReadDeviceMapping.readDeviceConf();
			
			logger.info("Jedis setup done");
			subsTh = new Thread(new Runnable() {
				 
				@Override
				public void run() {
					Jedis jedServer = null;
					try {
						if(!Thread.interrupted()){
							logger.info("Subscribing to \"commonChannel\". This thread will be blocked.");
							final RobotProfileSubscriber subscriber = new RobotProfileSubscriber();
							jedServer = new Jedis(UserConstants.REDIS_SERVER, UserConstants.REDIS_PORT);
							jedServer.subscribe(subscriber, UserConstants.PROFILE_CHANNEL);
							logger.info("Subscription ended.");
						}
					}
					catch (Exception e) {
						logger.error("Subscribing failed.", e);
					}
					finally{
						if(jedServer != null)
							jedServer.close();
					}
				}
			});
			subsTh.start();
			System.out.println("Joining now");
			subsTh.join();
		}
		catch(InterruptedException ie){
			if(subsTh != null)
				subsTh.interrupt();
		}
	}
}
