package openwoz.rpi.client;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import openwoz.rpi.comm.RobotProfileSubscriber;
import openwoz.rpi.startup.ReadRobotProfile;
import redis.clients.jedis.Jedis;

/**
 * This is the main funciton of the OpenWoZ system. This function reads up the robot profiles,
 * device configuration, and initializes the motor configuration. It also starts up a Jedis instance
 * listening on a channel given by the robot profile name on a new thread.
 * 
 * If this function is stopped, it interrupts its child thread running the Jedis instance as well to 
 * ensure proper shutdown of the system
 * 
 * @param Array of command line parameters as strings
 * @return void
 * @author Amiraj Dhawan (amirajdhawan@gmail.com)
 */
public class OpenWoz
{
	private static Logger logger = LoggerFactory.getLogger(OpenWoz.class);
	private Thread subsTh = null;
	
	public void start(File robotProfileFile, final String redisIPAddr, final int redisPortNo, final String redisPassword)
	{
		try{
			//Read Robot profiles from configuration file
			logger.info("Read robot profiles");
			ReadRobotProfile.readRobotProfile(robotProfileFile);
			
			//Start a new thread to run a Jedis instance which listens on the topic robot_profile_name
			logger.info("Jedis setup started");
			subsTh = new Thread(new Runnable() {
				public void run() {
					Jedis jedServer = null;
					try {
						
						//If the thread is not interrupted
						if(!Thread.interrupted()){
							logger.info("Subscribing to \"commonChannel\". This thread will be blocked.");
							final RobotProfileSubscriber subscriber = new RobotProfileSubscriber();
							
							//Create a new instance of Jedis on the REDIS_SERVER and REDIS_PORT
							jedServer = new Jedis(redisIPAddr, redisPortNo);
							jedServer.auth(redisPassword);
							
							//Subscribe on the channel given by the robot_profile_name. This is a blocking 
							// function call and never returns back
							jedServer.subscribe(subscriber, ReadRobotProfile.robotProfile.getChannelName());
							logger.info("Subscription ended.");
						}
					}
					catch (Exception e) {
						logger.error("Subscribing failed.", e);
					}
					finally{
						//Finally when thread ends, close the jedis server instance
						if(jedServer != null)
							jedServer.close();
					}
				}
			});
			
			//Start the jedis thread
			subsTh.start();
			System.out.println("Joining now");
			subsTh.join();
		}
		catch(Exception ie){
			//if the main thread is interrupted for any reason, it interrupts the jedis thread too to ensure
			// that it closes the jedis server
			if(subsTh != null)
				subsTh.interrupt();
		}
	}
	
	public void shutdown(){
		if(subsTh != null)
			subsTh.interrupt();
	}
}
