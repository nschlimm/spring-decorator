package com.schlimm.decorator.chained;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

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
