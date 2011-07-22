package com.schlimm.springcdi.decorator;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception that is thrown by decorator pattern implementation logic.
 * 
 * @author Niklas Schlimm
 *
 */
public class DecoratorAwareBeanFactoryPostProcessorException extends NestedRuntimeException {

	/**
	 * Unique ID
	 */
	private static final long serialVersionUID = -2241526745257996175L;

	public DecoratorAwareBeanFactoryPostProcessorException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DecoratorAwareBeanFactoryPostProcessorException(String msg) {
		super(msg);
	}

	public DecoratorAwareBeanFactoryPostProcessorException(String msg, ClassNotFoundException e) {
		super(msg, e);
	}

}
