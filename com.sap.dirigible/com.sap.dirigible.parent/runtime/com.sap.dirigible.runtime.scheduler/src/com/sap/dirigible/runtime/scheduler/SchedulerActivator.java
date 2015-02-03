package com.sap.dirigible.runtime.scheduler;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class SchedulerActivator implements BundleActivator {
	
	static SchedulerServlet schedulerServlet;

	@Override
	public void start(BundleContext context) throws Exception {
		schedulerServlet = new SchedulerServlet();
		schedulerServlet.startSchedulers();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		schedulerServlet.stopSchedulers();
	}
	
	public static SchedulerServlet getSchedulerServlet() {
		return schedulerServlet;
	}

}
