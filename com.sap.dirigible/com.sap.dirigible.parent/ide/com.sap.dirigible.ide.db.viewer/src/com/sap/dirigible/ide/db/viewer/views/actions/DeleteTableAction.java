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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.sap.dirigible.ide.db.viewer.views.IDbConnectionFactory;
import com.sap.dirigible.ide.db.viewer.views.TableDefinition;
import com.sap.dirigible.ide.db.viewer.views.TreeObject;
import com.sap.dirigible.ide.db.viewer.views.TreeParent;
import com.sap.dirigible.ide.logging.Logger;

/**
 * Action that will trigger opening of Table Definition editor.
 * 
 */
public class DeleteTableAction extends Action {

	private static final String DROP_TABLE = "DROP TABLE "; //$NON-NLS-1$

	private static final String DATABASE_VIEW = Messages.DeleteTableAction_DATABASE_VIEW;

	private static final String FAILED_TO_DELETE_TABLE_S = Messages.DeleteTableAction_FAILED_TO_DELETE_TABLE_S;

	private static final Logger logger = Logger.getLogger(DeleteTableAction.class);

	private static final String WARNING_THIS_ACTION_WILL_DELETE_THE_TABLE_AND_ALL_OF_ITS_CONTENT_CONTINUE = Messages.DeleteTableAction_WARNING_THIS_ACTION_WILL_DELETE_THE_TABLE_AND_ALL_OF_ITS_CONTENT_CONTINUE;

	private static final String WILL_DELETE_THE_TABLE_AND_ITS_CONTENT = Messages.DeleteTableAction_WILL_DELETE_THE_TABLE_AND_ITS_CONTENT;

	private static final String DELETE_TABLE = Messages.DeleteTableAction_DELETE_TABLE;

	private static final long serialVersionUID = 3872859942737870851L;

	private TreeViewer viewer;

	public DeleteTableAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(DELETE_TABLE);
		setToolTipText(WILL_DELETE_THE_TABLE_AND_ITS_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		TreeParent parent = null;

		for (Iterator nextSelection = selection.iterator(); nextSelection.hasNext();) {
			Object obj = nextSelection.next();
			if (TreeObject.class.isInstance(obj)) {
				if (((TreeObject) obj).getTableDefinition() != null) {
					TableDefinition tableDefinition = ((TreeObject) obj).getTableDefinition();

					boolean confirm = MessageDialog
							.openConfirm(
									viewer.getControl().getShell(),
									DELETE_TABLE,
									String.format(
											WARNING_THIS_ACTION_WILL_DELETE_THE_TABLE_AND_ALL_OF_ITS_CONTENT_CONTINUE,
											tableDefinition.getTableName()));
					if (!confirm) {
						continue;
					}
					parent = ((TreeObject) obj).getParent();
					IDbConnectionFactory connectionFactory = parent.getConnectionFactory();
					try {
						deleteTable(tableDefinition, connectionFactory);
					} catch (SQLException e) {
						showMessage(String.format(FAILED_TO_DELETE_TABLE_S,
								tableDefinition.getTableName()));
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
		if (parent != null) {
			RefreshViewAction refresh = new RefreshViewAction(viewer, parent.getChildren()[0]);
			refresh.run();
		}
	}

	protected void showMessage(String message) {
		MessageDialog.openError(viewer.getControl().getShell(), DATABASE_VIEW, message);
	}

	private void deleteTable(TableDefinition tableDefinition, IDbConnectionFactory connectionFactory)
			throws SQLException {
		Connection connection = connectionFactory.getDatabaseConnection();
		try {
			Statement createStatement = connection.createStatement();
			String name = tableDefinition.getFqn();
			createStatement.execute(DROP_TABLE + name);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
