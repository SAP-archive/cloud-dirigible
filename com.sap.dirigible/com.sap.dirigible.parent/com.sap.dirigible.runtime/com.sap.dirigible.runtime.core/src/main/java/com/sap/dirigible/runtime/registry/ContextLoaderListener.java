/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.runtime.registry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.memory.MemoryLogCleanupTask;
import com.sap.dirigible.runtime.memory.MemoryLogTask;
import com.sap.dirigible.runtime.metrics.AccessLogCleanupTask;
import com.sap.dirigible.runtime.metrics.AccessLogLocationsSynchronizer;
import com.sap.dirigible.runtime.repository.RepositoryHistoryCleanupTask;
import com.sap.dirigible.runtime.search.RebuildSearchIndexTask;
import com.sap.dirigible.runtime.search.UpdateSearchIndexTask;
import com.sap.dirigible.runtime.task.TaskManagerLong;
import com.sap.dirigible.runtime.task.TaskManagerMedium;
import com.sap.dirigible.runtime.task.TaskManagerShort;

public class ContextLoaderListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(ContextLoaderListener.class);

	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "contextInitialized"); //$NON-NLS-1$

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new SecuritySynchronizer(), 1, 1, TimeUnit.MINUTES);

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(TaskManagerShort.getInstance(), 10, 10, TimeUnit.SECONDS);

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(TaskManagerMedium.getInstance(), 1, 1, TimeUnit.MINUTES);

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(TaskManagerLong.getInstance(), 1, 1, TimeUnit.HOURS);

		registerRunnableTasks();

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "contextInitialized"); //$NON-NLS-1$
	}

	private void registerRunnableTasks() {
		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "registerRunnableTasks"); //$NON-NLS-1$

		// short
		AccessLogLocationsSynchronizer accessLogLocationsSynchronizer = new AccessLogLocationsSynchronizer();
		TaskManagerShort.getInstance().registerRunnableTask(accessLogLocationsSynchronizer);

		// medium
		MemoryLogTask memoryLogTask = new MemoryLogTask();
		TaskManagerMedium.getInstance().registerRunnableTask(memoryLogTask);

		UpdateSearchIndexTask updateSearchIndexTask = new UpdateSearchIndexTask();
		TaskManagerLong.getInstance().registerRunnableTask(updateSearchIndexTask);

		// long
		AccessLogCleanupTask accessLogCleanupTask = new AccessLogCleanupTask();
		TaskManagerLong.getInstance().registerRunnableTask(accessLogCleanupTask);

		RepositoryHistoryCleanupTask historyCleanupTask = new RepositoryHistoryCleanupTask();
		TaskManagerLong.getInstance().registerRunnableTask(historyCleanupTask);

		MemoryLogCleanupTask memoryLogCleanupTask = new MemoryLogCleanupTask();
		TaskManagerLong.getInstance().registerRunnableTask(memoryLogCleanupTask);

		RebuildSearchIndexTask rebuildSearchIndexTask = new RebuildSearchIndexTask();
		TaskManagerLong.getInstance().registerRunnableTask(rebuildSearchIndexTask);

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "registerRunnableTasks"); //$NON-NLS-1$
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "contextDestroyed"); //$NON-NLS-1$

		scheduler.shutdownNow();

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "contextDestroyed"); //$NON-NLS-1$

	}

}
