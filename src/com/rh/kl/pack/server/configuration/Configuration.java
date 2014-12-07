package com.rh.kl.pack.server.configuration;

import java.io.FileReader;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class Configuration implements InitializingBean {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private Properties properties = new Properties();
	
	@Getter @Setter private String configFile;
	
	private Log log = LogFactory.getLog(Configuration.class);
	
	public Configuration() {

	}
	
	public String getPropertyValue(String key) {
		return properties.getProperty(key);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = applicationContext.getResource("classpath:" + configFile);
		properties.load(new FileReader(resource.getFile()));
		log.trace("Loaded configuration file.");
	}
}
