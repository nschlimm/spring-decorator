package com.schlimm.springcdi.decorator.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import com.schlimm.springcdi.decorator.meta.DecoratorAnnotation;
import com.schlimm.springcdi.decorator.meta.DelegateAnnotation;

/**
 * Decorator meta-data model bean.
 * 
 * @author Niklas Schlimm
 *
 */
@SuppressWarnings("rawtypes")
public class DecoratorInfo {


	/**
	 * The decorator class as defined in the bean definition
	 */
	private Class decoratorClass;

	/**
	 * The bean definition of this decorator
	 */
	private BeanDefinitionHolder decoratorBeanDefinitionHolder;

	/**
	 * All delegate fields of the decoratror. Currently only allows one delegate field.
	 */
	private List<DelegateField> delegateFields;

	private static Class<? extends Annotation> decoratorAnnotationType = Decorator.class;

	private static Class<? extends Annotation> delegateAnnotationType = Delegate.class;
	
	private static Class<? extends Annotation> decoratorMetaAnnotationType = DecoratorAnnotation.class;
	
	private static Class<? extends Annotation> delegateMetaAnnotationType = DelegateAnnotation.class;
	
	
	/**
	 * Check if given class is a decorator.
	 * @param candidateClass class to check
	 * @return true if contains @Decorator annotation of @DecoratorAnnotation meta-annotation
	 */
	public static boolean isDecorator(Class candidateClass) {
		if (AnnotationUtils.findAnnotation(candidateClass, decoratorAnnotationType) != null) {
			return true;
		}
		Annotation[] annotations = candidateClass.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(decoratorMetaAnnotationType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if given class name is a decorator.
	 * @param candidateClass class to check
	 * @return true if contains @Decorator annotation
	 */
	public static boolean isDecorator(String candidateClassName) {
		Class candidateClazz = null;
		try {
			candidateClazz = Class.forName(candidateClassName);
		} catch (ClassNotFoundException e) {
			throw new DecoratorAwareBeanFactoryPostProcessorException("Could not locate decorator class name: " + candidateClassName, e);
		}
		return isDecorator(candidateClazz);
	}
	
	/**
	 * Check if the declaring class of the dependency (injection point) is a decorator
	 * @param dependencyDescriptor dependency to check
	 * @return true if declaring class is a decorator
	 */
	public static boolean isDecorator(DependencyDescriptor dependencyDescriptor) {
		if (isDecorator(dependencyDescriptor.getField().getDeclaringClass())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if annotated bean definition defines a decorator 
	 * @param bd given bean definition
	 * @return true if bean definition defines a decorator
	 */
	public static boolean isDecorator(AnnotatedBeanDefinition bd) {
		Map<String, Object> attributes = bd.getMetadata().getAnnotationAttributes(decoratorAnnotationType.getName());
		if (attributes != null)
			return true;
		attributes = bd.getMetadata().getAnnotationAttributes(decoratorMetaAnnotationType.getName());
		if (attributes!=null) {
			return true;
		}
		return false;
	}

	public static boolean isDelegateField(Field field) {
		if (field.isAnnotationPresent(delegateAnnotationType)) {
			return true;
		}
		Annotation[] anns = field.getAnnotations();
		for (Annotation annotation : anns) {
			if (annotation.annotationType().isAnnotationPresent(delegateMetaAnnotationType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Constructor.
	 * @param beanName bean name of the decorator
	 * @param beanDefinition bean definition of the decorator
	 * @param decoratorClass decorator class
	 */
	public DecoratorInfo(String beanName, BeanDefinition beanDefinition, Class decoratorClass) {
		this.decoratorClass = decoratorClass;
		decoratorBeanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
		delegateFields = new ArrayList<DelegateField>();
		ReflectionUtils.doWithFields(decoratorClass, new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				if (isDelegateField(field)) {
					delegateFields.add(new DelegateField(field, new DependencyDescriptor(field, false)));
				}
			}
		});
		Assert.isTrue(delegateFields.size()==1, "Decorator must have exactly one delegate field!");
	}

	/**
	 * Get all delegate fields of this decorator.
	 * @return delegate fields
	 */
	public List<Field> getDeclaredDelegateFields() {
		List<Field> fields = new ArrayList<Field>();
		for (DelegateField delegateField : delegateFields) {
			fields.add(delegateField.getDeclaredField());
		}
		return fields;
	}

	public Class getDecoratorClass() {
		return decoratorClass;
	}

	public void setDecoratorClass(Class decoratorClass) {
		this.decoratorClass = decoratorClass;
	}

	public void setDecoratorBeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder) {
		this.decoratorBeanDefinitionHolder = beanDefinitionHolder;
	}

	public BeanDefinitionHolder getDecoratorBeanDefinitionHolder() {
		return decoratorBeanDefinitionHolder;
	}

	public List<DelegateField> getDelegateFields() {
		return delegateFields;
	}

	public String toString() {
		return "Decorator class: " + decoratorClass.getName() + ", " + delegateFields.toString();
	}

}
