package com.sap.dirigible.runtime.content;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sap.dirigible.runtime.scheduler.SchedulerActivator;

public class InitActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		ContentInitializerServlet contentInitializerServlet = new ContentInitializerServlet();
		contentInitializerServlet.registerInitRegister();
		SchedulerActivator.getSchedulerServlet().startSchedulers();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		

	}

}
