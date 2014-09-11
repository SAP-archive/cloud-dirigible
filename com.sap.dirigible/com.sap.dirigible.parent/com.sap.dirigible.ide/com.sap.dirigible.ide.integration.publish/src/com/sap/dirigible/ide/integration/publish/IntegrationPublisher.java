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

package com.sap.dirigible.ide.integration.publish;

import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.ACTION_ROUTES;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.ACTION_WS;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.EXTENSION_ROUTE;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.EXTENSION_WS;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.EXTENSION_XSL;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.EXTENSION_XSLT;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.IS_CONTENT_FOLDER;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.IS_REGISTYRY_PUBLISH_LOCATION;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.STATUS_FAILED;
import static com.sap.dirigible.ide.integration.publish.IntegrationConstants.STATUS_OK;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.rap.rwt.RWT;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.publish.AbstractPublisher;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IResource;

public class IntegrationPublisher extends AbstractPublisher implements
		IPublisher, PropertyChangeListener {

	private static final String SOME_OF_THE_SERVICES_HAVE_NOT_BEEN_PUBLISHED_SUCCESSFULLY = Messages.IntegrationPublisher_SOME_OF_THE_SERVICES_HAVE_NOT_BEEN_PUBLISHED_SUCCESSFULLY;

	private static final Logger logger = Logger
			.getLogger(IntegrationPublisher.class);

	private boolean status = false;
	private String error = null;
	private String clientId = UUID.randomUUID().toString();
	
	public IntegrationPublisher() {
		super();
	}

	@Override
	public void publish(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(
					project, IS_REGISTYRY_PUBLISH_LOCATION);
			final IFolder sourceFolder = getSourceFolder(project,
					IS_CONTENT_FOLDER);
			copyAllFromTo(sourceFolder, targetContainer);

			 List<String> knownFiles = new ArrayList<String>();
			 enumerateKnownFiles(targetContainer, knownFiles);
			 notifyEsb(knownFiles);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new PublishException(ex.getMessage(), ex);
		}
	}
	
	// no sandboxing for integration services
	@Override
	public void activate(IProject project) throws PublishException {
		publish(project);
	}
	
	@Override
	public void activateFile(IFile file) throws PublishException {
		publish(file.getProject());		
	}

	@Override
	public String getFolderType() {
		return ICommonConstants.ARTIFACT_TYPE.INTEGRATION_SERVICES;
	}

	@Override
	public boolean recognizedFile(IFile file) {
		return false;
	}

	@Override
	public String getPublishedContainerMapping(IFile file) {
		return null;
	}
	
	@Override
	public String getActivatedContainerMapping(IFile file) {
		return null;
	}
	
	@Override
	public boolean isAutoActivationAllowed() {
		return false;
	}

	@Override
	protected String getSandboxLocation() {
		return null;
	}
	
	private void notifyEsb(List<String> knownFiles) throws PublishException {
		
		PropertyChangeSupport runtimeBridge = getRuntimeBridge(this);
		
		List<String> errors = new ArrayList<String>();
		if (runtimeBridge != null) {
			runtimeBridge.addPropertyChangeListener(this);
			
			for (Iterator<String> iterator = knownFiles.iterator(); iterator.hasNext();) {
				String integrationDefinition = iterator.next();
				if (getCommand(integrationDefinition) != null) {
					try {
						notifyESB(runtimeBridge, integrationDefinition, errors);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						errors.add(integrationDefinition + ": " + e.getMessage()); //$NON-NLS-1$
					}
				}
			}

			runtimeBridge.removePropertyChangeListener(this);
		}
		
		if (errors.size() > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Iterator<String> iterator = errors.iterator(); iterator.hasNext();) {
				String string = iterator.next();
				stringBuilder.append(string + '\n');
			}
			throw new PublishException(SOME_OF_THE_SERVICES_HAVE_NOT_BEEN_PUBLISHED_SUCCESSFULLY 
					+ stringBuilder.toString());
		}
	
	}

	private String getCommand(String integrationDefinition) {
		if (integrationDefinition.endsWith(EXTENSION_ROUTE)) {
			return ACTION_ROUTES;
		} else if (integrationDefinition.endsWith(EXTENSION_WS)) {
			return ACTION_WS;
		}
		return null;
	}

	private void notifyESB(PropertyChangeSupport runtimeBridge, String location, List<String> errors) throws PublishException {
		
		String commandId = getCommand(location);
		String commandBody = location;
		
		this.status = false;
		this.error = null;
		
		sendCommand(runtimeBridge, commandId, commandBody);
		waitForResponse();
        
        if (this.error != null 
        		&& !"".equals(error.trim()) //$NON-NLS-1$
        		&& !"OK".equals(error.trim())) { //$NON-NLS-1$
        	errors.add(location + ": " + error); //$NON-NLS-1$
        }
		
	}

	private void waitForResponse() {
		int wait = 0;
		final int maxWaits = 20;
		final int sleepTime = 500;

		while (wait < maxWaits) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			wait++;
			if (error != null
					|| status) {
				return;
			}
		}
		
	}

	private void sendCommand(final PropertyChangeSupport runtimeBridge, final String commandId, String commandBody) {
		if (runtimeBridge != null) {
			logger.debug("sending IntegrationPublisher.sendCommand() with commandId: " //$NON-NLS-1$
					+ commandId + ", clientId: " + clientId+ ", commandBody: " + commandBody); //$NON-NLS-1$ //$NON-NLS-2$
			runtimeBridge.firePropertyChange(commandId, clientId, commandBody);
		} else {
			logger.debug("sending IntegrationPublisher.sendCommand() failed - RuntimeBridge is not present - with commandId: " //$NON-NLS-1$
					+ commandId + ", clientId: " + clientId+ ", commandBody: " + commandBody); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	private static PropertyChangeSupport getRuntimeBridge(PropertyChangeListener listener) {
		PropertyChangeSupport runtimeBridge = (PropertyChangeSupport) RWT.getRequest().getSession()
				.getAttribute(CommonParameters.DIRIGIBLE_RUNTIME_BRIDGE);
		return runtimeBridge;
	}

	private void enumerateKnownFiles(ICollection collection, List<String> integrationDefinitions) throws IOException {
		if (collection.exists()) {
			List<IResource> resources = collection.getResources();
			for (Iterator<IResource> iterator = resources.iterator(); iterator.hasNext();) {
				IResource resource = iterator.next();
				if (resource != null && resource.getName() != null) {
					if (resource.getName().endsWith(EXTENSION_ROUTE)
							|| resource.getName().endsWith(EXTENSION_WS)
							|| resource.getName().endsWith(EXTENSION_XSL)
							|| resource.getName().endsWith(EXTENSION_XSLT)) {
						String fullPath = collection.getPath().substring(IS_REGISTYRY_PUBLISH_LOCATION.length()) + "/" + resource.getName(); //$NON-NLS-1$
						integrationDefinitions.add(fullPath);
					}
				}
			}
			
			List<ICollection> collections = collection.getCollections();
			for (Iterator<ICollection> iterator = collections.iterator(); iterator.hasNext();) {
				ICollection subCollection = iterator.next();
				enumerateKnownFiles(subCollection, integrationDefinitions);
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String commandId = event.getPropertyName();
		String clientId = (String) event.getOldValue();
		String commandBody = (String) event.getNewValue();
		if (this.clientId.equals(clientId)) {
			logger.debug("IntegrationPublisher.propertyChange() - received message via runtime for this client: " + clientId + " - cmmandId=" + commandId + ", commandBody=" + commandBody);
			this.status = false;
			if (STATUS_OK.equals(commandId)) {
				this.status = true;
				this.error = commandBody;
			} else if (STATUS_FAILED.equals(commandId)) {
				this.status = false;
				this.error = commandBody;
			}
			
		} else {
			logger.debug("IntegrationPublisher.propertyChange() - received message via runtime for another client: " + clientId); //$NON-NLS-1$
		}
		
	}
	
	
}
