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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sap.dirigible.ide.db.viewer.views.SQLConsole;
import com.sap.dirigible.ide.db.viewer.views.TableDefinition;
import com.sap.dirigible.ide.db.viewer.views.TreeObject;

public class ViewTableContentAction extends Action {

	private static final String DATABASE_VIEW = Messages.ViewTableContentAction_DATABASE_VIEW;

	private static final String SELECT_FROM = "SELECT * FROM "; //$NON-NLS-1$

	private static final String CANNOT_OPEN_SQL_VIEW = Messages.ViewTableContentAction_CANNOT_OPEN_SQL_VIEW;

	private static final String WILL_SHOW_TABLE_CONTENT = Messages.ViewTableContentAction_WILL_SHOW_TABLE_CONTENT;

	private static final String SHOW_CONTENT = Messages.ViewTableContentAction_SHOW_CONTENT;

	private static final long serialVersionUID = 5194043886090203855L;

	private TreeViewer viewer;

	private String consoleId;

	public ViewTableContentAction(TreeViewer viewer, String consoleId) {
		this.viewer = viewer;
		this.consoleId = consoleId;
		setText(SHOW_CONTENT);
		setToolTipText(WILL_SHOW_TABLE_CONTENT);
	}

	public void run() {
		try {
			ISelection selection = viewer.getSelection();
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (TreeObject.class.isInstance(obj)) {
				TableDefinition tableDefinition = ((TreeObject) obj)
						.getTableDefinition();
				if (tableDefinition != null) {
					SQLConsole view = (SQLConsole) PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.showView(consoleId);
					executeSelectStatement(tableDefinition, view);
				}
			}

		} catch (PartInitException e) {
			showMessage(CANNOT_OPEN_SQL_VIEW);
		}
	}

	protected void executeSelectStatement(TableDefinition tableDefinition,
			SQLConsole view) {
		String name = tableDefinition.getFqn();
		view.setQuery(SELECT_FROM + name);
		view.executeStatement(true);
	}

	protected void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				DATABASE_VIEW, message);
	}

}
