package com.schlimm.springcdi.decorator.resolver.longtwochains;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.context.annotation.Scope;

@Decorator
@Scope("session")
public class LongTwoChains_AnotherDecorator2 implements LongTwoChains_AnotherServiceInterface {
	
	@Delegate 
	private LongTwoChains_AnotherServiceInterface delegateInterface;

	@Override
	public LongTwoChains_AnotherServiceInterface getDelegateObject() {
		return delegateInterface;
	}

	@Override
	public String sayHello() {
		return delegateInterface.sayHello();
	}


}
