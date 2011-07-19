package com.schlimm.decorator.aop;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class MyDelegate implements MyServiceInterface {

	@Override
	public String getDelegateHello() {
		// TODO Auto-generated method stub
		return "Hello!";
	}

	@Override
	public MyServiceInterface getDelegateObject() {
		return this;
	}

}
