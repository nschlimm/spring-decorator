package com.schlimm.decorator.resolver.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class NotVeryUsefulAspect {

	@Pointcut("execution(* com.schlimm.decorator.resolver.longsinglechain.MyServiceInterface.*(..))")
	public void myDummyPointCut() {}
	
	@Before("myDummyPointCut()")
	public void sayHello() {
		System.out.println("Aspect Hello");
	}
}
