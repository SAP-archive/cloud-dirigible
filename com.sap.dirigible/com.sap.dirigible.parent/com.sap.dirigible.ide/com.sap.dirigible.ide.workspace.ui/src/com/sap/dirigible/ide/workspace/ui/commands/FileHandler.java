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

import com.sap.dirigible.ide.workspace.ui.wizard.file.NewFileWizard;

public class FileHandler extends AbstractWorkspaceHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IContainer container = getContainer(event);
		Wizard wizard = new NewFileWizard(container);
		WizardDialog dialog = new WizardDialog(null, wizard);
		dialog.open();
		return null;
	}

	private IContainer getContainer(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			return getContainer((IStructuredSelection) selection);
		}
		return null;
	}

	private IContainer getContainer(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof IContainer) {
			return (IContainer) element;
		}
		return null;
	}

}
