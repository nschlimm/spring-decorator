package com.schlimm.decorator.resolver.longtwochains;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.context.annotation.Scope;

@Decorator
@Scope("session")
public class AnotherDecorator2 implements AnotherServiceInterface {
	
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
