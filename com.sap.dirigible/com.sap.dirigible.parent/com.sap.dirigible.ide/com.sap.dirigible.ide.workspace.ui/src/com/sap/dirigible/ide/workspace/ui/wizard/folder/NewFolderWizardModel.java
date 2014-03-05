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

package com.sap.dirigible.ide.workspace.ui.wizard.folder;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.ui.shared.IValidationStatus;
import com.sap.dirigible.ide.workspace.ui.shared.ValidationStatus;
import com.sap.dirigible.ide.workspace.ui.viewer.WorkspaceViewerUtils;

public class NewFolderWizardModel {

	private static final String A_RESOURCE_WITH_THAT_PATH_ALREADY_EXISTS = Messages.NewFolderWizardModel_A_RESOURCE_WITH_THAT_PATH_ALREADY_EXISTS;

	private static final String INVALID_FOLDER_NAME = Messages.NewFolderWizardModel_INVALID_FOLDER_NAME;

	private static final String INVALID_PARENT_LOCATION = Messages.NewFolderWizardModel_INVALID_PARENT_LOCATION;

	private static final String PARENT_LOCATION_CANNOT_BE_NULL = Messages.NewFolderWizardModel_PARENT_LOCATION_CANNOT_BE_NULL;

	private static final String FOLDER_NAME_CANNOT_BE_NULL = Messages.NewFolderWizardModel_FOLDER_NAME_CANNOT_BE_NULL;

	private String folderName = "folder"; //$NON-NLS-1$

	private String parentLocation = ""; //$NON-NLS-1$

	public NewFolderWizardModel() {
		super();
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		if (folderName == null) {
			throw new IllegalArgumentException(FOLDER_NAME_CANNOT_BE_NULL);
		}
		this.folderName = folderName;
	}

	public String getParentLocation() {
		return parentLocation;
	}

	public void setParentLocation(String parentLocation) {
		if (parentLocation == null) {
			throw new IllegalArgumentException(PARENT_LOCATION_CANNOT_BE_NULL);
		}
		this.parentLocation = parentLocation;
	}

	public IValidationStatus validate() {
		IWorkspace workspace = RemoteResourcesPlugin.getWorkspace();
		IStatus projectPathValidation = workspace.validatePath(parentLocation,
				IResource.PROJECT);
		IStatus folderPathValidation = workspace.validatePath(parentLocation,
				IResource.FOLDER);
		if (!projectPathValidation.isOK() && !folderPathValidation.isOK()) {
			return ValidationStatus.createError(INVALID_PARENT_LOCATION);
		}
		IStatus folderNameValidation = workspace.validateName(folderName,
				IResource.FOLDER);
		if (!folderNameValidation.isOK()) {
			return ValidationStatus.createError(INVALID_FOLDER_NAME);
		}
		IPath location = new Path(parentLocation).append(folderName);
		IWorkspaceRoot root = workspace.getRoot();
		IResource resource = root.findMember(location);
		if (resource != null) {
			return ValidationStatus
					.createError(A_RESOURCE_WITH_THAT_PATH_ALREADY_EXISTS);
		}
		return ValidationStatus.createOk();
	}

	public void execute() throws CoreException {
		IPath location = new Path(parentLocation).append(folderName);
		IWorkspace workspace = RemoteResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IFolder folder = root.getFolder(location);
		folder.create(false, false, null);
		IContainer parent = folder.getParent();
		if (parent != null) {
			WorkspaceViewerUtils.expandElement(parent);
		}
		WorkspaceViewerUtils.selectElement(folder);
	}

}
