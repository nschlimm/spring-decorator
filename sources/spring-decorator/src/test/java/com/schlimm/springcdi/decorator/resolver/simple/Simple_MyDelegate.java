package com.schlimm.springcdi.decorator.resolver.simple;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class Simple_MyDelegate implements Simple_MyServiceInterface {

	public void setDelegateClass(Simple_MyDelegate delegate) {
		// TODO Auto-generated method stub
		
	}

	public Simple_MyDelegate getDelegateClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDelegateInterface(Simple_MyServiceInterface serviceDelegate) {
		// TODO Auto-generated method stub
		
	}

	public Simple_MyServiceInterface getDelegateInterface() {
		// TODO Auto-generated method stub
		return null;
	}

}
