package com.schlimm.decorator.resolver.longtwochains;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

@Decorator
public class AnotherDecorator implements AnotherServiceInterface {
	
	@Delegate 
	private AnotherServiceInterface delegateInterface;

	@Override
	public AnotherServiceInterface getDelegateObject() {
		return delegateInterface;
	}

	@Override
	public String sayHello() {
		return delegateInterface.sayHello();
	}

}
