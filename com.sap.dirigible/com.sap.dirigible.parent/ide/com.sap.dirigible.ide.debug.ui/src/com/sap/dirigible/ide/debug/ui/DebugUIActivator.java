package com.sap.dirigible.ide.debug.ui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DebugUIActivator implements BundleActivator {

	private static BundleContext context;
	
	@Override
	public void start(BundleContext context) throws Exception {
		DebugUIActivator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//
	}

	public static BundleContext getContext() {
		return context;
	}
}
