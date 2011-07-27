package com.schlimm.decorator.springsamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SomeTestBean {
	
	@Autowired @Qualifier("my")
	private MyServiceInterface decoratedInterface;

	public MyServiceInterface getDecoratedInterface() {
		return decoratedInterface;
	}

	public void setDecoratedInterface(MyServiceInterface decoratedInterface) {
		this.decoratedInterface = decoratedInterface;
	}

}
