package com.schlimm.decorator.resolver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public class QualifiedDecoratorChain {
	
	List<DecoratorInfo> decorators = new ArrayList<DecoratorInfo>();
	
	/**
	 * Dependency descriptor that matches this decorator chain.
	 * An injection point that matches this description gets the decorator chain injected.
	 */
	private QualifiedDependencyDescription qualifiedDependencyDescription;

	/**
	 * The bean definition of the target delegate that matches the qualified dependency description.
	 */
	private BeanDefinitionHolder delegateBeanDefinitionHolder;
	
	public List<DecoratorInfo> getDecorators() {
		return decorators;
	}

	public QualifiedDecoratorChain(QualifiedDependencyDescription qualifiedDependencyDescription) {
		super();
		this.qualifiedDependencyDescription = qualifiedDependencyDescription;
	}

	public void setDecorators(List<DecoratorInfo> decorators) {
		this.decorators = decorators;
	}

	public boolean validateAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		if (bdHolder.equals(delegateBeanDefinitionHolder)) {
			DecoratorInfo lastDecoratorInfo = decorators.get(decorators.size()-1);
			for (DelegateDependencyDescriptor delegateDepDesc : lastDecoratorInfo.getAllDelegateDependencyDescriptors()) {
				if (delegateDepDesc.equals(descriptor)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isKnownDecorator(DecoratorInfo decoratorInfo) {
		for (DecoratorInfo decorator : decorators) {
			if (decorator.equals(decoratorInfo)) return true;
		}
		return false;
	}
	
	public DelegateField getArbitraryDelegateField() {
		return decorators.get(0).getDelegateField(qualifiedDependencyDescription);
	}
	
	public void addDecoratorInfo(DecoratorInfo decoratorInfo) {
		decorators.add(decoratorInfo);
	}

	public void setDelegateBeanDefinitionHolder(BeanDefinitionHolder delegateBeanDefinitionHolder) {
		this.delegateBeanDefinitionHolder = delegateBeanDefinitionHolder;
	}

	public BeanDefinitionHolder getDelegateBeanDefinitionHolder() {
		return delegateBeanDefinitionHolder;
	}

	public void setQualifiedDependencyDescription(QualifiedDependencyDescription qualifiedDependencyDescription) {
		this.qualifiedDependencyDescription = qualifiedDependencyDescription;
	}

	public QualifiedDependencyDescription getQualifiedDependencyDescription() {
		return qualifiedDependencyDescription;
	}

}
