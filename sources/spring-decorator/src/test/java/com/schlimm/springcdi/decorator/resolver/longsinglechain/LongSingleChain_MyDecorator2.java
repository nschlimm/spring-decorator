package com.schlimm.springcdi.decorator.resolver.longsinglechain;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.context.annotation.Scope;

@Decorator
@Scope("session")
public class LongSingleChain_MyDecorator2 implements LongSingleChain_MyServiceInterface {
	
	@Delegate 
	private LongSingleChain_MyServiceInterface delegateInterface;

	@Override
	public LongSingleChain_MyServiceInterface getDelegateObject() {
		return delegateInterface;
	}

	@Override
	public String sayHello() {
		return delegateInterface.sayHello();
	}


}
