package com.schlimm.springcdi.decorator.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Meta annotation to define custom delegate annotations.
 * 
 * @author Niklas Schlimm
 *
 */
@Target(value={java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface DelegateAnnotation { }
