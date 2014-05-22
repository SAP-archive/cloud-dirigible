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

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeBridgeUtils {
	
	private static final Logger logger = LoggerFactory
			.getLogger(RuntimeBridgeUtils.class.getCanonicalName());

	public static final String DIRIGIBLE_RUNTIME_BRIDGE = "dirigible.runtime.bridge"; //$NON-NLS-1$
	public static final String DIRIGIBLE_CONFIG_AGENT = "dirigible.config.agent"; //$NON-NLS-1$
	
	/**
	 * Bind this instance to the local Environment
	 */
	public static void bindToEnvironment(String name, Object obj) {
		System.getProperties().put(name, obj);
		logger.info(name + " bound to Environment: " + obj.hashCode()); //$NON-NLS-1$
	}
	
	/**
	 * Retrieve the RuntimeBridge(PropertyChangeSupport) from the target server environment
	 * 
	 * @return
	 * @throws NamingException
	 */
	public static PropertyChangeSupport lookupRuntimeBridge() {
		return (PropertyChangeSupport) System.getProperties().get(DIRIGIBLE_RUNTIME_BRIDGE);
	}
	
	public static ConfigurationAgent getConfigurationAgent() {
		return (ConfigurationAgent) System.getProperties().get(DIRIGIBLE_CONFIG_AGENT);
	}
	
	public static String createApplyRoutesMessage(String routes)
			throws IOException {

		logger.debug("entering: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createApplyRoutesMessage"); //$NON-NLS-1$
		
		String content = createMessage(ConfigurationCommands.ACTION_ROUTES, routes);

		logger.debug("exiting: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createApplyRoutesMessage"); //$NON-NLS-1$

		return content;
	}
	
	public static String createApplyWsMessage(String ws) throws IOException {

		logger.debug("entering: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createApplyWsMessage"); //$NON-NLS-1$

		String content = createMessage(ConfigurationCommands.ACTION_WS, ws);
		
		logger.debug("exiting: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createApplyWsMessage"); //$NON-NLS-1$

		return content;
	}
	
	public static String createMessage(String action, String location) throws IOException {

		logger.debug("entering: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createApplyWsMessage"); //$NON-NLS-1$

		Properties props = new Properties();
		props.put(ConfigurationCommands.PROPERTY_ACTION,
				action);
		props.put(ConfigurationCommands.PROPERTY_LOCATION, location);
		StringWriter writer = new StringWriter();
		props.store(writer, ConfigurationCommands.COMMENT_ACTION);
		String content = writer.getBuffer().toString();

		logger.debug("exiting: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createApplyWsMessage"); //$NON-NLS-1$

		return content;
	}
	
	public static void pushMessage(String content, HttpServletRequest request)
			throws IOException {
		
		logger.debug("entering: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "pushMessage"); //$NON-NLS-1$
		
		logger.info("Received at " + new Date() + ": " + content); //$NON-NLS-1$ //$NON-NLS-2$

		Properties props = loadProps(content);

		if (ConfigurationCommands.ACTION_LOAD.equals(props
				.getProperty(ConfigurationCommands.PROPERTY_ACTION))) {
			logger.debug("[Action: " + ConfigurationCommands.ACTION_LOAD); //$NON-NLS-1$
			// DEPRECATED the full reload is excluded

			// // LOAD action message has been received. Create configurator
			// and apply the new context
			// ConfigurationApplyHandler configurationApplyer =
			// new ConfigurationApplyHandler(this.configurationAgent,
			// props.getProperty(ConfigurationCommands.PROPERTY_LOCATION),
			// this.configurationAgent.getLocalConfigurationFile());
			// configurationApplyer.applyConfiguration();
		} else if (ConfigurationCommands.ACTION_ROUTES.equals(props
				.getProperty(ConfigurationCommands.PROPERTY_ACTION))) {
			// Load Route control message has been received
			logger.debug("[Action: " + ConfigurationCommands.ACTION_ROUTES); //$NON-NLS-1$
			RouteApplyHandler routeApplyHandler = new RouteApplyHandler(
					getConfigurationAgent(),
					props.getProperty(ConfigurationCommands.PROPERTY_LOCATION));
			routeApplyHandler.applyConfiguration(request);
		} else if (ConfigurationCommands.ACTION_WS.equals(props
				.getProperty(ConfigurationCommands.PROPERTY_ACTION))) {
			// Load WS control message has been received
			logger.debug("[" + ConfigurationCommands.ACTION_WS); //$NON-NLS-1$
			WsApplyHandler wsApplyHandler = new WsApplyHandler(
					getConfigurationAgent(),
					props.getProperty(ConfigurationCommands.PROPERTY_LOCATION));
			wsApplyHandler.applyConfiguration(request);
		} // else other actions ...
		
		logger.debug("exiting: " + RuntimeBridgeUtils.class.getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "pushMessage"); //$NON-NLS-1$
	}

	// utility method
	private static Properties loadProps(String content) throws IOException {
		StringReader reader = new StringReader(content);
		Properties props = new Properties();
		props.load(reader);
		return props;
	}

}
