package com.schlimm.springcdi;

import java.util.Set;

import org.springframework.beans.factory.support.AutowireCandidateResolver;

/**
 * Interface for different Spring-CDI classes that allow plug-ins. This interface enables Spring-CDI modules to be registered
 * in any combination. This allows convenient selection of extended functionality based on users needs.
 * 
 * @author Niklas Schlimm
 * @see AutowireCandidateResolver
 *
 */
public interface SpringCDIInfrastructure {
	
	void addPlugin(SpringCDIPlugin plugin);
	Set<SpringCDIPlugin> getPlugins();
	
}
