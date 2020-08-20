package redis;

import redis.clients.jedis.Jedis;

public class RedisConnection {
	public static void main(String[] args) {
		Jedis conn = new Jedis("hadoop02", 6379);
		String ping = conn.ping();
		System.out.println(ping);
	}
}
