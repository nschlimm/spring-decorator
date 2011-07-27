package com.schlimm.springcdi.decorator.resolver.longsinglechain;

public interface MyServiceInterface {

	public MyServiceInterface getDelegateObject();
	public String sayHello();
}
