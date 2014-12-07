package com.rh.kl.pack.server.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rh.kl.pack.server.exception.AuthenticationException;
import com.rh.kl.pack.server.exception.MessageException;
import com.rh.kl.pack.server.manager.OrchestrateManager;
import com.rh.kl.pack.server.manager.RedisManager;
import com.rh.kl.pack.server.model.Pack;
import com.rh.kl.pack.server.model.Packs;
import com.rh.kl.pack.server.model.User;
import com.rh.kl.pack.server.response.Message;

@Controller
@RequestMapping("/shop")
public class ShopController {
	
	@Autowired
	private OrchestrateManager db;
	
	@Autowired
	private RedisManager redisServer;
	
	@RequestMapping(value = "/packs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Packs getPacks() {
		Properties prop = new Properties();
		return db.getPacks();
	}
	
	@RequestMapping(value = "/pack/{packId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody Message purchase(HttpServletRequest request, @PathVariable("packId") Integer packId) throws Exception {
		Pack pack =  db.getPack("Packs", "" + packId);
		
		if(pack == null) {
			throw new MessageException(1001, "Unable to retrieve pack.");
		}
		
		String userId = redisServer.getAuthenticatedUserId(request.getHeader("Authentication-Token"));
		
		if(userId == null) {
			throw new AuthenticationException(1003, "Invalid credentials. Please login before attempting to make a purchase.");
		}
		
		User user = db.getUserById(userId);
		
		if(user == null) {
			throw new MessageException(1002, "User not found.");
		}
		
		if(user.getGold() >= pack.getPrice()) {
			if(db.purchasePack(pack, userId)) {
				return new Message("You successfully purchased the #" + pack.getId() + " " + pack.getName() + " pack. You can now find this in your inventory.");
			} else {
				return new Message("There was an issue in purchasing this pack. Please try again later.");
			}
		} else {
			return new Message("You do not have sufficient gold to purchase this pack.");
		}
	}
}
