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

package com.sap.dirigible.ide.template.ui.js.wizard;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;

public class TablesTemplateTablePage extends WizardPage {

	private static final long serialVersionUID = -4198853177500786981L;

	private static final String ERROR_ON_LOADING_TABLES_FROM_DATABASE_FOR_GENERATION = Messages.TablesTemplateTablePage_ERROR_ON_LOADING_TABLES_FROM_DATABASE_FOR_GENERATION;

	private static final String AVAILABLE_TABLES_AND_VIEWS = Messages.TablesTemplateTablePage_AVAILABLE_TABLES_AND_VIEWS;

	private static final String SELECT_THE_TABLE_WHICH_WILL_BE_USED_DURING_GENERATION = Messages.TablesTemplateTablePage_SELECT_THE_TABLE_WHICH_WILL_BE_USED_DURING_GENERATION;

	private static final String SELECTION_OF_TABLE = Messages.TablesTemplateTablePage_SELECTION_OF_TABLE;

	private static final Logger logger = Logger
			.getLogger(TablesTemplateTablePage.class);

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.odata.wizard.UIForODataTemplateTablePage"; //$NON-NLS-1$

	private JavascriptServiceTemplateModel model;

	private TableViewer typeViewer;
	
	private Label labelSelected;

	protected TablesTemplateTablePage(JavascriptServiceTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(SELECTION_OF_TABLE);
		setDescription(SELECT_THE_TABLE_WHICH_WILL_BE_USED_DURING_GENERATION);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout());
		createTableField(composite);

		checkPageStatus();
	}

	private void createTableField(Composite parent) {
		final Label label = new Label(parent, SWT.NONE);
		label.setText(AVAILABLE_TABLES_AND_VIEWS);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));

		typeViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER
				| SWT.V_SCROLL);
		typeViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		typeViewer.setContentProvider(new ArrayContentProvider());
		typeViewer.setLabelProvider(new TablesTemplateTablePageLabelProvider());
		typeViewer.setSorter(new ViewerSorter());
		TableName[] tableNames = createTableNames();
		typeViewer.setInput(tableNames);
		typeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateTableNames();
				checkPageStatus();
			}
		});
		updateTableNames();
		labelSelected = new Label(parent, SWT.NONE);
		labelSelected.setText("");
		labelSelected.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false));

	}

	private void updateTableNames() {
		if (typeViewer.getTable().getSelection() != null
				&& typeViewer.getTable().getSelection().length > 0) {
			TableName selectedTableName = (TableName) typeViewer.getTable().getSelection()[0].getData();
			if (selectedTableName != null) {
				model.setTableName(selectedTableName.getName());
				model.setTableType(selectedTableName.getType());
				labelSelected.setText(selectedTableName.getName());
				labelSelected.pack();
			} else {
				model.setTableName(null);
				model.setTableType(null);
				labelSelected.setText("");
				labelSelected.pack();
			}
		} else {
			model.setTableName(null);
			model.setTableType(null);
		}
	}

	private TableName[] createTableNames() {

		List<TableName> availableTableNames = new ArrayList<TableName>();

		try {
			Connection connection = null;
			try {
				connection = DataSourceFacade.getInstance()
						.getDataSource().getConnection();
				DatabaseMetaData meta = connection.getMetaData();

				ResultSet tableNames = meta.getTables(null, null, "%", null); //$NON-NLS-1$
				while (tableNames.next()) {
					String sTableName = tableNames.getString("TABLE_NAME"); //$NON-NLS-1$
					String sTableType = tableNames.getString("TABLE_TYPE"); //$NON-NLS-1$
					if ("TABLE".equals(sTableType) || "VIEW".equals(sTableType)) { //$NON-NLS-1$ //$NON-NLS-2$
						TableName tableName = new TableName(sTableName,
								sTableType);
						availableTableNames.add(tableName);
					}
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (Exception e) {
			logger.error(ERROR_ON_LOADING_TABLES_FROM_DATABASE_FOR_GENERATION,
					e);
		}
		return availableTableNames.toArray(new TableName[] {});
	}

	private void checkPageStatus() {
		setPageComplete(model.validateTableName());
	}

}
