package com.schlimm.springcdi.decorator.resolver.longtwoqualified;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import com.schlimm.springcdi.decorator.resolver.longsinglechain.LongSingleChain_MyServiceInterface;

@Decorator
@Scope("session")
@Qualifier("another")
public class LongTwoQualified_AnotherDecorator2 implements LongSingleChain_MyServiceInterface {
	
	@Delegate @Qualifier("another")
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
