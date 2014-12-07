package com.rh.kl.pack.server.manager;

import org.springframework.beans.factory.InitializingBean;

import com.rh.kl.pack.server.model.User;

import redis.clients.jedis.Jedis;

public class RedisManager implements InitializingBean {
	
	private Jedis redisServer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		redisServer = new Jedis("localhost");
	}
	
	public String getAuthenticatedUserId(String authenticationToken) {
		if(authenticationToken == null) return null;
		return redisServer.get(authenticationToken);
	}

}
