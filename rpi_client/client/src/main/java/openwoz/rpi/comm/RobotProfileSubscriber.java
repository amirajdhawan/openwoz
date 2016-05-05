package openwoz.rpi.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
