package com.schlimm.decorator.resolver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public class QualifiedDecoratorChain {

	List<DecoratorInfo> decorators = new ArrayList<DecoratorInfo>();

	/**
	 * Dependency descriptor that identifies the delegate of the contained decorators. Every decorator in the chain has a
	 * reference to this delegate.
	 */
	private QualifiedDependencyDescription qualifiedDelegateDependencyDescription;

	/**
	 * The bean definition of the target delegate that matches the qualified dependency description.
	 */
	private BeanDefinitionHolder delegateBeanDefinitionHolder;

	public List<DecoratorInfo> getDecorators() {
		return decorators;
	}

	public QualifiedDecoratorChain(QualifiedDependencyDescription qualifiedDependencyDescription) {
		super();
		this.qualifiedDelegateDependencyDescription = qualifiedDependencyDescription;
	}

	public void setDecorators(List<DecoratorInfo> decorators) {
		this.decorators = decorators;
	}

	public boolean areSequential(BeanDefinitionHolder successorDefinition, DependencyDescriptor predecessorDescriptor) {
		// First decorator cannot be a successor
		if (successorDefinition.getBeanName().equals(decorators.get(0).getDecoratorBeanDefinitionHolder().getBeanDefinition())) {
			return false;
		}
		// Is the successor the target delegate bean?
		if (successorDefinition.getBeanName().equals(delegateBeanDefinitionHolder.getBeanName())) {
			// The the predessessor must be the last decorator => to be sequential
			DecoratorInfo lastDecoratorInfo = decorators.get(decorators.size() - 1);
			return decoratorMatchesDescriptor(predecessorDescriptor, lastDecoratorInfo);
		}
		for (int i = 0; i < decorators.size() - 1; i++) {
			if (decoratorMatchesDescriptor(predecessorDescriptor, decorators.get(i))) {
				if (successorDefinition.getBeanName().equals(decorators.get(i+1).getDecoratorBeanDefinitionHolder().getBeanDefinition())) {
					// Sequential decorators if predessessor matches decorator i and successor matches decorator i + 1
					return true;
				}
			}
		}
		return false;
	}

	private boolean decoratorMatchesDescriptor(DependencyDescriptor predecessorDescriptor, DecoratorInfo lastDecoratorInfo) {
		for (DelegateDependencyDescriptor delegateDepDesc : lastDecoratorInfo.getAllDelegateDependencyDescriptors()) {
			if (delegateDepDesc.equals(predecessorDescriptor)) {
				return true;
			}
		}
		return false;
	}

	public boolean isKnownDecorator(DecoratorInfo decoratorInfo) {
		for (DecoratorInfo decorator : decorators) {
			if (decorator.equals(decoratorInfo))
				return true;
		}
		return false;
	}

	public DelegateField getArbitraryDelegateField() {
		return decorators.get(0).getDelegateField(qualifiedDelegateDependencyDescription);
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

	public QualifiedDependencyDescription getQualifiedDelegateDependencyDescription() {
		return qualifiedDelegateDependencyDescription;
	}

	public void setQualifiedDelegateDependencyDescription(QualifiedDependencyDescription qualifiedDelegateDependencyDescription) {
		this.qualifiedDelegateDependencyDescription = qualifiedDelegateDependencyDescription;
	}

}
