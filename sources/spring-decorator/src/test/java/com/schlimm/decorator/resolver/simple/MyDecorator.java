package com.schlimm.decorator.resolver.simple;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

@Decorator
public class MyDecorator implements MyServiceInterface {
	
	@Delegate 
	private MyServiceInterface delegateInterface;

	public void setDelegateInterface(MyServiceInterface serviceDelegate) {
		this.delegateInterface = serviceDelegate;
	}

	public MyServiceInterface getDelegateInterface() {
		System.out.println("In Decorator 1");
		return delegateInterface;
	}

}
