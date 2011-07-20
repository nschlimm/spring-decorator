package com.schlimm.decorator.resolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public class QualifiedDecoratorChain {

	List<DecoratorInfo> decorators = new ArrayList<DecoratorInfo>();

	/**
	 * The bean definition of the target delegate that matches the qualified dependency description.
	 */
	private BeanDefinitionHolder delegateBeanDefinitionHolder;

	public List<DecoratorInfo> getDecorators() {
		return decorators;
	}

	public QualifiedDecoratorChain(BeanDefinitionHolder delegateBeanDefinitionHolder) {
		super();
		this.delegateBeanDefinitionHolder = delegateBeanDefinitionHolder;
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
			// The predessessor must be the last decorator => to be sequential
			DecoratorInfo lastDecoratorInfo = decorators.get(decorators.size() - 1);
			return lastDecoratorInfo.getDeclaredDelegateFields().contains(predecessorDescriptor.getField());
		}
		// Both decorators?
		for (int i = 0; i < decorators.size() - 1; i++) {
			if (decorators.get(i).getDeclaredDelegateFields().contains(predecessorDescriptor.getField())) {
				if (successorDefinition.getBeanName().equals(decorators.get(i+1).getDecoratorBeanDefinitionHolder().getBeanName())) {
					// Sequential decorators if predessessor matches decorator i and successor matches decorator i + 1
					return true;
				}
			}
		}
		System.out.println();
		return false;
	}

	public boolean isKnownDecorator(DecoratorInfo decoratorInfo) {
		for (DecoratorInfo decorator : decorators) {
			if (decorator.equals(decoratorInfo))
				return true;
		}
		return false;
	}
	
	public Set<Field> getAllDeclaredDelegateFields() {
		Set<Field> set = new HashSet<Field>();
		for (DecoratorInfo deco : decorators) {
			set.addAll(deco.getDeclaredDelegateFields());
		}
		return set;
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

}
