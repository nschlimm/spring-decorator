package com.schlimm.decorator;

import java.util.List;

import com.schlimm.decorator.resolver.DecoratorInfo;

public interface DecoratorOrderingStrategyStrategy {
	
	List<DecoratorInfo> orderDecorators(List<DecoratorInfo> decorators);

}
