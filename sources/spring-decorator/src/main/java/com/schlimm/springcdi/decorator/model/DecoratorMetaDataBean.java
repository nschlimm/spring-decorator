package com.schlimm.springcdi.decorator.model;

import java.util.List;

/**
 * Top-level model bean that contains all the decorator chains of an application context. The bean is registered with the
 * application context.
 * 
 * @author Niklas Schlimm
 * 
 */
public class DecoratorMetaDataBean {

	private List<QualifiedDecoratorChain> decoratorChains;

	public DecoratorMetaDataBean(List<QualifiedDecoratorChain> decoratorChains) {
		super();
		this.decoratorChains = decoratorChains;
	}

	/**
	 * Find out if the given bean name refers to a decorated bean.
	 * @param beanName the bean name to check
	 * @return true if bean is decorated
	 */
	public boolean isDecoratedBean(String beanName) {
		for (QualifiedDecoratorChain chain : decoratorChains) {
			if (chain.getDelegateBeanDefinitionHolder().getBeanName().equals(beanName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve {@link QualifiedDecoratorChain} for trhe given delegate bean name
	 * @param delegateBeanName the delegate bean name
	 * @return the corresponding {@link QualifiedDecoratorChain} if exists, otherwise return null
	 */
	public QualifiedDecoratorChain getQualifiedDecoratorChain(String delegateBeanName) {
		for (QualifiedDecoratorChain chain : decoratorChains) {
			if (chain.getDelegateBeanDefinitionHolder().getBeanName().equals(delegateBeanName)) {
				return chain;
			}
		}
		return null;
	}

	public void setDecoratorChains(List<QualifiedDecoratorChain> decoratorChains) {
		this.decoratorChains = decoratorChains;
	}

	public List<QualifiedDecoratorChain> getDecoratorChains() {
		return decoratorChains;
	}

}
