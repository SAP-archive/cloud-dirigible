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

package com.sap.dirigible.runtime.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import com.sap.dirigible.runtime.logger.Logger;

public abstract class TaskManager implements Runnable {

	private static final Logger logger = Logger.getLogger(TaskManager.class);

	private List<IRunnableTask> runnableTasks = Collections
			.synchronizedList(new ArrayList<IRunnableTask>());

	@Override
	public void run() {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$

		try {
			startRunnableTasks();
			logger.info("All tasks were performed successfylly: " //$NON-NLS-1$
					+ runnableTasks.size());
		} catch (Exception e) {
			logger.error("Task Manager error", e);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$
	}

	// public List<IRunnableTask> getRunnableTasks() {
	// return runnableTasks;
	// }

	public void registerRunnableTask(IRunnableTask runnableTask) {
		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "registerRunnableTask"); //$NON-NLS-1$

		runnableTasks.add(runnableTask);
		logger.debug("registered runnable task: " + runnableTask.getName()); //$NON-NLS-1$

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "registerRunnableTask"); //$NON-NLS-1$
	}

	public void unregisterRunnableTask(IRunnableTask runnableTask) {
		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "unregisterRunnableTask"); //$NON-NLS-1$

		runnableTasks.remove(runnableTask);
		logger.debug("unregistered runnable task: " + runnableTask.getName()); //$NON-NLS-1$

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "unregisterRunnableTask"); //$NON-NLS-1$
	}

	private void startRunnableTasks() throws ServletException {

		for (Iterator<IRunnableTask> iterator = runnableTasks.iterator(); iterator.hasNext();) {
			IRunnableTask task = iterator.next();
			try {
				logger.debug("Staring Task: " + task.getName() + "..."); //$NON-NLS-1$ //$NON-NLS-2$
				task.start();
				logger.debug("Task: " + task.getName() + " - " //$NON-NLS-1$ //$NON-NLS-2$
						+ "ended."); //$NON-NLS-1$
			} catch (Exception e) {
				logger.error("Task Manager error for Task: " + task.getName(), e);
			}
		}
	}
}
