package com.schlimm.decorator.aop;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.context.annotation.Scope;

@Decorator
public class MyDecorator implements MyServiceInterface {
	
	@Delegate 
	private MyServiceInterface delegateInterface;

	@Override
	public MyServiceInterface getDelegateObject() {
		return delegateInterface;
	}

	@Override
	public String getDelegateHello() {
		return delegateInterface.getDelegateHello();
	}

}
