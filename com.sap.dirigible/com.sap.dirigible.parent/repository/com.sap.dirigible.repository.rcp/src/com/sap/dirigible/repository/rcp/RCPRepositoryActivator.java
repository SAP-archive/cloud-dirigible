package com.sap.dirigible.repository.rcp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RCPRepositoryActivator implements BundleActivator {

	public static BundleContext context;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		context = bundleContext;
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		
	}

}
