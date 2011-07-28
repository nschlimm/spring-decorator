package com.schlimm.springcdi.decorator.resolver.longtwochains;

import org.springframework.beans.factory.annotation.Autowired;

public class LongTwoChains_SomeTestBean {
	
	@Autowired
	private LongTwoChains_AnotherServiceInterface decoratedInterface;

	public LongTwoChains_AnotherServiceInterface getDecoratedInterface() {
		return decoratedInterface;
	}

	public void setDecoratedInterface(LongTwoChains_AnotherServiceInterface decoratedInterface) {
		this.decoratedInterface = decoratedInterface;
	}

}
