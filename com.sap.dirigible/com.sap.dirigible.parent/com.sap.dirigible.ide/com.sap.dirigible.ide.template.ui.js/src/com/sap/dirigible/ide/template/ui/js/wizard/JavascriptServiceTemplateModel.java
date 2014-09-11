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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;
import com.sap.dirigible.ide.ui.common.validation.ValidationStatus;
import com.sap.dirigible.repository.api.ICommonConstants;

public class JavascriptServiceTemplateModel extends GenerationModel {

	private static final String ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_GENERATION = Messages.JavascriptServiceTemplateModel_ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_GENERATION;

	private static final String TARGET_LOCATION_IS_NOT_ALLOWED = Messages.JavascriptServiceTemplateModel_TARGET_LOCATION_IS_NOT_ALLOWED;

	private static final Logger logger = Logger
			.getLogger(JavascriptServiceTemplateModel.class);

	private String tableName;

	private String tableType;

	private TableColumn[] tableColumns;

	private boolean columnsInit = false;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public TableColumn[] getTableColumns() {
		if (!columnsInit) {
			createTableColumns();
		}
		return tableColumns; // NOPMD
	}

	public void setTableColumns(TableColumn[] tableColumns) {
		this.tableColumns = tableColumns;
		columnsInit = true;
	}

	@Override
	public IValidationStatus validate() {
		IValidationStatus locationStatus = validateLocation();
		if (locationStatus.hasErrors()) {
			return locationStatus;
		}
		IValidationStatus templateStatus = validateTemplate();
		if (locationStatus.hasErrors()) {
			return locationStatus;
		}
		// if (!validateTableName()) {
		// return false;
		// }
		return ValidationStatus.getValidationStatus(locationStatus,
				templateStatus);
	}

	public IValidationStatus validateLocation() {
		IValidationStatus status;
		try {
			status = validateLocationGeneric();
			if (status.hasErrors()) {
				return status;
			}
			IPath location = new Path(getTargetLocation())
					.append(getFileName());
			// TODO - more precise test for the location ../WebContent/...
			if (location.toString().indexOf(
					ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES) == -1) {
				return ValidationStatus
						.createError(TARGET_LOCATION_IS_NOT_ALLOWED);
			}
		} catch (Exception e) {
			// temp workaround due to another bug - context menu is not context
			// aware => target location and name are null (in the first page of
			// the wizard)
			return ValidationStatus.createError(""); //$NON-NLS-1$
		}
		return status;
	}

	public boolean validateTableName() {
		return tableName != null;
	}

	private void createTableColumns() {

		if (getTableName() == null) {
			return;
		}

		try {

			Connection connection = null;
			try {
				connection = DataSourceFacade.getInstance()
						.getDataSource().getConnection();
				DatabaseMetaData meta = connection.getMetaData();

				List<TableColumn> availableTableColumns = new ArrayList<TableColumn>();

				ResultSet primaryKeys = meta.getPrimaryKeys(null, null,
						getTableName());
				while (primaryKeys.next()) {
					String columnName = primaryKeys.getString("COLUMN_NAME"); //$NON-NLS-1$
					TableColumn tableColumn = new TableColumn(columnName, 0,
							true, true);
					availableTableColumns.add(tableColumn);
				}

				ResultSet columns = meta.getColumns(null, null, getTableName(),
						null);
				while (columns.next()) {
					// columns
					String columnName = columns.getString("COLUMN_NAME"); //$NON-NLS-1$
					int columnType = columns.getInt("DATA_TYPE"); //$NON-NLS-1$
					// column.columnTypeName = columns.getString("TYPE_NAME");
					// column.columnSize = columns.getInt("COLUMN_SIZE");
					// column.isNullable = columns.getInt("NULLABLE") ==
					// DatabaseMetaData.columnNullable;

					TableColumn tableColumn = new TableColumn(columnName,
							columnType, false, true);
					if (!exists(availableTableColumns, tableColumn)) {
						availableTableColumns.add(tableColumn);
					}
				}

				setTableColumns(availableTableColumns
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
		if (getTableName() == null) {
			return false;
		}
		for (Iterator<TableColumn> iterator = availableTableColumns.iterator(); iterator
				.hasNext();) {
			TableColumn tableColumnX = (TableColumn) iterator.next();
			if (tableColumnX.getName().equals(tableColumn.getName())) {
				tableColumnX.setType(tableColumn.getType());
				return true;
			}
		}
		return false;
	}
}
