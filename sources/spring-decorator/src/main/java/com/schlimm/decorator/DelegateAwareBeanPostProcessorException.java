package com.schlimm.decorator;

import org.springframework.core.NestedRuntimeException;

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
