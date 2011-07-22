package com.schlimm.springcdi;

import org.springframework.beans.factory.support.AutowireCandidateResolver;

/**
 * Interface for different Spring-CDI classes that allow plug-ins.
 * 
 * @author Niklas Schlimm
 * @see AutowireCandidateResolver
 *
 */
public interface SpringCDIInfrastructure {
	
	void addPlugin(SpringCDIPlugin plugin);
	
}
