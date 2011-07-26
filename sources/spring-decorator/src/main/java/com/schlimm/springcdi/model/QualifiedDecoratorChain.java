package com.schlimm.springcdi.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

/**
 * Model bean that aggregates all decorators for a specific delegate target bean.
 * 
 * @author Niklas Schlimm
 * 
 */
public class QualifiedDecoratorChain {

	/**
	 * The decorator chain for the target delegate bean
	 */
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

	/**
	 * Method determines if a bean described by a given {@link BeanDefinitionHolder} is the successor of another bean described by
	 * a {@link DependencyDescriptor}.
	 * 
	 * If both parameters point to decorator beans: The {@link DependencyDescriptor} must match a {@link DelegateField} of a
	 * decorator in the chain. The {@link BeanDefinitionHolder} must match a {@link BeanDefinitionHolder} of a
	 * {@link DecoratorInfo} in the chain.
	 * 
	 * @param successorDefinition
	 *            successor bean
	 * @param predecessorDescriptor
	 *            predecessor bean
	 * @return true if successorDefinition points to a decorator that is the successor of the decorator that the
	 *         predecessorDescriptor points to
	 */
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
				if (successorDefinition.getBeanName().equals(decorators.get(i + 1).getDecoratorBeanDefinitionHolder().getBeanName())) {
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
