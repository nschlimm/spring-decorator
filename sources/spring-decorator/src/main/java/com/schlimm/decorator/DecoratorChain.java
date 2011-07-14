package com.schlimm.decorator;


public interface DecoratorChain {
	
	String findSuccessorBeanName(String beanName, String[] chainBeanNames);

}
