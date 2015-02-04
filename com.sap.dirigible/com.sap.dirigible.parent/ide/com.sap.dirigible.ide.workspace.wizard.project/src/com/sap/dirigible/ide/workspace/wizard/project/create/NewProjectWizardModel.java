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

package com.sap.dirigible.ide.workspace.wizard.project.create;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.publish.PublishManager;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.ide.workspace.dual.WorkspaceLocator;
import com.sap.dirigible.ide.workspace.ui.shared.IValidationStatus;
import com.sap.dirigible.ide.workspace.ui.shared.ValidationStatus;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.api.RepositoryException;

public class NewProjectWizardModel {

	private static final String ERROR_OCCURED_WHEN_TRYING_TO_VALIDATE_NEW_PROJECT_NAME = Messages.NewProjectWizardModel_ERROR_OCCURED_WHEN_TRYING_TO_VALIDATE_NEW_PROJECT_NAME;

	private static final String PROJECT_WITH_NAME_S_WAS_ALREADY_CREATED_FROM_USER_S = Messages.NewProjectWizardModel_PROJECT_WITH_NAME_S_WAS_ALREADY_CREATED_FROM_USER_S;

	private static final String PROJECT_WITH_THIS_NAME_ALREADY_EXISTS = Messages.NewProjectWizardModel_PROJECT_WITH_THIS_NAME_ALREADY_EXISTS;

	private static final String INVALID_PROJECT_NAME = Messages.NewProjectWizardModel_INVALID_PROJECT_NAME;

	public static final Logger logger = Logger
			.getLogger(NewProjectWizardModel.class.getCanonicalName());

	private static final String INITIAL_LOCATION = "project"; //$NON-NLS-1$

	private String projectName = INITIAL_LOCATION;

	private String conflictUser;

	private ProjectTemplateType template;

	private boolean useTemplate = true;

	public NewProjectWizardModel() {
		super();
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String location) {
		this.projectName = location;
	}

	public IValidationStatus validate() {
		IWorkspace workspace = WorkspaceLocator.getWorkspace();
		IStatus pathValidation = workspace.validateName(projectName,
				IResource.PROJECT);
		if (!pathValidation.isOK()) {
			return ValidationStatus.createError(INVALID_PROJECT_NAME);
		}

		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(projectName);
		if (project.exists()) {
			return ValidationStatus
					.createError(PROJECT_WITH_THIS_NAME_ALREADY_EXISTS);
		}

		if (!isValidRepositoryProject()) {
			return ValidationStatus.createError(String.format(
					PROJECT_WITH_NAME_S_WAS_ALREADY_CREATED_FROM_USER_S,
					projectName, conflictUser));
		}
		return ValidationStatus.createOk();
	}

	private boolean isValidRepositoryProject() {
		IRepository repository = RepositoryFacade.getInstance().getRepository();
		ICollection userFolders = repository
				.getCollection(IRepositoryPaths.DB_DIRIGIBLE_USERS);
		boolean isValid = true;
		try {
			for (ICollection user : userFolders.getCollections()) {
				if (user.exists()) {
					ICollection workspace = user
							.getCollection(IRepositoryPaths.WORKSPACE_FOLDER_NAME);
					for (ICollection nextProject : workspace.getCollections()) {
						if (nextProject.exists()) {
							if (nextProject.getName().equals(projectName)) {
								this.conflictUser = user.getName();
								isValid = false;
								break;
							}
						}
					}
					if (!isValid) {
						break;
					}
				}
			}
		} catch (IOException e) {
			String message = ERROR_OCCURED_WHEN_TRYING_TO_VALIDATE_NEW_PROJECT_NAME;
			logger.error(message, e);
		}
		return isValid;
	}

	public IProject execute() throws CoreException {
		IWorkspace workspace = WorkspaceLocator.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(projectName);
		// create the project first
		try {
			project.create(new NullProgressMonitor());
		} catch(CoreException e) {
			logger.error(e.getMessage());
		}
		
		project.open(new NullProgressMonitor());		
		
		if (isUseTemplate()) {
			String contentPath = this.template.getContentPath();
			try {
				IRepository repository = RepositoryFacade.getInstance()
						.getRepository();
				com.sap.dirigible.repository.api.IResource contentResource = repository
						.getResource(contentPath);
				if (contentResource.exists()) {
					byte[] data = contentResource.getContent();
					IPath location = project.getRawLocation();
					if (location == null) {
						location = project.getLocation();
					}
					repository.importZip(data, location
							.toString());
				}
			} catch (RepositoryException e) {
				logger.error(e.getMessage(), e);
				throw new CoreException(new Status(IStatus.ERROR, // NOPMD
						"com.sap.dirigible.ide.workspace.ui", e.getMessage())); //$NON-NLS-1$ // NOPMD
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new CoreException(new Status(IStatus.ERROR, // NOPMD
						"com.sap.dirigible.ide.workspace.ui", e.getMessage())); //$NON-NLS-1$ // NOPMD
			}

		} else {
			// create default folders
			List<IPublisher> publishers = PublishManager.getPublishers();
			for (IPublisher publisher : publishers) {
				IFolder folder = project.getFolder(publisher.getFolderType());
				folder.create(true, false, null);
			}
		}
		
		return project;
	}

	public ProjectTemplateType getTemplate() {
		return template;
	}

	public void setTemplate(ProjectTemplateType template) {
		this.template = template;
	}

	public String getTemplateLocation() {
		if (this.template == null) {
			return null;
		}
		return this.template.getLocation();
	}

	public void setUseTemplate(boolean b) {
		this.useTemplate = b;
	}

	public boolean isUseTemplate() {
		return useTemplate;
	}

}
