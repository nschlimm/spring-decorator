package com.schlimm.springcdi.decorator.resolver.longtwochains;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class AnotherDelegate implements AnotherServiceInterface {

	@Override
	public AnotherServiceInterface getDelegateObject() {
		return null;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}

}
