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

package com.sap.dirigible.ide.db.viewer.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sap.dirigible.ide.db.viewer.editor.DbEditorInput;
import com.sap.dirigible.ide.db.viewer.editor.DbTableMetadataEditor;
import com.sap.dirigible.ide.db.viewer.views.TreeObject;

/**
 * Action that will trigger opening of Table Definition editor.
 * 
 */
public class ShowTableDefinitionAction extends Action {

	private static final String WILL_SHOW_TABLE_DEFINITION_CONTENT = Messages.ShowTableDefinitionAction_WILL_SHOW_TABLE_DEFINITION_CONTENT;

	private static final String OPEN_TABLE_DEFINITION = Messages.ShowTableDefinitionAction_OPEN_TABLE_DEFINITION;

	private static final long serialVersionUID = 3872859942737870851L;

	private TreeViewer viewer;

	public ShowTableDefinitionAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(OPEN_TABLE_DEFINITION);
		setToolTipText(WILL_SHOW_TABLE_DEFINITION_CONTENT);
	}

	public void run() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (TreeObject.class.isInstance(obj)) {
			if (((TreeObject) obj).getTableDefinition() != null) {
				String editorId = DbTableMetadataEditor.class
						.getCanonicalName();
				IEditorInput input = new DbEditorInput(
						((TreeObject) obj).getTableDefinition(),
						((TreeObject) obj).getParent().getConnectionFactory());
				openEditor(editorId, input);
			}
		}

	}

	private static boolean openEditor(String id, IEditorInput input) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		try {
			page.openEditor(input, id);
			return true;
		} catch (PartInitException e) {
			return false;
		}
	}

}
