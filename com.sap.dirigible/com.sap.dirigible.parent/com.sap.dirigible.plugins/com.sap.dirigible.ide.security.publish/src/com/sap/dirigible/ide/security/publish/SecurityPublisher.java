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

package com.sap.dirigible.ide.security.publish;

import static com.sap.dirigible.ide.security.publish.SecurityConstants.REGISTYRY_PUBLISH_LOCATION;
import static com.sap.dirigible.ide.security.publish.SecurityConstants.SC_CONTENT_FOLDER;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.rap.rwt.RWT;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.publish.AbstractPublisher;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.ext.security.SecurityUpdater;

public class SecurityPublisher extends AbstractPublisher implements IPublisher {

	private static final Logger logger = Logger
			.getLogger(SecurityPublisher.class);

	public SecurityPublisher() {
		super();
	}

	@Override
	public void publish(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(
					project, REGISTYRY_PUBLISH_LOCATION);
			final IFolder sourceFolder = getSourceFolder(project,
					SC_CONTENT_FOLDER);
			copyAllFromTo(sourceFolder, targetContainer);

			List<String> knownFiles = new ArrayList<String>();
			SecurityUpdater securityUpdater = new SecurityUpdater(
					RepositoryFacade.getInstance().getRepository(),
					DataSourceFacade.getInstance().getDataSource(),
					REGISTYRY_PUBLISH_LOCATION);
			securityUpdater.enumerateKnownFiles(targetContainer, knownFiles);
			securityUpdater.executeUpdate(knownFiles, RWT.getRequest());
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
		return ICommonConstants.ARTIFACT_TYPE.SECURITY_CONSTRAINTS;
	}

	@Override
	public boolean recognizedFile(IFile file) {
		if (checkFolderType(file)) {
			if (SecurityUpdater.EXTENSION_ACCESS.equals("." //$NON-NLS-1$
					+ file.getFileExtension())) {
				return true;
			}
		}
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
}
