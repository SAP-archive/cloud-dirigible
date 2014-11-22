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

package com.sap.dirigible.ide.template.ui.html.wizard;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;

public class HtmlForEntityTemplateTablePage extends WizardPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2145208425464294046L;

	private static final String AVAILABLE_FIELDS = IDEWorkbenchMessages.HtmlForEntityTemplateTablePage_AVAILABLE_FIELDS;

	private static final String ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_GENERATION = IDEWorkbenchMessages.HtmlForEntityTemplateTablePage_ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_GENERATION;

	private static final String SELECT_THE_VISIBLE_FIELDS_WHICH_WILL_BE_USED_DURING_GENERATION = IDEWorkbenchMessages.HtmlForEntityTemplateTablePage_SELECT_THE_VISIBLE_FIELDS_WHICH_WILL_BE_USED_DURING_GENERATION;

	private static final String SELECTION_OF_FIELDS = IDEWorkbenchMessages.HtmlForEntityTemplateTablePage_SELECTION_OF_FIELDS;

	private static final Logger logger = Logger
			.getLogger(HtmlForEntityTemplateTablePage.class);

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.html.wizard.HtmlForEntityTemplateTablePage"; //$NON-NLS-1$

	private HtmlForEntityTemplateModel model;

	private TableViewer typeViewer;

	protected HtmlForEntityTemplateTablePage(HtmlForEntityTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(SELECTION_OF_FIELDS);
		setDescription(SELECT_THE_VISIBLE_FIELDS_WHICH_WILL_BE_USED_DURING_GENERATION);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout());
		createTableField(composite);

		checkPageStatus();
	}

	private void selectAll(boolean b) {
		TableColumn[] tableColumns = (TableColumn[]) typeViewer.getInput();
		TableItem[] items = typeViewer.getTable().getItems();
		int selectionIndex = typeViewer.getTable().getSelectionIndex();
		for (int j = 0; j < items.length; j++) {
			TableItem tblItem = items[j];
			typeViewer.getTable().setSelection(j);
			tableColumns[j].setVisible(tblItem.getChecked());
			items[typeViewer.getTable().getSelectionIndex()].setChecked(b
					|| tableColumns[j].isKey());
			tableColumns[j].setVisible(true);
		}
		typeViewer.getTable().setSelection(selectionIndex);
	};

	private void createTableField(Composite parent) {
		Composite upperPart = new Composite(parent, SWT.NONE);
		upperPart.setLayout(new GridLayout(3, false));
		upperPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final Label label = new Label(upperPart, SWT.NONE);
		label.setText(AVAILABLE_FIELDS);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false));

		Button selectButton = new Button(upperPart, SWT.PUSH);
		selectButton.setText(IDEWorkbenchMessages.WizardTransferPage_selectAll);
		selectButton.setLayoutData(new GridData(SWT.END, SWT.FILL, true, true));
		selectButton.setFont(JFaceResources.getDialogFont());
		selectButton.addSelectionListener(new SelectionAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7095187791495950403L;

			public void widgetSelected(SelectionEvent event) {
				selectAll(true);
			}
		});

		Button deselectButton = new Button(upperPart, SWT.PUSH);
		deselectButton
				.setText(IDEWorkbenchMessages.WizardTransferPage_deselectAll);
		deselectButton.setLayoutData(new GridData(SWT.END, SWT.FILL, false,
				false));
		deselectButton.setFont(JFaceResources.getDialogFont());
		deselectButton.addSelectionListener(new SelectionAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7117397741755265980L;

			public void widgetSelected(SelectionEvent event) {
				selectAll(false);
			}
		});

		typeViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER
				| SWT.V_SCROLL | SWT.CHECK);
		typeViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		typeViewer.setContentProvider(new ArrayContentProvider());
		typeViewer.setLabelProvider(new HtmlForEntityTemplateTablePageLabelProvider());
		//typeViewer.setSorter(new ViewerSorter());
		createTableColumns();
		typeViewer.setInput(model.getOriginalTableColumns());
		typeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateTableColumns();
				checkPageStatus();
			}
		});
		updateTableColumns();

	}

	private void updateTableColumns() {
		TableColumn[] tableColumns = (TableColumn[]) typeViewer.getInput();
		TableItem[] items = typeViewer.getTable().getItems();
		int selectionIndex = typeViewer.getTable().getSelectionIndex();
		for (int j = 0; j < items.length; j++) {
			TableItem tblItem = items[j];
			typeViewer.getTable().setSelection(j);
			tableColumns[j].setVisible(tblItem.getChecked());
			if (tableColumns[j].isKey()) {
				items[typeViewer.getTable().getSelectionIndex()]
						.setChecked(true);
				tableColumns[j].setVisible(true);
			}
		}
		typeViewer.getTable().setSelection(selectionIndex);
	}

	private void createTableColumns() {
		try {
			parseEntityDescriptor();

			Connection connection = getConnection();
			try {
				DatabaseMetaData meta = connection.getMetaData();

				List<TableColumn> availableTableColumns = new ArrayList<TableColumn>();

				ResultSet primaryKeys = meta.getPrimaryKeys(null, null,
						model.getTableName());
				List<String> primaryKeysList = new ArrayList<String>();
				while (primaryKeys.next()) {
					String columnName = primaryKeys.getString("COLUMN_NAME"); //$NON-NLS-1$
					// TableColumn tableColumn = new TableColumn(columnName,
					// true, true, null, 0);
					// availableTableColumns.add(tableColumn);
					primaryKeysList.add(columnName);
				}

				ResultSet columns = meta.getColumns(null, null,
						model.getTableName(), null);
				while (columns.next()) {
					// columns
					String columnName = columns.getString("COLUMN_NAME"); //$NON-NLS-1$
					String columnType = columns.getString("TYPE_NAME"); //$NON-NLS-1$
					int columnSize = columns.getInt("COLUMN_SIZE"); //$NON-NLS-1$

					// column.columnType = columns.getInt("DATA_TYPE");
					// column.columnTypeName = columns.getString("TYPE_NAME");
					// column.columnSize = columns.getInt("COLUMN_SIZE");
					// column.isNullable = columns.getInt("NULLABLE") ==
					// DatabaseMetaData.columnNullable;
					TableColumn tableColumn = null;
					if (primaryKeysList.contains(columnName)) {
						tableColumn = new TableColumn(columnName, true, true,
								columnType, columnSize);
					} else {
						tableColumn = new TableColumn(columnName, false, true,
								columnType, columnSize);
					}
					if (!exists(availableTableColumns, tableColumn)) {
						availableTableColumns.add(tableColumn);
					}

				}

				model.setTableColumns(availableTableColumns
						.toArray(new TableColumn[] {}));
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (Exception e) {
			logger.error(
					ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_GENERATION,
					e);
		}
	}

	private boolean exists(List<TableColumn> availableTableColumns,
			TableColumn tableColumn) {
		for (Iterator<TableColumn> iterator = availableTableColumns.iterator(); iterator
				.hasNext();) {
			TableColumn tableColumnX = (TableColumn) iterator.next();
			if (tableColumnX.getName().equals(tableColumn.getName())) {
				return true;
			}
		}
		return false;
	}

	protected void parseEntityDescriptor() throws Exception {
		// {"tableName":"SOME_TABLE","tableType":"TABLE"}
		JsonParser parser = new JsonParser();
		JsonObject entityService = (JsonObject) parser
				.parse(new InputStreamReader(model.getSourceFile()
						.getContents()));
		model.setTableName(entityService.get("tableName").getAsString()); //$NON-NLS-1$
		model.setTableType(entityService.get("tableType").getAsString()); //$NON-NLS-1$
	}

	private void checkPageStatus() {
		setPageComplete(model.validateTableColumns());
	}

	public HtmlForEntityTemplateModel getModel() {
		return model;
	}

	protected Connection getConnection() throws SQLException {
		try {
			return DataSourceFacade.getInstance().getDataSource()
					.getConnection();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}
}
