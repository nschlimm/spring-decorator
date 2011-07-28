package com.schlimm.springcdi.decorator.resolver.longtwochains;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class LongTwoChains_AnotherDelegate implements LongTwoChains_AnotherServiceInterface {

	@Override
	public LongTwoChains_AnotherServiceInterface getDelegateObject() {
		return null;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}

}
