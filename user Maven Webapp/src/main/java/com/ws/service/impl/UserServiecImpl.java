package com.ws.service.impl;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.ws.service.UserServiceI;
@WebService
public class UserServiecImpl  implements UserServiceI{
	Logger logger=Logger.getLogger(UserServiecImpl.class);
	
	@Override
	public String login(String name) {
		this.logger.info("正在登录。。。。");
		this.logger.debug("正在登录。。。。");
		this.logger.error("正在登录。。。。");
		
		return "恭喜"+name+"用户：登录成功！";
	}

}
