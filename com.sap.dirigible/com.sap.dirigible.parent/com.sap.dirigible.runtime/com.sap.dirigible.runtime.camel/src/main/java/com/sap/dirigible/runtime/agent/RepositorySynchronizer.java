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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositorySynchronizer implements Runnable {

	private static final String INITIALIZATION_CONFIGURATION_AGENT_FAILED = Messages.getString("RepositorySynchronizer.INITIALIZATION_CONFIGURATION_AGENT_FAILED"); //$NON-NLS-1$

	private static final Logger logger = LoggerFactory
			.getLogger(RepositorySynchronizer.class.getCanonicalName());

	private ConfigurationAgent configurationAgent;

	@Override
	public void run() {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$

		try {
			initializeConfigurationAgent();
			if (configurationAgent != null) {
				configurationAgent.synchronize(null);
				logger.debug("Synchronization done at: " + new Date()); //$NON-NLS-1$
			}
		} catch (Exception e) {
			logger.error(INITIALIZATION_CONFIGURATION_AGENT_FAILED, e);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$
	}

	protected void initializeConfigurationAgent() {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "initializeConfigurationAgent"); //$NON-NLS-1$

		if (configurationAgent == null) {
			configurationAgent = RuntimeBridgeUtils.getConfigurationAgent();
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "initializeConfigurationAgent"); //$NON-NLS-1$

	}

}
