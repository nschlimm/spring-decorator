package com.schlimm.decorator.resolver.longtwochains;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

@Decorator
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
