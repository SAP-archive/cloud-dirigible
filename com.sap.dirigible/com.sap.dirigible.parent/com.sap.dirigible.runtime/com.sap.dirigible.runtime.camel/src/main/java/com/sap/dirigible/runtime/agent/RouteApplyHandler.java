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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;

import com.sap.dirigible.runtime.logger.Logger;

public class RouteApplyHandler extends AbstractApplyHandler {

	private static final String INVALID_FORMAT_OF_ROUTE_IN_S = Messages.getString("RouteApplyHandler.INVALID_FORMAT_OF_ROUTE_IN_S"); //$NON-NLS-1$
	private static final String APPLY_CONFIGURATION_FOR_ROUTE = Messages.getString("RouteApplyHandler.APPLY_CONFIGURATION_FOR_ROUTE"); //$NON-NLS-1$
	private static final String APPLY_CONFIGURATION = "applyConfiguration"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(RouteApplyHandler.class);

	public RouteApplyHandler(ConfigurationAgent configurationAgent,
			String remote) {
		super(configurationAgent, remote);
		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "constructor"); //$NON-NLS-1$
		logger.debug("remote: " + remote); //$NON-NLS-1$
		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "constructor"); //$NON-NLS-1$
	}

	public void applyConfiguration(HttpServletRequest request)
			throws IOException {
		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ APPLY_CONFIGURATION);
		try {

			List<String> remoteFiles = new ArrayList<String>();
			// if ("*".equals(remote)) {
			// // for initial load or force reload
			// populateRemoteFiles(remoteFiles);
			// } else {
			remoteFiles.add(getRemote());
			// }

			// TODO check and remove running services w/o corresponding
			// repository content (deleted meanwhile)

			for (Iterator<String> iterator = remoteFiles.iterator(); iterator
					.hasNext();) {
				String remoteFile = iterator.next();

				// add the route
				addRoute(remoteFile, request);
			}

		} catch (MalformedURLException e) {
			logger.error(APPLY_CONFIGURATION, e);
			throw new IOException(String.format(APPLY_CONFIGURATION_FOR_ROUTE, e.getMessage()), e);
		} catch (Exception e) {
			logger.error(APPLY_CONFIGURATION, e);
			throw new IOException(String.format(APPLY_CONFIGURATION_FOR_ROUTE, e.getMessage()), e);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ APPLY_CONFIGURATION);

	}

	@SuppressWarnings("deprecation")
	private void addRoute(String remoteFile, HttpServletRequest request)
			throws Exception {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "addRoute"); //$NON-NLS-1$

		InputStream routeIn = getRemoteFile(remoteFile, request);

		List<RouteDefinition> routesDefinitions = null;
		try {
			routesDefinitions = getConfigurationAgent()
					.getCamelContext().loadRoutesDefinition(routeIn).getRoutes();
		} catch (Exception e) {
			throw new Exception(String.format(INVALID_FORMAT_OF_ROUTE_IN_S, remoteFile), e);
		}

		getConfigurationAgent().getCamelContext().addRouteDefinitions(
				routesDefinitions);

		for (Iterator<RouteDefinition> iterator = routesDefinitions.iterator(); iterator
				.hasNext();) {
			RouteDefinition routeDefinition = iterator.next();
			Route route = getConfigurationAgent().getCamelContext().getRoute(
					routeDefinition.getId());
			route.getProperties().put(
					ConfigurationCommands.PROPERTY_REPOSITORY_LOCATION,
					remoteFile);
			route.getProperties().put(
					ConfigurationCommands.PROPERTY_EXPOSED_AT, new Date());
			logger.debug("Added Route: " + route.getId() + " with properites: " //$NON-NLS-1$ //$NON-NLS-2$
					+ remoteFile + "|" + new Date()); //$NON-NLS-1$
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "addRoute"); //$NON-NLS-1$

	}

}
