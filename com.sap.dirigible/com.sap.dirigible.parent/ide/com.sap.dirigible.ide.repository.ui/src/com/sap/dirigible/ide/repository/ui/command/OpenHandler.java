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

package com.sap.dirigible.ide.repository.ui.command;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.editor.text.input.ContentEditorInput;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.repository.api.IResource;

public class OpenHandler extends AbstractHandler {

	private static final String OPEN_FAILURE = Messages.OpenHandler_OPEN_FAILURE;

	private static final String COULD_NOT_OPEN_ONE_OR_MORE_FILES = Messages.OpenHandler_COULD_NOT_OPEN_ONE_OR_MORE_FILES;

	private static final Logger logger = Logger.getLogger(OpenHandler.class);

	public static final String TEXT_EDITOR_ID = "com.sap.dirigible.ide.editor.text.editor.ReadOnlyEditor"; //$NON-NLS-1$

	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean successful = true;
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			for (Object element : structuredSelection.toArray()) {
				successful &= openEditorFor(element);
			}
		}
		if (!successful) {
			logger.error(COULD_NOT_OPEN_ONE_OR_MORE_FILES);
			MessageDialog.openError(null, OPEN_FAILURE,
					COULD_NOT_OPEN_ONE_OR_MORE_FILES);
		}
		return null;
	}

	private static boolean openEditorFor(Object element) {
		if (element instanceof IResource) {
			return openEditorForResource((IResource) element);
		}
		return true;
	}

	private static boolean openEditorForResource(IResource file) {
		String editorId;// =
						// EditorUtil.getEditorIdForExtension(ContentTypeHelper.getExtension(file.getName()));
		// if (editorId == null) {
		editorId = TEXT_EDITOR_ID;
		// }
		IEditorInput input;
		try {
			input = new ContentEditorInput(file.getName(), file.getPath(),
					file.getContent());
			return openEditor(editorId, input);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	private static boolean openEditor(String id, IEditorInput input) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		try {
			page.openEditor(input, id);
			return true;
		} catch (PartInitException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

}
