package com.schlimm.decorator;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception that is thrown by decorator pattern implementation logic.
 * 
 * @author Niklas Schlimm
 *
 */
public class DelegateAwareBeanPostProcessorException extends NestedRuntimeException {

	/**
	 * Unique ID
	 */
	private static final long serialVersionUID = -2241526745257996175L;

	public DelegateAwareBeanPostProcessorException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DelegateAwareBeanPostProcessorException(String msg) {
		super(msg);
	}

}
