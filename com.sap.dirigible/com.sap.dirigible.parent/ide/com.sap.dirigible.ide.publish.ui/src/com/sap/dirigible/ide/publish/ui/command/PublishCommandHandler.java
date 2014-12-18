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

package com.sap.dirigible.ide.publish.ui.command;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.publish.PublishManager;

/**
 * Handler for the Publish command.
 * 
 */
public class PublishCommandHandler extends AbstractHandler {

	private static final String UNKNOWN_SELECTION_TYPE = PublishCommandMessages.UNKNOWN_SELECTION_TYPE;
	private static final String NO_PROJECTS_IN_SELECTION_NOTHING_TO_PUBLISH = PublishCommandMessages.NO_PROJECTS_IN_SELECTION_NOTHING_TO_PUBLISH;
	private static final String NOTHING_IS_SELECTED_TO_BE_PUBLISHED = PublishCommandMessages.NOTHING_IS_SELECTED_TO_BE_PUBLISHED;
	private static final Logger logger = Logger.getLogger(PublishCommandHandler.class);

	public PublishCommandHandler() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		return executeOnSelection(selection);
	}

	protected Object executeOnSelection(final ISelection selection) {
		if (selection.isEmpty()) {
			logger.warn(NOTHING_IS_SELECTED_TO_BE_PUBLISHED);
			return null;
		}

		final IProject[] projects = PublishManager.getProjects(selection);
		if (projects.length == 0) {
			logger.warn(NO_PROJECTS_IN_SELECTION_NOTHING_TO_PUBLISH);
			return null;
		}

		StatusLineManagerUtil.setInfoMessage("");
		boolean success = true;
		String errorMessage = null;
		for (IProject project : projects) {
			try {
				publishProject(project);
				StatusLineManagerUtil.setInfoMessage(String.format(getStatusMessage(),
						project.getName()));
			} catch (Exception ex) {
				errorMessage = ex.getMessage();
				logger.error(errorMessage, ex);
				success = false;
			}
		}
		if (!success) {
			logger.error(errorMessage);
			StatusLineManagerUtil.setErrorMessage(errorMessage);
			MessageDialog.openError(null, PublishCommandMessages.PUBLISH_FAIL_TITLE, errorMessage);
		}
		return null;
	}

	protected String getStatusMessage() {
		return StatusLineManagerUtil.ARTIFACT_HAS_BEEN_PUBLISHED;
	}


	protected void publishProject(IProject project) throws PublishException {
		PublishManager.publishProject(project);
	}

}
