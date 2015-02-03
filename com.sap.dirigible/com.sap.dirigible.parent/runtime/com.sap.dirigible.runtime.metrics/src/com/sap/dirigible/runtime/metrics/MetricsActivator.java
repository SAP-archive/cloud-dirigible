package com.sap.dirigible.runtime.metrics;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sap.dirigible.runtime.memory.MemoryLogCleanupTask;
import com.sap.dirigible.runtime.memory.MemoryLogTask;
import com.sap.dirigible.runtime.task.TaskManagerLong;
import com.sap.dirigible.runtime.task.TaskManagerMedium;
import com.sap.dirigible.runtime.task.TaskManagerShort;

public class MetricsActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		
//		SchedulerActivator.getSchedulerServlet().startSchedulers();
		
		// short
		AccessLogLocationsSynchronizer accessLogLocationsSynchronizer = new AccessLogLocationsSynchronizer();
		TaskManagerShort.getInstance().registerRunnableTask(accessLogLocationsSynchronizer);

		// medium
		MemoryLogTask memoryLogTask = new MemoryLogTask();
		TaskManagerMedium.getInstance().registerRunnableTask(memoryLogTask);
		
		// long
		AccessLogCleanupTask accessLogCleanupTask = new AccessLogCleanupTask();
		TaskManagerLong.getInstance().registerRunnableTask(accessLogCleanupTask);
		
		MemoryLogCleanupTask memoryLogCleanupTask = new MemoryLogCleanupTask();
		TaskManagerLong.getInstance().registerRunnableTask(memoryLogCleanupTask);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		

	}

}
