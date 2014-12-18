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

package com.sap.dirigible.ide.db.viewer.views;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.db.viewer.views.actions.DeleteTableAction;
import com.sap.dirigible.ide.db.viewer.views.actions.ExportDataAction;
import com.sap.dirigible.ide.db.viewer.views.actions.RefreshViewAction;
import com.sap.dirigible.ide.db.viewer.views.actions.ShowTableDefinitionAction;
import com.sap.dirigible.ide.db.viewer.views.actions.ViewTableContentAction;

/**
 * Database Viewer represents the structure of the tenant specific schema
 * 
 */
public class DatabaseViewer extends ViewPart implements IDbConnectionFactory {

	private static final String DATABASE_VIEW = Messages.DatabaseViewer_DATABASE_VIEW;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.sap.dirigible.ide.db.viewer.views.DatabaseViewer"; //$NON-NLS-1$

	protected TreeViewer viewer;
	@SuppressWarnings("unused")
	private DrillDownAdapter drillDownAdapter;
	protected Action viewTableContentAction;
	private Action deleteAction;
	private Action exportDataAction;
	private Action doubleClickAction;
	private boolean isOperator;

	private ShowTableDefinitionAction showTableDefinitionAction;

	protected RefreshViewAction refreshViewAction;

	class NameSorter extends ViewerSorter {
		private static final long serialVersionUID = -7067479902071396325L;
	}

	/**
	 * The constructor.
	 */
	public DatabaseViewer() {
		isOperator = CommonParameters.isUserInRole(CommonParameters.ROLE_OPERATOR);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@SuppressWarnings("unused")
	public void createPartControl(Composite parent) {
		PatternFilter filter = new PatternFilter();
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL,
				filter, true);
		viewer = tree.getViewer();
		DrillDownAdapter drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(initContentProvider());
		viewer.setLabelProvider(new DatabaseViewLabelProvider(this));
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
		// "com.sap.dirigible.ide.db.viewer.views.DatabaseViewer");

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	protected IContentProvider initContentProvider() {
		return new DatabaseViewContentProvider(this);
	}

	protected IFilter getSchemaFilter(Connection connection) throws SQLException {
		return null;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			private static final long serialVersionUID = -4330735691305481160L;

			public void menuAboutToShow(IMenuManager manager) {
				DatabaseViewer.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(showTableDefinitionAction);
		manager.add(viewTableContentAction);
		manager.add(exportDataAction);
		manager.add(new Separator());
		if (isOperator) {
			manager.add(deleteAction);
		}
		manager.add(new Separator());
		manager.add(refreshViewAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (!TreeParent.class.isInstance(obj)) {
			if (TreeObject.class.isInstance(obj)) {
				manager.add(showTableDefinitionAction);
				manager.add(viewTableContentAction);
				manager.add(exportDataAction);
				manager.add(new Separator());
				if (isOperator) {
					manager.add(deleteAction);
				}
				manager.add(new Separator());
			}
		}
		manager.add(refreshViewAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(showTableDefinitionAction);
		manager.add(viewTableContentAction);
		manager.add(exportDataAction);
		manager.add(new Separator());
		if (isOperator) {
			manager.add(deleteAction);
		}
		manager.add(new Separator());
		manager.add(refreshViewAction);
	}

	private void makeActions() {
		createRefreshAction();
		createExportDataAction();
		createViewTableContentAction();
		createShowTableDefinitionAction();
		if (isOperator) {
			deleteAction = new DeleteTableAction(viewer);
		}
		doubleClickAction = new ShowTableDefinitionAction(viewer);
		
	}

	protected void createRefreshAction() {
		refreshViewAction = new RefreshViewAction(viewer);
	}

	protected void createViewTableContentAction() {
		viewTableContentAction = new ViewTableContentAction(viewer, SQLConsole.ID);

	}

	protected void createShowTableDefinitionAction() {
		showTableDefinitionAction = new ShowTableDefinitionAction(viewer);
	}
	
	protected void createExportDataAction(){
		exportDataAction = new ExportDataAction(viewer);
	}
	
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	protected void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), DATABASE_VIEW, message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public Connection getDatabaseConnection() throws SQLException {
		return getConnectionFromSelectedDatasource();
	}

	/**
	 * Create connection from the data-source selected at this view
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnectionFromSelectedDatasource()
			throws SQLException {
		DataSource dataSource = DataSourceFacade.getInstance().getDataSource();
		Connection connection = dataSource.getConnection();
		return connection;
	}

	public boolean showSchemes() {
		return false;
	}

}