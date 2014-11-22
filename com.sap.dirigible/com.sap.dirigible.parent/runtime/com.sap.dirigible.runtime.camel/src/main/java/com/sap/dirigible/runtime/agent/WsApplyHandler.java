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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerRegistry;
import org.apache.cxf.jaxws.EndpointImpl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.dirigible.runtime.logger.Logger;

public class WsApplyHandler extends AbstractApplyHandler {

	private static final String ENDPOINT_WITH_PROCESSOR_TYPE_S_AND_PROCESSOR_S_AND_FORMAT_S_AND_Q_NAME_S_AT_ADDRESS_S_HAS_BEEN_PUBLISHED_SUCCESSFULLY = Messages.getString("WsApplyHandler.ENDPOINT_WITH_PROCESSOR_TYPE_S_AND_PROCESSOR_S_AND_FORMAT_S_AND_Q_NAME_S_AT_ADDRESS_S_HAS_BEEN_PUBLISHED_SUCCESSFULLY"); //$NON-NLS-1$

	private static final String APPLY_CONFIGURATION = "applyConfiguration"; //$NON-NLS-1$

	private static final String APPLY_CONFIGURATION_FOR_WEB_SERVICE = Messages.getString("WsApplyHandler.APPLY_CONFIGURATION_FOR_WEB_SERVICE"); //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(WsApplyHandler.class.getCanonicalName());

	private static final String PARAMETER_PROCESSOR = "processor"; //$NON-NLS-1$
	private static final String PARAMETER_PROCESSOR_TYPE = "processorType"; //$NON-NLS-1$
	private static final String PARAMETER_SERVICE_NAME = "serviceName"; //$NON-NLS-1$
	private static final String PARAMETER_WSDL = "wsdl"; //$NON-NLS-1$
	private static final String PARAMETER_ADDRESS = "address"; //$NON-NLS-1$

	public WsApplyHandler(ConfigurationAgent configurationAgent, String remote) {
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

			// boolean noLoadErrors = true;
			// String errorMessage = "";
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
				addWebService(remoteFile, request);
			}

		} catch (MalformedURLException e) {
			logger.error(APPLY_CONFIGURATION, e);
			throw new IOException(String.format(APPLY_CONFIGURATION_FOR_WEB_SERVICE, e.getMessage()), e);
		} catch (Exception e) {
			logger.error(APPLY_CONFIGURATION, e);
			throw new IOException(String.format(APPLY_CONFIGURATION_FOR_WEB_SERVICE, e.getMessage()), e);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ APPLY_CONFIGURATION);

	}

	private void addWebService(String remoteFile, HttpServletRequest request)
			throws IOException {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "addWebService"); //$NON-NLS-1$

		InputStream wsMetaDataIn = getRemoteFile(remoteFile, request);

		// {"address":"/publish_address","wsdl":"/projectXXX/fileXXX.wsdl","serviceName":"{http://example.com}ServiceXXX","processorType":"CAMEL","processor":"direct:camel_idXXX"}

		String wsMetaData = IOUtils.toString(wsMetaDataIn);

		logger.debug("WS Metadata: " + wsMetaData); //$NON-NLS-1$

		JsonParser parser = new JsonParser();
		JsonObject wsService = (JsonObject) parser.parse(wsMetaData);
		String address = wsService.get(PARAMETER_ADDRESS).getAsString();
		String wsdl = wsService.get(PARAMETER_WSDL).getAsString();
		String serviceName = wsService.get(PARAMETER_SERVICE_NAME)
				.getAsString();
		QName serviceQName = QName.valueOf(serviceName);
		String processorType = wsService.get(PARAMETER_PROCESSOR_TYPE)
				.getAsString();
		String processor = wsService.get(PARAMETER_PROCESSOR).getAsString();
		InputStream wsdlIn = getRemoteFile(wsdl, request);
		File wsdlFile = createTempWsdlFile(wsdl, wsdlIn);

		try {
			// create provider service implementor - retrieve and redirect the
			// message
			Object implementor = new WsEndpointProviderImplementor(
					getConfigurationAgent(), processorType, processor);

			// stop the endpoint if already published
			stopIfPublished(address);

			EndpointImpl jaxwsEndpoint = new EndpointImpl(
					CXFBusFactory.getDefaultBus(), implementor, (String) null,
					wsdlFile.getCanonicalPath());
			jaxwsEndpoint.setServiceName(serviceQName);
			jaxwsEndpoint.setAddress(address);
			jaxwsEndpoint.getProperties().put(
					ConfigurationCommands.PROPERTY_REPOSITORY_LOCATION,
					remoteFile);
			jaxwsEndpoint.getProperties().put(
					ConfigurationCommands.PROPERTY_EXPOSED_AT, new Date());
			jaxwsEndpoint.publish();

			logger.debug(String
					.format(ENDPOINT_WITH_PROCESSOR_TYPE_S_AND_PROCESSOR_S_AND_FORMAT_S_AND_Q_NAME_S_AT_ADDRESS_S_HAS_BEEN_PUBLISHED_SUCCESSFULLY,
							processorType, processor,
							serviceQName.toString(), address));
		} finally {
			clean(wsdlFile);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "addWebService"); //$NON-NLS-1$
	}

	private void clean(File wsdlFile) {
		try {
			// TODO check this
			wsdlFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopIfPublished(String address) {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "stopIfPublished"); //$NON-NLS-1$

		// Look up for all available endpoints registered on the bus
		Bus bus = CXFBusFactory.getDefaultBus();
		ServerRegistry serverRegistry = bus.getExtension(ServerRegistry.class);
		List<Server> servers = serverRegistry.getServers();
		for (Iterator<Server> iterator = servers.iterator(); iterator.hasNext();) {
			Server server = iterator.next();
			// TODO check whether "endsWith" is correct
			if (server.getEndpoint().getEndpointInfo().getAddress()
					.endsWith(address)) {
				server.stop();
				server.destroy();
				break;
			}
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "stopIfPublished"); //$NON-NLS-1$

	}

	// TODO investigate another way to set the WSDL
	private File createTempWsdlFile(String wsdl, InputStream wsdlIn)
			throws IOException {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createTempWsdlFile"); //$NON-NLS-1$

		String tempFileName = wsdl;
		if (wsdl.lastIndexOf('/') != -1
				&& wsdl.length() > wsdl.lastIndexOf('/') + 1) {
			tempFileName = wsdl.substring(wsdl.lastIndexOf('/') + 1);
		}
		if (tempFileName.lastIndexOf('.') != -1) {
			tempFileName = tempFileName.substring(0,
					tempFileName.lastIndexOf('.'));
		}

		File wsdlFile = File.createTempFile(tempFileName, ".wsdl"); //$NON-NLS-1$
		writeToFile(new InputStreamReader(wsdlIn), new FileWriter(wsdlFile));

		logger.debug("Temp Wsdl File: " + wsdlFile.getCanonicalPath()); //$NON-NLS-1$

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "createTempWsdlFile"); //$NON-NLS-1$

		return wsdlFile;
	}

}
