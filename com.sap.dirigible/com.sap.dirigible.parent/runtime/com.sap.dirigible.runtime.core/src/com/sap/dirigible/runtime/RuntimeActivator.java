package com.sap.dirigible.runtime;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RuntimeActivator implements BundleActivator {

	private static BundleContext context;
	
	@Override
	public void start(BundleContext context) throws Exception {
		RuntimeActivator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static BundleContext getContext() {
		return context;
	}
}
