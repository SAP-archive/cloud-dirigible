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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.jgit.connector.JGitConnector;
import com.sap.dirigible.ide.jgit.utils.CommandHandlerUtils;
import com.sap.dirigible.ide.jgit.utils.GitFileUtils;
import com.sap.dirigible.ide.jgit.utils.GitProjectProperties;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.impl.DefaultProgressMonitor;
import com.sap.dirigible.ide.workspace.impl.Workspace;
import com.sap.dirigible.ide.workspace.ui.commands.AbstractWorkspaceHandler;
import com.sap.dirigible.repository.api.IRepository;

public class ResetCommandHandler extends AbstractWorkspaceHandler {
	private static final String TASK_RESETING_PROJECT = Messages.ResetCommandHandler_TASK_RESETING_PROJECT;
	private static final String PROJECT_S_SUCCESSFULY_RESETED = Messages.ResetCommandHandler_PROJECT_S_SUCCESSFULY_RESETED;
	private static final String DO_YOU_REALLY_WANT_TO_HARD_RESET_PROJECT_S = Messages.HardResetCommandHandler_DO_YOU_REALLY_WANT_TO_HARD_RESET_PROJECT_S;
	private static final String HARD_RESET = Messages.HardResetCommandHandler_HARD_RESET;
	private static final String SLASH = "/"; //$NON-NLS-1$
	private static final String DOT_GIT = ".git"; //$NON-NLS-1$
	private static final String MASTER = "master"; //$NON-NLS-1$
	private static final String ONLY_ONE_PROJECT_CAN_BE_HARD_RESETED_AT_A_TIME = Messages.HardResetCommandHandler_ONLY_ONE_PROJECT_CAN_BE_HARD_RESETED_AT_A_TIME;
	private static final String PLEASE_SELECT_ONE = Messages.HardResetCommandHandler_PLEASE_SELECT_ONE;
	private static final String NO_PROJECT_IS_SELECTED_FOR_HARD_RESET = Messages.HardResetCommandHandler_NO_PROJECT_IS_SELECTED_FOR_HARD_RESET;
	private static final String WHILE_HARD_RESETING_PROJECT_ERROR_OCCURED = Messages.HardResetCommandHandler_WHILE_HARD_RESETING_PROJECT_ERROR_OCCURED;
	private static final String ERROR_DURING_HARD_RESET = Messages.HardResetCommandHandler_ERROR_DURING_HARD_RESET;
	private static final String INCORRECT_USERNAME_AND_OR_PASSWORD_OR_GIT_REPOSITORY_URI = Messages.HardResetCommandHandler_reset;
	private static final String PLEASE_CHECK_IF_PROXY_SETTINGS_ARE_SET_PROPERLY = Messages.HardResetCommandHandler_PLEASE_CHECK_IF_PROXY_SETTINGS_ARE_SET_PROPERLY;
	private static final Logger logger = Logger.getLogger(ResetCommandHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final IProject[] projects = CommandHandlerUtils.getProjects(selection, logger);

		if (projects.length == 0) {
			logger.warn(NO_PROJECT_IS_SELECTED_FOR_HARD_RESET);
			StatusLineManagerUtil.setWarningMessage(NO_PROJECT_IS_SELECTED_FOR_HARD_RESET);
			MessageDialog.openWarning(null, NO_PROJECT_IS_SELECTED_FOR_HARD_RESET,
					PLEASE_SELECT_ONE);
			return null;
		} else if (projects.length > 1) {
			logger.warn(ONLY_ONE_PROJECT_CAN_BE_HARD_RESETED_AT_A_TIME);
			StatusLineManagerUtil.setWarningMessage(ONLY_ONE_PROJECT_CAN_BE_HARD_RESETED_AT_A_TIME);
			MessageDialog.openWarning(null, ONLY_ONE_PROJECT_CAN_BE_HARD_RESETED_AT_A_TIME,
					PLEASE_SELECT_ONE);
			return null;
		}

		IProject project = projects[0];
		final Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		String message = String.format(DO_YOU_REALLY_WANT_TO_HARD_RESET_PROJECT_S,
				project.getName());

		DefaultProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.beginTask(TASK_RESETING_PROJECT, IProgressMonitor.UNKNOWN);

		boolean confirmed = MessageDialog.openConfirm(parent, HARD_RESET, message);
		if (confirmed) {
			hardReset(project);
		}

		monitor.done();
		return null;
	}

	private void hardReset(IProject project) {
		final String errorMessage = String.format(WHILE_HARD_RESETING_PROJECT_ERROR_OCCURED,
				project.getName());
		File tempGitDirectory = null;
		try {
			String dirigibleUser = RemoteResourcesPlugin.getUserName();
			GitProjectProperties gitProperties = GitFileUtils.getGitPropertiesForProject(project,
					dirigibleUser);
			String gitRepositoryURI = gitProperties.getURL();
			String repositoryName = gitRepositoryURI.substring(
					gitRepositoryURI.lastIndexOf(SLASH) + 1, gitRepositoryURI.lastIndexOf(DOT_GIT));
			tempGitDirectory = GitFileUtils.createTempDirectory(JGitConnector.TEMP_DIRECTORY_PREFIX
					+ repositoryName);

			// FIXME: Won't work for secured git repositories. Maybe default
			// should prompt for username and password?
			JGitConnector.cloneRepository(tempGitDirectory, gitRepositoryURI);

			GitFileUtils.deleteDGBRepositoryProject(project, dirigibleUser);

			Workspace workspace = (Workspace) project.getWorkspace();
			IRepository dirigibleRepository = workspace.getRepository();

			String workspacePath = String.format(
					GitProjectProperties.DB_DIRIGIBLE_USERS_S_WORKSPACE, dirigibleUser);

			Repository repository = JGitConnector.getRepository(tempGitDirectory.getAbsolutePath());
			JGitConnector jgit = new JGitConnector(repository);
			final String lastSHA = jgit.getLastSHAForBranch(MASTER);
			gitProperties.setSHA(lastSHA);

			GitFileUtils.importProject(tempGitDirectory, dirigibleRepository, workspacePath,
					dirigibleUser, gitProperties);

			String message = String.format(PROJECT_S_SUCCESSFULY_RESETED, project.getName());
			StatusLineManagerUtil.setInfoMessage(message);
			refreshWorkspace();
		} catch (IOException e) {
			logger.error(errorMessage, e);
			MessageDialog.openError(null, ERROR_DURING_HARD_RESET, errorMessage);
		} catch (InvalidRemoteException e) {
			logger.error(errorMessage, e);
			MessageDialog.openError(null, ERROR_DURING_HARD_RESET,
					errorMessage + "\n" + e.getMessage()); //$NON-NLS-1$
		} catch (TransportException e) {
			logger.error(errorMessage, e);
			Throwable rootCause = e.getCause();
			if (rootCause != null) {
				rootCause = rootCause.getCause();
				if (rootCause instanceof UnknownHostException) {
					MessageDialog.openError(null, errorMessage,
							PLEASE_CHECK_IF_PROXY_SETTINGS_ARE_SET_PROPERLY);
				} else {
					MessageDialog.openError(null, errorMessage, e.getCause().getMessage()
							+ INCORRECT_USERNAME_AND_OR_PASSWORD_OR_GIT_REPOSITORY_URI);
				}
			}
		} catch (GitAPIException e) {
			logger.error(errorMessage, e);
			MessageDialog.openError(null, ERROR_DURING_HARD_RESET, errorMessage);
		} finally {
			GitFileUtils.deleteDirectory(tempGitDirectory);
		}
	}

}
