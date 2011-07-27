package com.schlimm.springcdi.decorator.resolver.longtwoqualified;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.factory.annotation.Qualifier;

import com.schlimm.springcdi.decorator.resolver.longsinglechain.MyServiceInterface;

@Decorator
@Qualifier("my")
public class MyDecorator2 implements MyServiceInterface {
	
	@Delegate @Qualifier("my")
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
