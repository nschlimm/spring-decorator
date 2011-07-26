package com.schlimm.springcdi.decorator;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SimpleBeanTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.schlimm.springcdi.decorator.strategies.DecoratorChainingStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorChainingStrategy;
import com.schlimm.springcdi.model.DecoratorMetaDataBean;

public class DecoratorAwareBeanPostProcessor implements BeanPostProcessor, InitializingBean {

	@Autowired
	private DecoratorMetaDataBean metaData;
	
	@Autowired
	private ConfigurableListableBeanFactory beanFactory;
	
	private DecoratorChainingStrategy chainingStrategy;
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		if (metaData.isDecoratedBean(beanName)) {
			return buildDelegateProxy(bean, beanName);
		} else {
			return bean;
		}
		
	}

	public Object buildDelegateProxy(Object bean, String beanName) {
		ProxyFactory pf = new ProxyFactory();
		SimpleBeanTargetSource targetSource = new SimpleBeanTargetSource();
		targetSource.setTargetBeanName(beanName);
		targetSource.setBeanFactory(beanFactory);
		targetSource.setTargetClass(bean.getClass());
		pf.setTargetSource(targetSource);
		pf.setProxyTargetClass(true);
		DelegatingInterceptor interceptor = new DelegatingInterceptor(chainingStrategy.getChainedDecorators(beanFactory, metaData.getQualifiedDecoratorChain(beanName), bean));
		pf.addAdvice(interceptor);
		Object proxy = pf.getProxy();
		return proxy;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (chainingStrategy == null) {
			chainingStrategy = new SimpleDecoratorChainingStrategy();
		}
	}
	
}
