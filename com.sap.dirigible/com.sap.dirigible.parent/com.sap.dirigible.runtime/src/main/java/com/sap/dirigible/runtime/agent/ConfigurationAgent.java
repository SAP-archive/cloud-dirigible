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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Endpoint;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.runtime.repository.RepositoryFacade;


public class ConfigurationAgent extends HttpServlet implements PropertyChangeListener {
	
	private static final String SYNCHRONIZATION_FAILD = Messages.getString("ConfigurationAgent.SYNCHRONIZATION_FAILD"); //$NON-NLS-1$
	private static final String WEB_SERVICE_S_FAILED_DURING_SYNCHRONIZATION = Messages.getString("ConfigurationAgent.WEB_SERVICE_S_FAILED_DURING_SYNCHRONIZATION"); //$NON-NLS-1$
	private static final String ROUTE_S_FAILED_DURING_SYNCHRONIZATION = Messages.getString("ConfigurationAgent.ROUTE_S_FAILED_DURING_SYNCHRONIZATION"); //$NON-NLS-1$

	private static final String CANNOT_STOP_AND_REMOVE_RUNNING_SERVICES_DURING_FORCE_RELOAD = Messages.getString("ConfigurationAgent.CANNOT_STOP_AND_REMOVE_RUNNING_SERVICES_DURING_FORCE_RELOAD"); //$NON-NLS-1$
	private static final String CANNOT_GET_THE_NAME_OF_THE_SERVER_HOST = Messages.getString("ConfigurationAgent.CANNOT_GET_THE_NAME_OF_THE_SERVER_HOST"); //$NON-NLS-1$
	private static final String CONTEXT_CONTEXT_NOT_INITIALIZED = Messages.getString("ConfigurationAgent.CONTEXT_CONTEXT_NOT_INITIALIZED"); //$NON-NLS-1$
	private static final String CANNOT_LOAD_THE_ALREADY_PUBLISHED_ENDPOINTS = Messages.getString("ConfigurationAgent.CANNOT_LOAD_THE_ALREADY_PUBLISHED_ENDPOINTS"); //$NON-NLS-1$
	
	private static final long serialVersionUID = -9044881423443856880L;
	
	private static final Logger logger = LoggerFactory
			.getLogger(ConfigurationAgent.class.getCanonicalName());

	public static final String SYSTEM_USER = "SYSTEM"; //$NON-NLS-1$

	// TODO to be defined, reviewed, approved and documented
	private String configurationRoot;// = "/db/cxf/";

	// TODO to be taken via JMX later
	private String serverInstanceId;

	private Map<String, Endpoint> endpoints = new HashMap<String, Endpoint>();

	private XmlWebApplicationContext applicationContext;

	private String consumerName;

	private String localConfigurationFile;

	private boolean initialized = false;

	@Override
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "init"); //$NON-NLS-1$

		initializeRuntimeBridge();
		
		applicationContext = (XmlWebApplicationContext) WebApplicationContextUtils
				.getWebApplicationContext(sc.getServletContext());

		// bind Configuration Agent to the local Environment
		RuntimeBridgeUtils.bindToEnvironment(RuntimeBridgeUtils.DIRIGIBLE_CONFIG_AGENT, this);

		this.localConfigurationFile = getServletContext().getRealPath(
				ConfigurationCommands.LOCAL_CONFIGURATION_FILE);

		logger.info("Context initialization started..."); //$NON-NLS-1$

		// initialize instances
		try {
			initialize();
			logger.info("Context initialization finished."); //$NON-NLS-1$
			try {
				forceLoadEndpoints(null);
			} catch (IOException e) {
				logger.error(CANNOT_LOAD_THE_ALREADY_PUBLISHED_ENDPOINTS, e);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.warn(CONTEXT_CONTEXT_NOT_INITIALIZED);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "init"); //$NON-NLS-1$

	}

	public CamelContext getCamelContext() {
		return (CamelContext) applicationContext.getBean("camel"); //$NON-NLS-1$
	}
	
	public String getConfigurationRoot() {
		return configurationRoot;
	}

	public void setConfigurationRoot(String configurationRoot) {
		this.configurationRoot = configurationRoot;
	}

	public IRepository getRepository(HttpServletRequest request) {
		return RepositoryFacade.getInstance().getRepository(request);
	}

	public String getServerInstanceId() {
		return serverInstanceId;
	}

	public void setServerInstanceId(String serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}

	public Map<String, Endpoint> getEndpoints() {
		return endpoints;
	}

	/**
	 * Initialize parameters, repository, messaging, etc.
	 * 
	 */
	public void initialize() {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "initialize"); //$NON-NLS-1$

		initialized = false;

		String localHostName = "unknown"; //$NON-NLS-1$
		try {
			localHostName = ConfigurationUtils.formatName(InetAddress
					.getLocalHost().getCanonicalHostName());
		} catch (UnknownHostException e) {
			logger.error(CANNOT_GET_THE_NAME_OF_THE_SERVER_HOST, e);
		}
		serverInstanceId = localHostName;
		consumerName = ConfigurationUtils
				.generateConsumerName(serverInstanceId);

		initialized = true;

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "initialize"); //$NON-NLS-1$

	}

	public String forceLoadEndpoints(HttpServletRequest request)
			throws IOException {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "forceLoadEndpoints"); //$NON-NLS-1$

		String message = null;
		try {
			stopWebServices();
			stopRoutes();
			message = " ";
		} catch (Exception e) {
			logger.error(
					CANNOT_STOP_AND_REMOVE_RUNNING_SERVICES_DURING_FORCE_RELOAD,
					e);
			message = CANNOT_STOP_AND_REMOVE_RUNNING_SERVICES_DURING_FORCE_RELOAD + " " + e.getMessage();
		}

		synchronizeEndpoints(request);

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "forceLoadEndpoints"); //$NON-NLS-1$

		return message;
	}

	private void synchronizeEndpoints(HttpServletRequest request)
			throws IOException {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "synchronizeEndpoints"); //$NON-NLS-1$

		final ICollection targetContainer = getTargetProjectContainer(
				ConfigurationCommands.REPOSITORY_INTEGRATION_DEPLOY_PATH,
				request);
		Map<String, Date> knownFiles = new HashMap<String, Date>();
		enumerateKnownFiles(targetContainer, knownFiles);
		for (Iterator<Map.Entry<String, Date>> iterator = knownFiles.entrySet()
				.iterator(); iterator.hasNext();) {
			Map.Entry<String, Date> endpointLocation = iterator.next();
			String endpointLocationPath = endpointLocation.getKey();
			if (endpointLocationPath
					.endsWith(ConfigurationCommands.EXTENSION_ROUTE)) {
				try {
					Date repositoryDate = endpointLocation.getValue();
					Date endpointDate = getRunningRoute(endpointLocationPath);
					logger.debug(endpointLocationPath
							+ " with repositoryDate: " + repositoryDate //$NON-NLS-1$
							+ " corresponding endpointDate: " + endpointDate); //$NON-NLS-1$
					if (endpointDate == null
							|| endpointDate.before(repositoryDate)) {
						RouteApplyHandler routeApplyHandler = new RouteApplyHandler(
								this, endpointLocationPath);
						routeApplyHandler.applyConfiguration(request);
					}
				} catch (Exception e) {
					logger.error(String.format(
							ROUTE_S_FAILED_DURING_SYNCHRONIZATION,
							endpointLocationPath), e);
				}
			} else if (endpointLocationPath
					.endsWith(ConfigurationCommands.EXTENSION_WS)) {
				try {
					Date repositoryDate = endpointLocation.getValue();
					Date endpointDate = getRunningWs(endpointLocationPath);
					logger.debug(endpointLocationPath
							+ " with repositoryDate: " + repositoryDate //$NON-NLS-1$
							+ " corresponding endpointDate: " + endpointDate); //$NON-NLS-1$
					if (endpointDate == null
							|| endpointDate.before(repositoryDate)) {
						WsApplyHandler wsApplyHandler = new WsApplyHandler(
								this, endpointLocationPath);
						wsApplyHandler.applyConfiguration(request);
					}
				} catch (Exception e) {
					logger.error(String.format(
							WEB_SERVICE_S_FAILED_DURING_SYNCHRONIZATION,
							endpointLocationPath), e);
				}
			}
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "synchronizeEndpoints"); //$NON-NLS-1$

	}

	private Date getRunningRoute(String location) {
		List<Route> routes = getCamelContext().getRoutes();
		logger.debug("getRunningRoute for location: " + location); //$NON-NLS-1$
		for (Iterator<Route> iterator = routes.iterator(); iterator.hasNext();) {
			Route route = iterator.next();
			logger.debug("Route: " //$NON-NLS-1$
					+ route.getId()
					+ " with properites: " //$NON-NLS-1$
					+ route.getProperties().get(
							ConfigurationCommands.PROPERTY_REPOSITORY_LOCATION)
					+ "|" //$NON-NLS-1$
					+ route.getProperties().get(
							ConfigurationCommands.PROPERTY_EXPOSED_AT));
			if (location.equals(route.getProperties().get(
					ConfigurationCommands.PROPERTY_REPOSITORY_LOCATION))) {
				logger.debug("getRunningRoute found: " + route.getId()); //$NON-NLS-1$
				return (Date) route.getProperties().get(
						ConfigurationCommands.PROPERTY_EXPOSED_AT);
			}
		}
		logger.debug("getRunningRoute not found"); //$NON-NLS-1$
		return null;
	}

	private Date getRunningWs(String location) {
		// TODO inspect where are the properties set?
		// Bus bus = CXFBusFactory.getDefaultBus();
		// ServerRegistry serverRegistry =
		// bus.getExtension(ServerRegistry.class);
		// List<Server> servers = serverRegistry.getServers();
		// for (Iterator<Server> iterator = servers.iterator();
		// iterator.hasNext();) {
		// Server server = iterator.next();
		// // if (location.equals(((JaxWsEndpointImpl)
		// server.getEndpoint()).getProperties().get(ConfigurationCommands.PROPERTY_REPOSITORY_LOCATION)))
		// {
		// // return (Date) ((JaxWsEndpointImpl)
		// server.getEndpoint()).getProperties().get(ConfigurationCommands.PROPERTY_EXPOSED_AT);
		// // }
		// }
		return null;
	}

	private void stopRoutes() throws Exception {
		List<Route> routes = getCamelContext().getRoutes();
		for (Iterator<Route> iterator = routes.iterator(); iterator.hasNext();) {
			Route route = iterator.next();
			getCamelContext().stopRoute(route.getId());
			getCamelContext().removeRoute(route.getId());
		}
	}

	private void stopWebServices() {
		Bus bus = CXFBusFactory.getDefaultBus();
		ServerRegistry serverRegistry = bus.getExtension(ServerRegistry.class);
		List<Server> servers = serverRegistry.getServers();
		for (Iterator<Server> iterator = servers.iterator(); iterator.hasNext();) {
			Server server = iterator.next();
			server.stop();
			server.destroy();
		}
	}

	protected ICollection getTargetProjectContainer(String registryLocation,
			HttpServletRequest request) throws IOException {
		final ICollection publishContainer = getRepository(request)
				.getCollection(registryLocation);
		publishContainer.create();
		return publishContainer;
	}

	private void enumerateKnownFiles(ICollection collection,
			Map<String, Date> integrationDefinitions) throws IOException {
		if (collection.exists()) {
			List<IResource> resources = collection.getResources();
			for (Iterator<IResource> iterator = resources.iterator(); iterator
					.hasNext();) {
				IResource resource = iterator.next();
				if (resource != null && resource.getName() != null) {
					if (resource.getName().endsWith(
							ConfigurationCommands.EXTENSION_ROUTE)
							|| resource.getName().endsWith(
									ConfigurationCommands.EXTENSION_WS)) {
						String fullPath = collection
								.getPath()
								.substring(
										ConfigurationCommands.REPOSITORY_INTEGRATION_DEPLOY_PATH
												.length())
								+ IRepository.SEPARATOR + resource.getName();
						integrationDefinitions.put(fullPath, resource
								.getInformation().getModifiedAt());
						logger.debug(fullPath + " known by synchronization"); //$NON-NLS-1$
					}
				}
			}

			List<ICollection> collections = collection.getCollections();
			for (Iterator<ICollection> iterator = collections.iterator(); iterator
					.hasNext();) {
				ICollection subCollection = iterator.next();
				enumerateKnownFiles(subCollection, integrationDefinitions);
			}
		}
	}

	public XmlWebApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			applicationContext = (XmlWebApplicationContext) WebApplicationContextUtils
					.getWebApplicationContext(getServletContext());
		}
		return applicationContext;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public String getLocalConfigurationFile() {
		return localConfigurationFile;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void synchronize(HttpServletRequest request) {
		try {
			synchronized (this.getClass()) {
				synchronizeEndpoints(request);
			}
		} catch (IOException e) {
			logger.error(SYNCHRONIZATION_FAILD, e);
		}
	}

	private void initializeRuntimeBridge() {
		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "initializeRuntimeBridge"); //$NON-NLS-1$
		
		PropertyChangeSupport runtimeBridge = RuntimeBridgeUtils.lookupRuntimeBridge();
		if (runtimeBridge == null) {
			runtimeBridge = new PropertyChangeSupport(new Object());
			RuntimeBridgeUtils.bindToEnvironment(RuntimeBridgeUtils.DIRIGIBLE_RUNTIME_BRIDGE, runtimeBridge);
		}
		runtimeBridge.addPropertyChangeListener(this);
		
		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "initializeRuntimeBridge"); //$NON-NLS-1$		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String commandId = event.getPropertyName();
		String clientId = (String) event.getOldValue();
		String commandBody = (String) event.getNewValue();
		
		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "propertyChange: receiving commandId: " + commandId + ", clientId: " + clientId + ", commandBody: " + commandBody); //$NON-NLS-1$
		
		if (commandId != null
				&& commandId.startsWith(ConfigurationCommands.ACTION)) {
			try {
				String message = RuntimeBridgeUtils.createMessage(commandId, commandBody);
				RuntimeBridgeUtils.pushMessage(message, null);
				sendCommand(ConfigurationCommands.STATUS_OK, clientId, "");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				sendCommand(ConfigurationCommands.STATUS_FAILED, clientId, e.getMessage());
			}
		}
		
		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "propertyChange: commandId: " + commandId + ", clientId: " + clientId + ", commandBody: " + commandBody); //$NON-NLS-1$
	}

	private void sendCommand(String commandId, String clientId, String commandBody) {
		PropertyChangeSupport runtimeBridge = RuntimeBridgeUtils.lookupRuntimeBridge();
		if (runtimeBridge != null) {
			logger.debug("sending ConfigurationAgent.sendCommand() with commandId: "
					+ commandId + ", clientId: " + clientId+ ", commandBody: " + commandBody);
			runtimeBridge.firePropertyChange(commandId, clientId, commandBody);
			
		}
	}
	
}
