package org.daistudy.demo.service;

import org.springframework.stereotype.Component;

@Component
public class UserServiceY implements IUserService {

	public UserServiceY(){
		System.out.println("init UserServiceY");
	}
}
