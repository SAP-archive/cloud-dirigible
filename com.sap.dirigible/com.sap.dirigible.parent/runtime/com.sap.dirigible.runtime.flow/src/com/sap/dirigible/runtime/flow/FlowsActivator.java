package com.sap.dirigible.runtime.flow;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class FlowsActivator implements BundleActivator {

	private static BundleContext context;
	
	@Override
	public void start(BundleContext context) throws Exception {
		FlowsActivator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static BundleContext getContext() {
		return context;
	}

}
