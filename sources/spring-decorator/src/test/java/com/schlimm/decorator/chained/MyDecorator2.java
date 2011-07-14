package com.schlimm.decorator.chained;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import com.schlimm.decorator.simple.MyDelegate;
import com.schlimm.decorator.simple.MyServiceInterface;

@Decorator
public class MyDecorator2 implements MyServiceInterface {
	
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
