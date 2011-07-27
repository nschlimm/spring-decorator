package com.schlimm.springcdi.decorator.processor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ReflectionUtils;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;

public class DelegatingInterceptor implements MethodInterceptor {

	private Object decoratorChain;

	public DelegatingInterceptor(Object decoratorChain) {
		super();
		this.decoratorChain = decoratorChain;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object retVal = null;
		try {
			Method targetMethod = ReflectionUtils.findMethod(this.decoratorChain.getClass(), invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
			if (targetMethod == null) {
				retVal = invocation.proceed();
			} else {
				retVal = AopUtils.invokeJoinpointUsingReflection(this.decoratorChain, targetMethod, invocation.getArguments());
			}
		} catch (Throwable e) {
			throw new DecoratorAwareBeanFactoryPostProcessorException("Cannot invoke method on delegate proxy: " + invocation.toString());
		}
		return retVal;
	}

	public void setDecoratorChain(Object decoratorChain) {
		this.decoratorChain = decoratorChain;
	}

	public Object getDecoratorChain() {
		return decoratorChain;
	}

}
