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

package com.sap.dirigible.ide.jgit.command;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Display;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.jgit.connector.JGitConnector;
import com.sap.dirigible.ide.jgit.utils.GitFileUtils;
import com.sap.dirigible.ide.jgit.utils.GitProjectProperties;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.impl.DefaultProgressMonitor;
import com.sap.dirigible.ide.workspace.impl.Workspace;
import com.sap.dirigible.ide.workspace.ui.commands.AbstractWorkspaceHandler;
import com.sap.dirigible.repository.api.IRepository;

public class CloneCommandHandler extends AbstractWorkspaceHandler {

	private static final String TASK_CLONING_REPOSITORY = Messages.CloneCommandHandler_TASK_CLONING_REPOSITORY;
	private static final String MASTER = "master"; //$NON-NLS-1$
	private static final String PLEASE_CHECK_IF_PROXY_SETTINGS_ARE_SET_PROPERLY = Messages.CloneCommandHandler_MASTER;
	private static final String NO_REMOTE_REPOSITORY_FOR = Messages.CloneCommandHandler_NO_REMOTE_REPOSITORY_FOR;
	private static final String INVALID_GIT_REPOSITORY_URL = Messages.CloneCommandHandler_INVALID_GIT_REPOSITORY_URL;
	private static final String DOT_GIT = ".git"; //$NON-NLS-1$
	private static final int MINIMUM_URL_LENGTH = 25;
	private static final String CLONING_GIT_REPOSITORY = Messages.CloneCommandHandler_CLONING_GIT_REPOSITORY;
	private static final String ENTER_GIT_REPOSITORY_URL = Messages.CloneCommandHandler_ENTER_GIT_REPOSITORY_URL;
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private static final String PROJECT_WAS_CLONED = Messages.CloneCommandHandler_PROJECT_WAS_CLONED;
	private static final String WHILE_CLONING_REPOSITORY_ERROR_OCCURED = Messages.CloneCommandHandler_WHILE_CLONING_REPOSITORY_ERROR_OCCURED;
	private static final String SLASH = "/"; //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(CloneCommandHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InputDialog inputDialog = new InputDialog(Display.getCurrent().getActiveShell(),
				CLONING_GIT_REPOSITORY, ENTER_GIT_REPOSITORY_URL, EMPTY_STRING,
				new IInputValidator() {
					private static final long serialVersionUID = -3235570854671466097L;

					@Override
					public String isValid(String inputText) {
						if (!inputText.endsWith(DOT_GIT) || inputText.length() < MINIMUM_URL_LENGTH) {
							return INVALID_GIT_REPOSITORY_URL;
						}
						return null;
					}
				});

		DefaultProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.beginTask(TASK_CLONING_REPOSITORY, IProgressMonitor.UNKNOWN);

		switch (inputDialog.open()) {
		case Window.OK:
			final String repositoryURI = inputDialog.getValue();
			inputDialog.close();
			cloneGitRepository(repositoryURI);
			break;
		}

		monitor.done();
		return null;
	}

	private void cloneGitRepository(final String repositoryURI) {
		File gitDirectory = null;
		try {
			String user = RemoteResourcesPlugin.getUserName();
			String repositoryName = repositoryURI.substring(repositoryURI.lastIndexOf(SLASH) + 1,
					repositoryURI.lastIndexOf(DOT_GIT));
			gitDirectory = GitFileUtils.createTempDirectory(JGitConnector.TEMP_DIRECTORY_PREFIX
					+ repositoryName);

			JGitConnector.cloneRepository(repositoryURI, gitDirectory);

			Repository gitRepository = JGitConnector.getRepository(gitDirectory.getAbsolutePath());
			JGitConnector jgit = new JGitConnector(gitRepository);

			final String lastSha = jgit.getLastSHAForBranch(MASTER);
			GitProjectProperties gitProperties = new GitProjectProperties(repositoryURI, lastSha);

			Workspace workspace = (Workspace) RemoteResourcesPlugin.getWorkspace();
			IRepository repository = workspace.getRepository();
			String workspacePath = String.format(
					GitProjectProperties.DB_DIRIGIBLE_USERS_S_WORKSPACE, user);

			List<String> importedProjects = GitFileUtils.importProject(gitDirectory, repository,
					workspacePath, user, gitProperties);
			StatusLineManagerUtil.setInfoMessage(String
					.format(PROJECT_WAS_CLONED, importedProjects));
			refreshWorkspace();
		} catch (InvalidRemoteException e) {
			logger.error(WHILE_CLONING_REPOSITORY_ERROR_OCCURED + e.getMessage(), e);
			String causedBy = NO_REMOTE_REPOSITORY_FOR + e.getCause().getMessage();
			MessageDialog.openError(null, WHILE_CLONING_REPOSITORY_ERROR_OCCURED, causedBy);
		} catch (TransportException e) {
			logger.error(WHILE_CLONING_REPOSITORY_ERROR_OCCURED + e.getMessage(), e);
			Throwable rootCause = e.getCause();

			if (rootCause != null) {
				rootCause = rootCause.getCause();
				if (rootCause instanceof UnknownHostException) {
					String causedBy = PLEASE_CHECK_IF_PROXY_SETTINGS_ARE_SET_PROPERLY;
					MessageDialog.openError(null, WHILE_CLONING_REPOSITORY_ERROR_OCCURED, causedBy);
				} else {
					MessageDialog.openError(null, WHILE_CLONING_REPOSITORY_ERROR_OCCURED, e
							.getCause().getMessage());
				}
			}
		} catch (GitAPIException e) {
			logger.error(WHILE_CLONING_REPOSITORY_ERROR_OCCURED + e.getMessage(), e);
			MessageDialog.openError(null, WHILE_CLONING_REPOSITORY_ERROR_OCCURED, e.getCause()
					.getMessage());
		} catch (IOException e) {
			logger.error(WHILE_CLONING_REPOSITORY_ERROR_OCCURED + e.getMessage(), e);
			MessageDialog.openError(null, WHILE_CLONING_REPOSITORY_ERROR_OCCURED, e.getCause()
					.getMessage());
		} finally {
			GitFileUtils.deleteDirectory(gitDirectory);
		}
	}

}
