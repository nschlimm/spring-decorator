package com.schlimm.decorator.resolver;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import javax.decorator.Delegate;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.DependencyDescriptor;

@SuppressWarnings("rawtypes")
public class QualifiedDependencyDescription {

	/**
	 * Field annotations at the injection point.
	 */
	private Annotation[] fieldAnnotations;

	/**
	 * Qualifier annotations at the injection point.
	 */
	private Annotation[] qualifierAnnotations;

	@SuppressWarnings("unchecked")
	private List<Class<? extends Annotation>> autowiringAnnos = Arrays.asList(Delegate.class, Autowired.class, Inject.class, Value.class);

	/**
	 * Type of the field at the injection point.
	 */
	private Class fieldType;

	public QualifiedDependencyDescription(DependencyDescriptor descriptor) {
		super();
		this.fieldAnnotations = descriptor.getAnnotations();
		this.fieldType = descriptor.getField().getType();
//		List<Annotation> qAnno = new ArrayList<Annotation>();
//		for (Annotation annotation : fieldAnnotations) {
//			// TODO: retrieve from AnnotationBeanPostProcessor ...
//			if (annotation instanceof Delegate || annotation instanceof Autowired  || annotation instanceof Inject || annotation instanceof Value 
//					|| Arrays.asList(annotation.getClass().getAnnotations()).contains(Qualifier.class)
//					|| Arrays.asList(annotation.getClass().getAnnotations()).contains(org.springframework.beans.factory.annotation.Qualifier.class)) {
//				qAnno.add(annotation);
//			}
//		}
		this.qualifierAnnotations = fieldAnnotations;//(Annotation[]) qAnno.toArray();
	}

	public void setFieldAnnotations(Annotation[] fieldAnnotations) {
		this.fieldAnnotations = fieldAnnotations;
	}

	public Annotation[] getFieldAnnotations() {
		return fieldAnnotations;
	}

	public void setFieldType(Class fieldType) {
		this.fieldType = fieldType;
	}

	public Class getFieldType() {
		return fieldType;
	}

	public boolean equals(QualifiedDependencyDescription desc) {
		// TODO: Sort arrays prior to comparison and ignore autowiring annos
		return (Arrays.equals(desc.getQualifierAnnotations(), this.qualifierAnnotations) && fieldType.equals(desc.getFieldType()));
	}

	public void setQualifierAnnotations(Annotation[] qualifierAnnotations) {
		this.qualifierAnnotations = qualifierAnnotations;
	}

	public Annotation[] getQualifierAnnotations() {
		return qualifierAnnotations;
	}

}
