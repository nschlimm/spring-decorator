package com.schlimm.decorator.resolver.longtwoqualified;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.schlimm.decorator.resolver.longsinglechain.MyServiceInterface;

public class SomeTestBean {
	
	@Autowired @Qualifier("another")
	private MyServiceInterface decoratedInterface;

	public MyServiceInterface getDecoratedInterface() {
		return decoratedInterface;
	}

	public void setDecoratedInterface(MyServiceInterface decoratedInterface) {
		this.decoratedInterface = decoratedInterface;
	}

}
