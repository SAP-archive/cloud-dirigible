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

package com.sap.dirigible.ide.scripts.publish;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.publish.AbstractPublisher;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;

public class ScriptsPublisher extends AbstractPublisher implements IPublisher {

	private static final String DOT = "."; //$NON-NLS-1$

	public ScriptsPublisher() {
		super();
	}

	@Override
	public void publish(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(project,
					CommonParameters.SCRIPTING_REGISTRY_PUBLISH_LOCATION);
			final IFolder sourceFolder = getSourceFolder(project,
					CommonParameters.SCRIPTING_CONTENT_FOLDER);
			copyAllFromTo(sourceFolder, targetContainer);
		} catch (Exception ex) {
			throw new PublishException(ex.getMessage(), ex);
		}
	}

	@Override
	public void activate(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(project,
					CommonParameters.getScriptingContentSandbox());
			final IFolder sourceFolder = getSourceFolder(project,
					CommonParameters.SCRIPTING_CONTENT_FOLDER);
			copyAllFromTo(sourceFolder, targetContainer);
		} catch (Exception ex) {
			throw new PublishException(ex.getMessage(), ex);
		}
	}

	@Override
	public String getFolderType() {
		return ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	}

	@Override
	public boolean recognizedFile(IFile file) {
		if (checkFolderType(file)) {
			if (CommonParameters.JAVASCRIPT_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())
					|| CommonParameters.RUBY_SERVICE_EXTENSION
							.equals(DOT + file.getFileExtension())
					|| CommonParameters.GROOVY_SERVICE_EXTENSION.equals(DOT
							+ file.getFileExtension())
					|| CommonParameters.JAVA_SERVICE_EXTENSION.equals(DOT
							+ file.getFileExtension())
					|| CommonParameters.COMMAND_SERVICE_EXTENSION.equals(DOT
							+ file.getFileExtension())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getPublishedContainerMapping(IFile file) {
		if (CommonParameters.JAVASCRIPT_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.JAVASCRIPT_CONTAINER_MAPPING;
		}
		if (CommonParameters.RUBY_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.RUBY_CONTAINER_MAPPING;
		}
		if (CommonParameters.GROOVY_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.GROOVY_CONTAINER_MAPPING;
		}
		if (CommonParameters.JAVA_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.JAVA_CONTAINER_MAPPING;
		}
		if (CommonParameters.COMMAND_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.COMMAND_CONTAINER_MAPPING;
		}
		return null;
	}

	@Override
	public String getActivatedContainerMapping(IFile file) {
		if (CommonParameters.JAVASCRIPT_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.JAVASCRIPT_SANDBOX_MAPPING;
		}
		if (CommonParameters.RUBY_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.RUBY_SANDBOX_MAPPING;
		}
		if (CommonParameters.GROOVY_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.GROOVY_SANDBOX_MAPPING;
		}
		if (CommonParameters.JAVA_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.JAVA_SANDBOX_MAPPING;
		}
		if (CommonParameters.COMMAND_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.COMMAND_SANDBOX_MAPPING;
		}
		return null;
	}

	@Override
	public boolean isAutoActivationAllowed() {
		return true;
	}

	@Override
	protected String getSandboxLocation() {
		return CommonParameters.getScriptingContentSandbox();
	}

	@Override
	public String getDebugEndpoint(IFile file) {
		if (CommonParameters.JAVASCRIPT_SERVICE_EXTENSION.equals(DOT + file.getFileExtension())) {
			return CommonParameters.getServicesUrl()
					+ CommonParameters.JAVASCRIPT_DEBUG_CONTAINER_MAPPING
					+ generatePublishedPath(file);
		}
		return null;
	}

}
