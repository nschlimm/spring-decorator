package com.schlimm.decorator.resolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

@SuppressWarnings("rawtypes")
public class DecoratorInfo {

	private Class decoratorClass;

	private BeanDefinitionHolder decoratorBeanDefinitionHolder;

	private List<DelegateField> delegateFields;

	public static boolean isDecorator(Class candidate) {
		return AnnotationUtils.findAnnotation(candidate, Decorator.class) != null;
	}

	public DecoratorInfo(String beanName, BeanDefinition beanDefinition, Class decoratorClass) {
		this.decoratorClass = decoratorClass;
		decoratorBeanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
		delegateFields = new ArrayList<DelegateField>();
		ReflectionUtils.doWithFields(decoratorClass, new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				if (field.isAnnotationPresent(Delegate.class)) {
					delegateFields.add(new DelegateField(field, new DelegateDependencyDescriptor(field, false)));
				}
			}
		});
	}

	public List<QualifiedDependencyDescription> getQualifiedDependencyDescriptions() {
		List<QualifiedDependencyDescription> descs = new ArrayList<QualifiedDependencyDescription>();
		for (DelegateField field : delegateFields) {
			descs.add(new QualifiedDependencyDescription(field.getDependencyDescriptor()));
		}
		return descs;
	}

	public boolean isDecoratorFor(QualifiedDependencyDescription qualifiedDescription) {
		for (QualifiedDependencyDescription description : getQualifiedDependencyDescriptions()) {
			if (description.equals(qualifiedDescription))
				return true;
		}
		return false;
	}

	public DelegateField getDelegateField(QualifiedDependencyDescription qualifiedDependencyDescription) {
		for (DelegateField delegateField : delegateFields) {
			QualifiedDependencyDescription delegateFieldQualifiedDepDesc = new QualifiedDependencyDescription(delegateField.getDependencyDescriptor());
			if (delegateFieldQualifiedDepDesc.equals(qualifiedDependencyDescription))
				return delegateField;
		}
		return null;
	}

	public boolean equals(DecoratorInfo otherDecoratorInfo) {
		return this.decoratorClass.equals(otherDecoratorInfo.getDecoratorClass());
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

	public Set<DelegateDependencyDescriptor> getAllDelegateDependencyDescriptors() {
		Set<DelegateDependencyDescriptor> descs = new HashSet<DelegateDependencyDescriptor>();
		for (DelegateField delegateField : delegateFields) {
			descs.add(delegateField.getDependencyDescriptor());
		}
		return descs;
	}

	public List<DelegateField> getDelegateFields() {
		return delegateFields;
	}

	public void setDelegateFields(List<DelegateField> delegateFields) {
		this.delegateFields = delegateFields;
	}

}
