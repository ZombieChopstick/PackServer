package com.rh.kl.pack.server.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.rh.kl.pack.server.exception.MessageException;
import com.rh.kl.pack.server.manager.OrchestrateManager;
import com.rh.kl.pack.server.manager.RedisManager;
import com.rh.kl.pack.server.model.User;
import com.rh.kl.pack.server.request.Credentials;
import com.rh.kl.pack.server.response.Message;

@Controller
public class AuthenticateController {
	
	@Autowired
	private OrchestrateManager db;
	
	@Autowired
	private RedisManager redisManager;
	
	private Log log = LogFactory.getLog(AuthenticateController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User login(HttpServletRequest request, HttpServletResponse response, @RequestBody Credentials credentials) throws Exception {
		User user = db.getUser(credentials.getUsername(), credentials.getPassword());
		
		if(user == null) {
			throw new MessageException(1002, "User not found.");
		}
		
		Jedis jedis = new Jedis("localhost");
		String authToken = UUID.randomUUID().toString();
		jedis.set(authToken, user.getUserId());
		jedis.expire(authToken, 1800);
		response.addHeader("Authentication-Token", authToken);
		
		return user;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Message logout(HttpServletRequest request) throws Exception {
		
		Jedis jedis = new Jedis("localhost");
		jedis.expire(request.getHeader("Authentication-Token"), 1);
		
		return new Message("Logout successful.");
	}
}
