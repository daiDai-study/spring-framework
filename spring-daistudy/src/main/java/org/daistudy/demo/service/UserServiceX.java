package org.daistudy.demo.service;

import org.springframework.stereotype.Component;

@Component
public class UserServiceX implements IUserService {

	public UserServiceX(){
		System.out.println("init UserServiceX");
	}
}
