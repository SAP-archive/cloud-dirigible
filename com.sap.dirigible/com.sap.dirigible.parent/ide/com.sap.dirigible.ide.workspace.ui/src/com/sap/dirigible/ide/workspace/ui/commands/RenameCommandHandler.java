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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.editor.js.JavaScriptEditor;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.ui.wizards.rename.RenameWizard;

public class RenameCommandHandler extends AbstractHandler {

	private static final String RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_INSTANCES_OF_I_RESOURCE = Messages.RenameCommandHandler_RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_INSTANCES_OF_I_RESOURCE;
	private static final String RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_A_SINGLE_RESOURCE_AT_A_TIME = Messages.RenameCommandHandler_RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_A_SINGLE_RESOURCE_AT_A_TIME;
	private static final Logger log = Logger
			.getLogger(RenameCommandHandler.class);

	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			renameSelection((IStructuredSelection) selection);
		}
		return null;

	}

	private void renameFileInEditor(String oldFileName, String newFileName) {
		IEditorReference[] editors = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference editorRef : editors) {
			IEditorPart editorPart = editorRef.getEditor(false);
			if (editorPart != null && editorPart.getTitle().equals(oldFileName)) {
				// com.sap.dirigible.ide.editor.js.
				if (editorPart instanceof JavaScriptEditor) {
					((JavaScriptEditor) editorPart).setPartName(newFileName);
					return;
				}
			}

		}
	}

	private void renameSelection(IStructuredSelection selection) {
		if (selection.size() != 1) {
			log.warn(RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_A_SINGLE_RESOURCE_AT_A_TIME);
			return;
		}
		final Object element = selection.getFirstElement();
		if (!(element instanceof IResource)) {
			log.warn(RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_INSTANCES_OF_I_RESOURCE);
			return;
		}
		String newValue = renameResource((IResource) element);
		if (newValue != null) {
			renameFileInEditor(((IResource) element).getName(), newValue);
		}
	}

	private String renameResource(IResource resource) {
		final RenameWizard wizard = new RenameWizard(resource);
		final Dialog dialog = new WizardDialog(null, wizard);
		if (dialog.open() == Window.CANCEL) {
			return null;
		}
		;
		return wizard.getText();
	}

}
