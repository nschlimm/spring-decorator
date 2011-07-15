package com.schlimm.decorator;

import java.lang.reflect.Field;
import java.util.SortedMap;

public interface DecorationStrategy {
	
	Object decorateDelegate(Object delegate, SortedMap<String, Field> resolvedDecorators);

}
