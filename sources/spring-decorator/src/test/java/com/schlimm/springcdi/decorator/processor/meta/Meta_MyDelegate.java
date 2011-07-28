package com.schlimm.springcdi.decorator.processor.meta;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class Meta_MyDelegate implements Meta_MyServiceInterface {

	@Override
	public Meta_MyServiceInterface getDelegateObject() {
		return this;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}


}
