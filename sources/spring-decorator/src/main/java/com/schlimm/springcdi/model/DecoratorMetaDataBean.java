package com.schlimm.springcdi.model;

import java.util.List;


public class DecoratorMetaDataBean {
	
	private List<QualifiedDecoratorChain> decoratorChains;

	public DecoratorMetaDataBean(List<QualifiedDecoratorChain> decoratorChains) {
		super();
		this.decoratorChains = decoratorChains;
	}

	public void setDecoratorChains(List<QualifiedDecoratorChain> decoratorChains) {
		this.decoratorChains = decoratorChains;
	}

	public List<QualifiedDecoratorChain> getDecoratorChains() {
		return decoratorChains;
	}
	
	public boolean isDecoratedBean(String delegateBeanName) {
		for (QualifiedDecoratorChain chain : decoratorChains) {
			if (chain.getDelegateBeanDefinitionHolder().getBeanName().equals(delegateBeanName)) {
				return true;
			}
		}
		return false;
	}
	
	public QualifiedDecoratorChain getQualifiedDecoratorChain(String delegateBeanName) {
		for (QualifiedDecoratorChain chain : decoratorChains) {
			if (chain.getDelegateBeanDefinitionHolder().getBeanName().equals(delegateBeanName)) {
				return chain;
			}
		}
		return null;
	}
	
	

}
