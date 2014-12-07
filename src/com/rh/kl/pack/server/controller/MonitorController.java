package com.rh.kl.pack.server.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rh.kl.pack.server.response.Message;

@Controller
public class MonitorController {
	
	@RequestMapping(value = "/monitor", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Message monitor() {
		return new Message("PackServer Online.");
	}
}
