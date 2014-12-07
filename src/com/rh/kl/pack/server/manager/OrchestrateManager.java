package com.rh.kl.pack.server.manager;

import io.orchestrate.client.Client;
import io.orchestrate.client.KvList;
import io.orchestrate.client.KvMetadata;
import io.orchestrate.client.KvObject;
import io.orchestrate.client.OrchestrateClient;
import io.orchestrate.client.OrchestrateRequest;
import io.orchestrate.client.ResponseAdapter;
import io.orchestrate.client.SearchResults;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.rh.kl.pack.server.configuration.Configuration;
import com.rh.kl.pack.server.model.Pack;
import com.rh.kl.pack.server.model.Packs;
import com.rh.kl.pack.server.model.User;
import com.rh.kl.pack.server.response.Message;

public class OrchestrateManager implements InitializingBean {
	@Getter private final String API_KEY = "API_KEY";
	private Client client;
	
	private Log log = LogFactory.getLog(OrchestrateManager.class);
	
	@Autowired
	private Configuration orchestrateConfiguration;
	
	public OrchestrateManager() {
	}
	
	public boolean purchasePack(Pack pack, String userId) {
		User user = getUserById(userId);
		user.setUserId(null);
		user.setGold(user.getGold() - pack.getPrice());
		String currentRef = client.kv("Users", userId).get(User.class).get().getRef();
		KvMetadata success = client.kv("Users", userId).put(user).get();
		return(!success.getRef().equals(currentRef));
	}
	
	public Pack getPack(String collection, final String key) {
		log.trace("Calling GET /pack/id");
		OrchestrateRequest<KvObject<Pack>> res = client.kv("Packs", key)
	      .get(Pack.class)
	      .on(new ResponseAdapter<KvObject<Pack>>() {
	          @Override
	          public void onFailure(final Throwable error) {
	              // handle errors
	          }

	          @Override
	          public void onSuccess(final KvObject<Pack> object) {
	              if (object == null) {
	                  // we received a 404, no KV object exists
	            	  log.error("404 No Object Exists");
	              }

	              object.getValue().setId(Integer.parseInt(key));
	          }
	      });
		if(res.get() == null) {
			return null;
		} else {
			return res.get().getValue();
		}
	}
	
	public Packs getPacks() {
		List<Pack> packs = new ArrayList<Pack>();
		KvList<Pack> results =
		        client.listCollection("Packs")
		              .get(Pack.class)
		              .get();
		Iterator<KvObject<Pack>> it = results.iterator();
		for (KvObject<Pack> kvObject : results) {
			kvObject.getValue().setId(Integer.parseInt(it.next().getKey()));
			packs.add(kvObject.getValue());
		}
		
		Packs availablePacks = new Packs(packs);
		return availablePacks;
	}
	
	public User getUser(String username, String password) {
		SearchResults<User> results =
		        client.searchCollection("Users").get(User.class, "username:`" + username + "`&&password:`" + password + "`").get();
		if(results.getCount() == 1) {
			User user = results.iterator().next().getKvObject().getValue();
			user.setUserId("1");
			return user;
		} else {
			return null;
		}
	}
	
	public User getUserById(final String id) {
		OrchestrateRequest<KvObject<User>> res = client.kv("Users", id)
				.get(User.class).on(new ResponseAdapter<KvObject<User>>() {
					@Override
					public void onFailure(final Throwable error) {
						// handle errors
					}

					@Override
					public void onSuccess(final KvObject<User> object) {
						if (object == null) {
							// we received a 404, no KV object exists
							log.error("404 No Object Exists");
						}

						object.getValue().setUserId(id);
					}
				});
		if (res.get() == null) {
			return null;
		} else {
			return res.get().getValue();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.trace("API KEY: " + orchestrateConfiguration.getPropertyValue("API_KEY"));
		String apiKey = orchestrateConfiguration.getPropertyValue(API_KEY);
		client = OrchestrateClient.builder(apiKey)
				.host("https://api.aws-eu-west-1.orchestrate.io")
				.build();
	}
}
