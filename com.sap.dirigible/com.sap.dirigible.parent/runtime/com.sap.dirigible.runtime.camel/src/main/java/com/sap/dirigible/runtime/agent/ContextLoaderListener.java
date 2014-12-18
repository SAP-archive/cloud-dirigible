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

package com.sap.dirigible.runtime.agent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sap.dirigible.runtime.logger.Logger;

public class ContextLoaderListener implements ServletContextListener {
	
	private static final Logger logger = Logger.getLogger(ContextLoaderListener.class.getCanonicalName());

	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "contextInitialized"); //$NON-NLS-1$

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new RepositorySynchronizer(), 1, 5,
				TimeUnit.MINUTES);
		
		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "contextInitialized"); //$NON-NLS-1$
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
