package com.schlimm.springcdi.decorator.resolver.simple;

public interface MyServiceInterface {

	public void setDelegateInterface(MyServiceInterface serviceDelegate);
	public MyServiceInterface getDelegateInterface();
}
