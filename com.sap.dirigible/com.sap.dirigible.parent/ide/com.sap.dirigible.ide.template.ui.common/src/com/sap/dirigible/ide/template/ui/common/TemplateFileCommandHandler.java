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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.repository.logging.Logger;

public abstract class TemplateFileCommandHandler extends TemplateCommandHandler {

	private static final String SELECTED_RESOURCE_IS_NOT_A_FILE = Messages.TemplateFileCommandHandler_SELECTED_RESOURCE_IS_NOT_A_FILE;
	private static final String NO_FILE_IN_SELECTION_SELECTION_IS_EMPTY = Messages.TemplateFileCommandHandler_NO_FILE_IN_SELECTION_SELECTION_IS_EMPTY;
	private static final String UNKNOWN_SELECTION_TYPE = Messages.TemplateFileCommandHandler_UNKNOWN_SELECTION_TYPE;
	private static final String NO_FILE_SELECTED_WILL_NOT_OPEN_WIZARD = Messages.TemplateFileCommandHandler_NO_FILE_SELECTED_WILL_NOT_OPEN_WIZARD;
	private static final Logger logger = Logger
			.getLogger(TemplateFileCommandHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		final IFile file = getFile(selection);
		if (file != null) {
			openWizard(file);
		} else {
			logger.info(NO_FILE_SELECTED_WILL_NOT_OPEN_WIZARD);
		}
		return null;
	}

	private IFile getFile(ISelection selection) {
		if (!(selection instanceof IStructuredSelection)) {
			logger.warn(UNKNOWN_SELECTION_TYPE);
			return null;
		}
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.isEmpty()) {
			logger.info(NO_FILE_IN_SELECTION_SELECTION_IS_EMPTY);
			return null;
		}
		final Object element = structuredSelection.getFirstElement();
		if (!(element instanceof IFile)) {
			logger.info(SELECTED_RESOURCE_IS_NOT_A_FILE);
			return null;
		}
		return (IFile) element;
	}

	protected abstract Wizard getWizard(IFile file);

	@Override
	protected Wizard getWizard(IResource resource) {
		return getWizard((IFile) resource);
	}

}
