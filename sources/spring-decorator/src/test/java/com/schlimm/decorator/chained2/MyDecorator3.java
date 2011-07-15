package com.schlimm.decorator.chained2;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

@Decorator
public class MyDecorator3 implements MyServiceInterface {
	
	@Delegate 
	private MyServiceInterface delegateInterface;

	public void setDelegateClass(MyDelegate delegate) {
		
	}

	public MyDelegate getDelegateClass() {
		return null;
	}

	public void setDelegateInterface(MyServiceInterface serviceDelegate) {
		this.delegateInterface = serviceDelegate;
	}

	public MyServiceInterface getDelegateInterface() {
		System.out.println("In Decorator 2");
		delegateInterface.getDelegateClass();
		return delegateInterface;
	}

}
