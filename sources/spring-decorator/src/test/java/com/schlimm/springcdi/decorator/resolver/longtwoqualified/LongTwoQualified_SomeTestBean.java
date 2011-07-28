package com.schlimm.springcdi.decorator.resolver.longtwoqualified;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.schlimm.springcdi.decorator.resolver.longsinglechain.LongSingleChain_MyServiceInterface;

public class LongTwoQualified_SomeTestBean {
	
	@Autowired @Qualifier("another")
	private LongSingleChain_MyServiceInterface decoratedInterface;

	public LongSingleChain_MyServiceInterface getDecoratedInterface() {
		return decoratedInterface;
	}

	public void setDecoratedInterface(LongSingleChain_MyServiceInterface decoratedInterface) {
		this.decoratedInterface = decoratedInterface;
	}

}
