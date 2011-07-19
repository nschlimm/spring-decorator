package com.schlimm.decorator.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotVeryUsefulAspect {
	
	@Around("execution(* com.schlimm.decorator.aop.MyServiceInterface.*(..))")
	  public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("Before: " + pjp.getTarget().getClass().toString());
		System.out.println("Before: " + pjp.getSignature().toShortString());
	    Object retVal = pjp.proceed();
	    // stop stopwatch
	    return retVal;
	  }

}
