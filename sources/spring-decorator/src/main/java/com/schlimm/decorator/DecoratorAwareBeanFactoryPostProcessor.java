package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.decorator.Decorator;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import com.schlimm.decorator.resolver.DecoratorInfo;
import com.schlimm.decorator.resolver.DelegateAwareAutowireCandidateResolver;
import com.schlimm.decorator.resolver.DelegateField;
import com.schlimm.decorator.resolver.QualifiedDecoratorChain;
import com.schlimm.decorator.resolver.QualifiedDependencyDescription;

/**
 * This {@link BeanFactoryPostProcessor} sets custom {@link AutowireCandidateResolver} that ignores decorators for autowiring
 * 
 * @author Niklas Schlimm
 * 
 */
@SuppressWarnings("rawtypes")
public class DecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {

	protected final Log logger = LogFactory.getLog(getClass());

	private HashMap<String, Class> registeredDecoratorsCache;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		DelegateAwareAutowireCandidateResolver newResolver = new DelegateAwareAutowireCandidateResolver();
		newResolver.setBeanFactory(beanFactory);
		((DefaultListableBeanFactory) beanFactory).setAutowireCandidateResolver(newResolver);

		Map<String, Class> decorators = getRegisteredDecorators(beanFactory);
		List<DecoratorInfo> decoratorInfos = new ArrayList<DecoratorInfo>();
		List<QualifiedDecoratorChain> chains = new ArrayList<QualifiedDecoratorChain>();
		Set<QualifiedDependencyDescription> descs = new HashSet<QualifiedDependencyDescription>();
		for (String bdName : decorators.keySet()) {
			DecoratorInfo newDecoratorInfo = new DecoratorInfo(bdName, beanFactory.getBeanDefinition(bdName), decorators.get(bdName));
			descs.addAll(newDecoratorInfo.getQualifiedDependencyDescriptions());
			decoratorInfos.add(newDecoratorInfo);
		}
		for (QualifiedDependencyDescription qualifiedDependencyDescription : descs) {
			QualifiedDecoratorChain chain = new QualifiedDecoratorChain(qualifiedDependencyDescription);
			for (DecoratorInfo decoratorInformation : decoratorInfos) {
				if (decoratorInformation.isDecoratorFor(qualifiedDependencyDescription)) {
					chain.addDecoratorInfo(decoratorInformation);
				}
			}
			chain.setQualifiedDelegateDependencyDescription(qualifiedDependencyDescription);
			List<String> delegates = getRegisteredDelegate(beanFactory, chain);
			chain.setDelegateBeanDefinitionHolder(new BeanDefinitionHolder(beanFactory.getBeanDefinition(delegates.get(0)), delegates.get(0)));
			chains.add(chain);
		}
		newResolver.setDecoratorChains(chains);

	}

	private List<String> getRegisteredDelegate(ConfigurableListableBeanFactory beanFactory, QualifiedDecoratorChain chain) {
		DelegateField arbitraryDelegateField = chain.getArbitraryDelegateField();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(DependencyDescriptor.class);
		enhancer.setCallback(NoOp.INSTANCE);
		enhancer.setInterfaces(new Class[] { DelegateDependencyDescriptorTag.class });
		DependencyDescriptor desc = (DependencyDescriptor) enhancer.create(new Class[] { Field.class, boolean.class }, new Object[] { arbitraryDelegateField.getDelegateField(), true });
		String[] bdNames = beanFactory.getBeanNamesForType(arbitraryDelegateField.getDelegateField().getType(), true, false);
		List<String> registeredDelegates = new ArrayList<String>();
		for (String bdName : bdNames) {
			Class beanClass = null;
			try {
				beanClass = Class.forName(beanFactory.getBeanDefinition(bdName).getBeanClassName());
			} catch (NoSuchBeanDefinitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!DecoratorInfo.isDecorator(beanClass)) {
				if (bdName.startsWith("scopedTarget.")) bdName = bdName.replace("scopedTarget.", "");
				if ((((DefaultListableBeanFactory) beanFactory).isAutowireCandidate(bdName, desc))) {
					registeredDelegates.add(bdName);
				}
			}
		}
		return registeredDelegates;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	public Map<String, Class> getRegisteredDecorators(ConfigurableListableBeanFactory beanFactory) {
		Map<String, Class> definitions = new HashMap<String, Class>();
		if (registeredDecoratorsCache == null) {
			registeredDecoratorsCache = new HashMap<String, Class>();
			String[] bdNames = beanFactory.getBeanDefinitionNames();
			for (String bdName : bdNames) {
				BeanDefinition bd = beanFactory.getBeanDefinition(bdName);
				Class targetClass = null;
				if (bd.getOriginatingBeanDefinition() == null) {
					targetClass = getTargetClass(bdName, bd);
				} else {
					targetClass = getTargetClass(bdName, bd.getOriginatingBeanDefinition());
				}
				if (AnnotationUtils.findAnnotation(targetClass, Decorator.class) != null) {
					definitions.put(bdName, targetClass);
					registeredDecoratorsCache.put(bdName, targetClass);
				}
			}
		} else {
			definitions = registeredDecoratorsCache;
		}

		return definitions;
	}

	private Class getTargetClass(String bdName, BeanDefinition bd) {
		Class targetClass = null;
		try {
			targetClass = Class.forName(bd.getBeanClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return targetClass;
	}
}
