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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.publish.PublishManager;

/**
 * Handler for the Publish command.
 * 
 */
public class ActivateFileCommandHandler extends AbstractHandler {
	
	private static final String NO_PROJECTS_IN_SELECTION_NOTHING_TO_ACTIVATE = PublishCommandMessages.NO_PROJECTS_IN_SELECTION_NOTHING_TO_ACTIVATE;
	private static final String NOTHING_IS_SELECTED_TO_BE_ACTIVATED = PublishCommandMessages.NOTHING_IS_SELECTED_TO_BE_ACTIVATED;
	private static final Logger logger = Logger.getLogger(ActivateFileCommandHandler.class);
	

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection.isEmpty()) {
			logger.warn(NOTHING_IS_SELECTED_TO_BE_ACTIVATED);
			return null;
		}

		final IFile[] files = PublishManager.getFiles(selection);
		if (files.length == 0) {
			logger.warn(NO_PROJECTS_IN_SELECTION_NOTHING_TO_ACTIVATE);
			return null;
		}

		boolean success = true;
		String errorMessage = null;
		for (IFile file : files) {
			try {
				activateFile(file);
				StatusLineManagerUtil.setInfoMessage(String.format(
						StatusLineManagerUtil.ARTIFACT_HAS_BEEN_ACTIVATED,
						file.getName()));
			} catch (Exception ex) {
				errorMessage = ex.getMessage();
				logger.error(errorMessage, ex);
				success = false;
			}
		}
		if (!success) {
			logger.error(errorMessage);
			MessageDialog.openError(null,
					PublishCommandMessages.ACTIVATION_FAIL_TITLE, errorMessage);
		}
		return null;
	}
	
	protected void activateFile(IFile file) throws PublishException {
		final List<IPublisher> publishers = PublishManager.getPublishers();

		for (Iterator<IPublisher> iterator = publishers.iterator(); iterator
				.hasNext();) {
			IPublisher publisher = (IPublisher) iterator.next();
			publisher.activateFile(file);
		}
	}
	
}
