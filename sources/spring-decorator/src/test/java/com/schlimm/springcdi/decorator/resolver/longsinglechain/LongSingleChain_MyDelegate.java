package com.schlimm.springcdi.decorator.resolver.longsinglechain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class LongSingleChain_MyDelegate implements LongSingleChain_MyServiceInterface {

	@Override
	public LongSingleChain_MyServiceInterface getDelegateObject() {
		return null;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}

}
