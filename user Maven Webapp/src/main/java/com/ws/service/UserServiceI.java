package com.ws.service;

import javax.jws.WebService;

@WebService
public interface UserServiceI {
	
	String login(String name);

}
