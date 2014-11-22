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

package com.sap.dirigible.ide.template.ui.common;

import java.io.InputStream;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;
import com.sap.dirigible.ide.ui.common.validation.ValidationStatus;
import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.repository.api.ICommonConstants;

public abstract class GenerationModel {
	
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String COULD_NOT_OPEN_INPUT_STREAM_FOR = Messages.GenerationModel_COULD_NOT_OPEN_INPUT_STREAM_FOR;

	private static final String TEMPLATE_LOCATION_IS_EMPTY = Messages.GenerationModel_TEMPLATE_LOCATION_IS_EMPTY;

	private static final String RESOURCE_ALREADY_EXISTS_IN_THE_WORKSPACE = Messages.GenerationModel_RESOURCE_ALREADY_EXISTS_IN_THE_WORKSPACE;

	private static final String NAME_IS_NOT_VALID_FOR_A_RESOURCE_OF_THE_GIVEN_TYPE_S = Messages.GenerationModel_NAME_IS_NOT_VALID_FOR_A_RESOURCE_OF_THE_GIVEN_TYPE_S;

	private static final String PATH_IS_NOT_VALID_FOR_A_RESOURCE_OF_THE_GIVEN_TYPE_S = Messages.GenerationModel_PATH_IS_NOT_VALID_FOR_A_RESOURCE_OF_THE_GIVEN_TYPE_S;

	private static final Logger logger = Logger.getLogger(GenerationModel.class);

	private IResource sourceResource;

	private String targetLocation;

	private String fileName;

	private TemplateType template;

	private Class<?> templateClassLoader;

	public IResource getSourceResource() {
		return sourceResource;
	}

	public void setSourceResource(IResource sourceResource) {
		this.sourceResource = sourceResource;
	}

	public String getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(String targetLocation) {
		this.targetLocation = targetLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTemplateLocation() {
		if (template != null) {
			return template.getLocation();
		}
		return null;
	}

	public String getTemplateExtension() {
		if (template != null) {
			return template.getExtension();
		}
		return null;
	}

	public TemplateType getTemplate() {
		return template;
	}

	public void setTemplate(TemplateType template) {
		this.template = template;
	}

	public IValidationStatus validateLocationGeneric() {
		IWorkspace workspace = RemoteResourcesPlugin.getWorkspace();
		return validateLocationGeneric(workspace);
	}

	public IValidationStatus validateLocationGeneric(IWorkspace workspace) {

		IStatus folderLocationValidation = workspace.validatePath(getTargetLocation(),
				IResource.FOLDER);
		IStatus projectLocationValidation = workspace.validatePath(getTargetLocation(),
				IResource.PROJECT);
		if (!folderLocationValidation.isOK() && !projectLocationValidation.isOK()) {
			return ValidationStatus
					.createError(PATH_IS_NOT_VALID_FOR_A_RESOURCE_OF_THE_GIVEN_TYPE_S);
		}

		if (getTargetLocation().contains(ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES)) {

			if (isValidScriptingServiceFileName(getFileName())) {
				IWorkspaceRoot root = workspace.getRoot();
				if (isResourceExist(root)) {
					IResource res = extractResource(root);
					return ValidationStatus
							.createError(String.format(RESOURCE_ALREADY_EXISTS_IN_THE_WORKSPACE,
									res.getFullPath().toString()));
				}
				return ValidationStatus.createOk();
			} else {
				return ValidationStatus
						.createError(NAME_IS_NOT_VALID_FOR_A_RESOURCE_OF_THE_GIVEN_TYPE_S);
			}
		} else {
			IStatus nameValidation = workspace.validateName(getFileName(), IResource.FILE);
			if (!nameValidation.isOK()) {
				return ValidationStatus
						.createError(NAME_IS_NOT_VALID_FOR_A_RESOURCE_OF_THE_GIVEN_TYPE_S);
			}
			IWorkspaceRoot root = workspace.getRoot();
			if (isResourceExist(root)) {
				IPath location = new Path(getTargetLocation()).append(getFileName());
				return ValidationStatus.createError(String.format(
						RESOURCE_ALREADY_EXISTS_IN_THE_WORKSPACE, location.toString()));
			}
			return ValidationStatus.createOk();
		}
	}

	public IValidationStatus validateTemplate() {
		if (getTemplateLocation() == null || EMPTY_STRING.equals(getTemplateLocation())) {
			return ValidationStatus.createError(TEMPLATE_LOCATION_IS_EMPTY);
		}
		InputStream in = null;
		if (getTemplateClassLoader() != null) {
			in = getTemplateClassLoader().getResourceAsStream(getTemplateLocation());
		} else {
			in = this.getClass().getResourceAsStream(getTemplateLocation());
		}
		if (in == null) {
			logger.error(COULD_NOT_OPEN_INPUT_STREAM_FOR + getTemplateLocation());
			return ValidationStatus.createError(String.format(COULD_NOT_OPEN_INPUT_STREAM_FOR,
					getTemplateLocation()));
		}
		return ValidationStatus.createOk();
	}

	protected abstract IValidationStatus validate();

	public String getFileNameNoExtension() {
		String result = fileName;
		if (fileName != null && fileName.indexOf('.') > 0) {
			result = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return result;
	}

	public String getProjectName() {
		StringBuilder result = new StringBuilder();
		IPath location = new Path(getTargetLocation());
		if (location.segmentCount() > 2) {
			for (int i = 0; i < location.segmentCount(); i++) {
				if (i == 1) {
					continue;
				}
				result.append(location.segment(i) + CommonParameters.SEPARATOR);
			}
			result.delete(result.length() - CommonParameters.SEPARATOR.length(), result.length());
		} else {
			result.append(location.segment(0));
		}
		return result.toString();
	}

	public Class<?> getTemplateClassLoader() {
		return templateClassLoader;
	}

	public void setTemplateClassLoader(Class<?> templateClassLoader) {
		this.templateClassLoader = templateClassLoader;
	}

	private boolean isValidScriptingServiceFileName(String fileName) {

		String scriptingServicefileRegExPattern = "([a-zA-Z_0-9]+)+([\\.]){0,1}(([a-zA-Z0-9]*)*)"; //$NON-NLS-1$
		if (Pattern.matches(scriptingServicefileRegExPattern, fileName)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isResourceExist(IWorkspaceRoot root) {
		IResource resource = extractResource(root);
		if (resource != null) {
			return true;
		} else {
			return false;
		}
	}

	private IResource extractResource(IWorkspaceRoot root) {
		IPath location = new Path(getTargetLocation()).append(getFileName());
		IResource resource = root.findMember(location.toString());
		return resource;
	}
}
