package com.schlimm.springcdi.decorator.resolver.longtwoqualified;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.schlimm.springcdi.decorator.resolver.longsinglechain.LongSingleChain_MyServiceInterface;

@Component
@Scope("session")
@Qualifier("another")
public class LongTwoQualified_AnotherDelegate implements LongSingleChain_MyServiceInterface {

	@Override
	public LongSingleChain_MyServiceInterface getDelegateObject() {
		return null;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}

}
