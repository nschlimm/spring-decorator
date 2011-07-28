package com.schlimm.springcdi.decorator.springsamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Samples_SomeTestBean {
	
	@Autowired @Qualifier("my")
	private Samples_MyServiceInterface decoratedInterface;

	public Samples_MyServiceInterface getDecoratedInterface() {
		return decoratedInterface;
	}

	public void setDecoratedInterface(Samples_MyServiceInterface decoratedInterface) {
		this.decoratedInterface = decoratedInterface;
	}

}
