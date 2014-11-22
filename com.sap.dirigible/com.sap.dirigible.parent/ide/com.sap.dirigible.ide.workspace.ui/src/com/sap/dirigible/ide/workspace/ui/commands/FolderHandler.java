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

package com.sap.dirigible.ide.workspace.ui.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.workspace.ui.wizard.folder.NewFolderWizard;

public class FolderHandler extends AbstractWorkspaceHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IContainer selectedContainer = getSelectedContainer(event);
		if (selectedContainer == null) {
			Object lastSelectedElement = getLastSelectedWorkspaceElement();
			if (lastSelectedElement != null && lastSelectedElement instanceof IContainer) {
				selectedContainer = ((IContainer) lastSelectedElement);
			}
		}
		return execute(selectedContainer);
	}

	public Object execute(IContainer container) {
		Wizard wizard = new NewFolderWizard(container);
		WizardDialog dialog = new WizardDialog(null, wizard);
		dialog.open();
		return null;
	}

	private IContainer getSelectedContainer(ExecutionEvent event) {
		IContainer container = null;
		if (event != null) {
			ISelection selection = HandlerUtil.getCurrentSelection(event);
			if (selection instanceof IStructuredSelection) {
				container = getSelectedContainer((IStructuredSelection) selection);
			}
		}
		return container;
	}

	private IContainer getSelectedContainer(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof IContainer) {
			return (IContainer) element;
		}
		return null;
	}

}
