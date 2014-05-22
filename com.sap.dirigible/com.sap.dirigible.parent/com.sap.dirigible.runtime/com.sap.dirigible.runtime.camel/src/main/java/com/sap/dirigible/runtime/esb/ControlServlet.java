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

package com.sap.dirigible.runtime.esb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.runtime.agent.ConfigurationAgent;
import com.sap.dirigible.runtime.agent.RuntimeBridgeUtils;

/**
 * Servlet implementation class ControlServlet
 */
public abstract class ControlServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(ControlServlet.class.getCanonicalName());

	private static final long serialVersionUID = 1L;

	private ConfigurationAgent configurationAgent;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ControlServlet() {
		super();
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

	protected void beforeGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "beforeGet"); //$NON-NLS-1$

		initializeConfigurationAgent();

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "beforeGet"); //$NON-NLS-1$

	}

	public ConfigurationAgent getConfigurationAgent() {
		initializeConfigurationAgent();
		return configurationAgent;
	}

	// public ConfigurationChangeListener getConfigurationChangeListener() {
	// initializeListeners();
	// return configurationChangeListener;
	// }

}
