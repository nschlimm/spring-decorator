package com.schlimm.decorator.resolver.longtwochains;

import org.springframework.beans.factory.annotation.Autowired;

public class SomeTestBean {
	
	@Autowired
	private AnotherServiceInterface decoratedInterface;

	public AnotherServiceInterface getDecoratedInterface() {
		return decoratedInterface;
	}

	public void setDecoratedInterface(AnotherServiceInterface decoratedInterface) {
		this.decoratedInterface = decoratedInterface;
	}

}
