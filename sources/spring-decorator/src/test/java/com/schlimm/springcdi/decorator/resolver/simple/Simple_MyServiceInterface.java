package com.schlimm.springcdi.decorator.resolver.simple;

public interface Simple_MyServiceInterface {

	public void setDelegateInterface(Simple_MyServiceInterface serviceDelegate);
	public Simple_MyServiceInterface getDelegateInterface();
}
