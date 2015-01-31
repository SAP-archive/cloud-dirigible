package com.sap.dirigible.repository.api;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RepositoryActivator implements BundleActivator {

	private static BundleContext context;
	
	@Override
	public void start(BundleContext context) throws Exception {
		RepositoryActivator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static BundleContext getContext() {
		return context;
	}

}
