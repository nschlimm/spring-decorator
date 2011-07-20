package com.schlimm.decorator.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.core.annotation.AnnotationUtils;

import com.schlimm.decorator.DelegateDependencyDescriptorTag;

/**
 * {@link AutowireCandidateResolver} that ignores decorator beans for autowiring.
 * 
 * Decorators and delegate implement the same interface. Therefore decorators are autowiring candidates. Without this resolver
 * dependency resolution results in ambiguous bean candidates for decorated bean.
 * 
 * @author Niklas Schlimm
 * 
 */
public class DelegateAwareAutowireCandidateResolver extends QualifierAnnotationAutowireCandidateResolver {

	private BeanFactory beanFactory;

	private List<QualifiedDecoratorChain> decoratorChains;

	public DelegateAwareAutowireCandidateResolver() {
		super();
	}

	public DelegateAwareAutowireCandidateResolver(Class<? extends Annotation> qualifierType) {
		super(qualifierType);
	}

	public DelegateAwareAutowireCandidateResolver(Set<Class<? extends Annotation>> qualifierTypes) {
		super(qualifierTypes);
	}

	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		boolean rawResult = super.isAutowireCandidate(bdHolder, descriptor);
		if (rawResult==false) return rawResult;
		boolean isDelegateDescriptor = false;
		if (CollectionUtils.select(Arrays.asList(descriptor.getAnnotations()), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				if (object instanceof Delegate) return true;
				return false;
			};
		}).size()>0) isDelegateDescriptor = true;
		if (descriptor instanceof DelegateDependencyDescriptorTag || (!isDelegateDescriptor && !descriptor.getDependencyType().isInterface()))
			return rawResult;
		boolean decoratorRulesResult = applyDecoratorAutowiringRules(bdHolder, descriptor);
		return rawResult && decoratorRulesResult;
	}

	private boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		boolean match = false;
		if (isDecoratedInjectionPoint(descriptor)) {
			// Descriptor refers to a decorated target bean => bdHolder bean name must match last decorator bean name of the
			// decorator chain
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratedInjectionPoint(descriptor);
			DecoratorInfo lastDecoratorInfo = chain.getDecorators().get(chain.getDecorators().size() - 1);
			if (lastDecoratorInfo.getDecoratorBeanDefinitionHolder().getBeanName().equals(bdHolder.getBeanName())) {
				return true;
			}
		} else if (isDecorator(descriptor)) {
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratorDescriptor(descriptor);
			// descriptor must be predecessor decorator for bdHolder
			if (chain.areSequential(bdHolder, descriptor))
				return true;
		}
		return match;
	}

	public boolean isAutowireCandidate2(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		return super.isAutowireCandidate(bdHolder, descriptor);
	}

	public boolean isDecoratedInjectionPoint(DependencyDescriptor descriptor) {
		// Field is not in a decorator, but descriptor matches a target bean definition
		if (AnnotationUtils.findAnnotation(descriptor.getField().getDeclaringClass(), Decorator.class) == null) {
			// Now that we know, that we're aoutside a @Decorator:
			// is there a chain that contains a target delegate bean definition that matches the descriptor?
			for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
				if (super.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(), descriptor)) {
					return true;
				}
			}
		}
		return false;
	}

	public QualifiedDecoratorChain getDecoratorChainForDecoratedInjectionPoint(DependencyDescriptor decoratedInjectionPoint) {
		// Match: a chain contains a target bean definition that matches the target descriptor
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			if (super.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(), decoratedInjectionPoint)) {
				return decoratorChain;
			}
		}
		return null;
	}

	public boolean isDecorator(DependencyDescriptor dependencyDescriptor) {
		if (AnnotationUtils.findAnnotation(dependencyDescriptor.getField().getDeclaringClass(), Decorator.class) != null) {
			return true;
		}
		return false;
	}

	public QualifiedDecoratorChain getDecoratorChainForDecoratorDescriptor(DependencyDescriptor decoratorDescriptor) {
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			Set<Field> fields = decoratorChain.getAllDeclaredDelegateFields();
			if (fields.contains(decoratorDescriptor.getField())) {
				return decoratorChain;
			}
		}
		return null;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public List<QualifiedDecoratorChain> getDecoratorChains() {
		return decoratorChains;
	}

	public void setDecoratorChains(List<QualifiedDecoratorChain> decoratorChains) {
		this.decoratorChains = decoratorChains;
	}
}
