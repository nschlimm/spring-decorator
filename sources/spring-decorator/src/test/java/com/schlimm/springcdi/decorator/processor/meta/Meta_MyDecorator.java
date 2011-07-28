package com.schlimm.springcdi.decorator.processor.meta;


@SecurityDecorator
public class Meta_MyDecorator implements Meta_MyServiceInterface {
	
	@SecuredDelegate
	private Meta_MyServiceInterface delegateInterface;

	@Override
	public Meta_MyServiceInterface getDelegateObject() {
		return delegateInterface;
	}

	@Override
	public String sayHello() {
		return delegateInterface.sayHello();
	}


}
