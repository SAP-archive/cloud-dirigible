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

package com.sap.dirigible.ide.extensions.publish;

import static com.sap.dirigible.ide.extensions.publish.ExtensionsConstants.REGISTYRY_PUBLISH_LOCATION;
import static com.sap.dirigible.ide.extensions.publish.ExtensionsConstants.ED_CONTENT_FOLDER;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.rap.rwt.RWT;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.publish.AbstractPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.ext.extensions.ExtensionUpdater;

public class ExtensionsPublisher extends AbstractPublisher implements IPublisher {

	private static final Logger logger = Logger
			.getLogger(ExtensionsPublisher.class);

	public ExtensionsPublisher() {
		super();
	}

	@Override
	public void publish(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(
					project, REGISTYRY_PUBLISH_LOCATION);
			final IFolder sourceFolder = getSourceFolder(project,
					ED_CONTENT_FOLDER);
			copyAllFromTo(sourceFolder, targetContainer);

			List<String> knownFiles = new ArrayList<String>();
			ExtensionUpdater extensionUpdater = new ExtensionUpdater(
					RepositoryFacade.getInstance().getRepository(),
					DataSourceFacade.getInstance().getDataSource(),
					REGISTYRY_PUBLISH_LOCATION);
			extensionUpdater.enumerateKnownFiles(targetContainer, knownFiles);
			extensionUpdater.executeUpdate(knownFiles, RWT.getRequest());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new PublishException(ex.getMessage(), ex);
		}
	}
	
	// no sandboxing for extension points
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
		return ICommonConstants.ARTIFACT_TYPE.EXTENSION_DEFINITIONS;
	}

	@Override
	public boolean recognizedFile(IFile file) {
		if (checkFolderType(file)) {
			if (ExtensionUpdater.EXTENSION_EXTENSION.equals("."+ file.getFileExtension()) //$NON-NLS-1$
				|| ExtensionUpdater.EXTENSION_EXTENSION_POINT.equals("."+ file.getFileExtension())) { //$NON-NLS-1$
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
