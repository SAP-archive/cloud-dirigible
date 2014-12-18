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

package com.sap.dirigible.ide.workspace.ui.wizards.rename;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;

import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.ui.shared.ValidationStatus;

public class RenameWizardModel {

	private static final String COULD_NOT_RENAME_RESOURCE = Messages.RenameWizardModel_COULD_NOT_RENAME_RESOURCE;

	private static final String A_RESOURCE_WITH_THIS_NAME_ALREADY_EXISTS = Messages.RenameWizardModel_A_RESOURCE_WITH_THIS_NAME_ALREADY_EXISTS;

	private static final String INVALID_RESOURCE_NAME = Messages.RenameWizardModel_INVALID_RESOURCE_NAME;

	private final IResource resource;

	private String resourceName;

	public RenameWizardModel(IResource resource) {
		this.resource = resource;
		this.resourceName = resource.getName();
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public ValidationStatus validate() {
		final IWorkspace workspace = RemoteResourcesPlugin.getWorkspace();
		final IStatus nameValidation = workspace.validateName(resourceName,
				resource.getType());
		if (!nameValidation.isOK()) {
			return ValidationStatus.createError(INVALID_RESOURCE_NAME);
		}
		final IWorkspaceRoot root = workspace.getRoot();
		final IPath parentPath = resource.getParent().getFullPath();
		final IResource existing = root.findMember(parentPath
				.append(resourceName));
		if (existing != null) {
			return ValidationStatus
					.createError(A_RESOURCE_WITH_THIS_NAME_ALREADY_EXISTS);
		}
		return ValidationStatus.createOk();
	}

	public void persist() throws IOException {
		final IPath destination = resource.getParent().getFullPath()
				.append(resourceName);
		try {
			resource.move(destination, false, null);
		} catch (CoreException ex) {
			throw new IOException(COULD_NOT_RENAME_RESOURCE, ex);
		}
	}

}
