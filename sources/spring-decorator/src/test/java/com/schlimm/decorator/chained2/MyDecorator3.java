package com.schlimm.decorator.chained2;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.context.annotation.Scope;

@Decorator
@Scope("session")
public class MyDecorator3 implements MyServiceInterface {
	
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
