package org.daistudy.demo.main;

import org.daistudy.demo.config.JavaConfig;
import org.daistudy.demo.service.IUserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
		IUserService userServiceX = ctx.getBean("userServiceX", IUserService.class);
		IUserService userServiceY = ctx.getBean("userServiceY", IUserService.class);
	}
}
