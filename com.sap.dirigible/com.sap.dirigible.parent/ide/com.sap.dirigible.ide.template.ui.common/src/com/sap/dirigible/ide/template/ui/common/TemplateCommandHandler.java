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
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.workspace.ui.commands.AbstractWorkspaceHandler;
import com.sap.dirigible.repository.logging.Logger;

public abstract class TemplateCommandHandler extends AbstractWorkspaceHandler {

	private static final String COULD_NOT_OPEN_WIZARD = Messages.TemplateCommandHandler_COULD_NOT_OPEN_WIZARD;
	private static final String OPEN_WIZARD_ERROR = Messages.TemplateCommandHandler_OPEN_WIZARD_ERROR;
	private static final Logger logger = Logger.getLogger(TemplateCommandHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = null;
		if (event != null) {
			selection = HandlerUtil.getCurrentSelection(event);
		}

		if (selection == null) {
			Object lastSelectedElement = getLastSelectedWorkspaceElement();
			if (lastSelectedElement != null && lastSelectedElement instanceof IResource) {
				openWizard(((IResource) lastSelectedElement));
				return null;
			}
		}

		openWizard(selection);
		return null;
	}

	private void openWizard(ISelection selection) {
		IResource resource = null;
		if (selection != null && IStructuredSelection.class.isInstance(selection)) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (IResource.class.isInstance(element)) {
				resource = (IResource) element;
			}
		}
		openWizard(resource);
	}

	protected void openWizard(IResource resource) {
		try {
			final Wizard wizard = getWizard(resource);
			final WizardDialog dialog = new WizardDialog(null, wizard);
			dialog.open();
		} catch (Exception ex) {
			logger.error(COULD_NOT_OPEN_WIZARD, ex);
			MessageDialog.openError(null, OPEN_WIZARD_ERROR, ex.getMessage());
		}
	}

	protected abstract Wizard getWizard(IResource resource);

}
