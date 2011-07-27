package com.schlimm.decorator.resolver.longtwoqualified;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.schlimm.decorator.resolver.longsinglechain.MyServiceInterface;

@Component
@Scope("session")
@Qualifier("my")
public class MyDelegate implements MyServiceInterface {

	@Override
	public MyServiceInterface getDelegateObject() {
		return null;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}

}
