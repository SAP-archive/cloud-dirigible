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

package com.sap.dirigible.ide.editor.text.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import com.sap.dirigible.ide.logging.Logger;

public class TextEditorHandler extends AbstractHandler {

	private static final String COULD_NOT_OPEN_EDITOR_FOR_SOME_OR_ALL_OF_THE_SELECTED_ITEMS = Messages.TextEditorHandler_COULD_NOT_OPEN_EDITOR_FOR_SOME_OR_ALL_OF_THE_SELECTED_ITEMS;

	private static final Logger logger = Logger
			.getLogger(TextEditorHandler.class);

	private static final String EDITOR_ID = "com.sap.dirigible.ide.editor.text.editor.TextEditor"; //$NON-NLS-1$

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (selection instanceof IStructuredSelection && window != null) {
			execute(window.getActivePage(), (IStructuredSelection) selection);
		}
		return null;
	}

	protected String getEditorId() {
		return EDITOR_ID;
	}

	private void execute(IWorkbenchPage page, IStructuredSelection selection) {
		Throwable error = null;
		for (Object element : selection.toArray()) {
			if (element instanceof IFile) {
				try {
					execute(page, (IFile) element);
				} catch (PartInitException ex) {
					if (error == null) {
						error = ex;
					}
				}

			}
		}
		if (error != null) {
			logger.error(
					COULD_NOT_OPEN_EDITOR_FOR_SOME_OR_ALL_OF_THE_SELECTED_ITEMS,
					error);
			MessageDialog
					.openError(null,
							Messages.TextEditorHandler_OPERATION_ERROR,
							COULD_NOT_OPEN_EDITOR_FOR_SOME_OR_ALL_OF_THE_SELECTED_ITEMS);
		}

	}

	private void execute(IWorkbenchPage page, IFile file)
			throws PartInitException {
		IEditorInput input = new FileEditorInput(file);
		page.openEditor(input, getEditorId());
	}

}
