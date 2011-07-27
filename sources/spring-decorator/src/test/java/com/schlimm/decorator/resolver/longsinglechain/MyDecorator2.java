package com.schlimm.decorator.resolver.longsinglechain;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.context.annotation.Scope;

@Decorator
@Scope("session")
public class MyDecorator2 implements MyServiceInterface {
	
	@Delegate 
	private MyServiceInterface delegateInterface;

	@Override
	public MyServiceInterface getDelegateObject() {
		return delegateInterface;
	}

	@Override
	public String sayHello() {
		return delegateInterface.sayHello();
	}


}
