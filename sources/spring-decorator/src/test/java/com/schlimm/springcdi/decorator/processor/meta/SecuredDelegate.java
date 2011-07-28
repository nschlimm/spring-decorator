package com.schlimm.springcdi.decorator.processor.meta;

import java.lang.annotation.Retention;

import com.schlimm.springcdi.decorator.meta.DelegateAnnotation;

@DelegateAnnotation
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SecuredDelegate { }
