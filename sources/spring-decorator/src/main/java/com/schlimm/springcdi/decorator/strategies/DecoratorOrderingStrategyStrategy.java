package com.schlimm.springcdi.decorator.strategies;

import java.util.List;

import com.schlimm.springcdi.model.DecoratorInfo;

public interface DecoratorOrderingStrategyStrategy {
	
	List<DecoratorInfo> orderDecorators(List<DecoratorInfo> decorators);

}
