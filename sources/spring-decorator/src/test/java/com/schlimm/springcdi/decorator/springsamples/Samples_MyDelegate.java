package com.schlimm.springcdi.decorator.springsamples;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("my")
public class Samples_MyDelegate implements Samples_MyServiceInterface {

	@Override
	public Samples_MyServiceInterface getDelegateObject() {
		return null;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}

}
