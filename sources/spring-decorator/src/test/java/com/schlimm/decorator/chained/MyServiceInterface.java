package com.schlimm.decorator.chained;

public interface MyServiceInterface {

	public void setDelegateClass(MyDelegate delegate);
	public MyDelegate getDelegateClass();
	public void setDelegateInterface(MyServiceInterface serviceDelegate);
	public MyServiceInterface getDelegateInterface();
}
