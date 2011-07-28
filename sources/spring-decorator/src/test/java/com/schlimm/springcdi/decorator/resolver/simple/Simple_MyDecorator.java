package com.schlimm.springcdi.decorator.resolver.simple;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

@Decorator
public class Simple_MyDecorator implements Simple_MyServiceInterface {
	
	@Delegate 
	private Simple_MyServiceInterface delegateInterface;

	public void setDelegateInterface(Simple_MyServiceInterface serviceDelegate) {
		this.delegateInterface = serviceDelegate;
	}

	public Simple_MyServiceInterface getDelegateInterface() {
		System.out.println("In Decorator 1");
		return delegateInterface;
	}

}
