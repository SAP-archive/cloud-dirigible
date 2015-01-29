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

package com.sap.dirigible.ide.db.export;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.db.export.TableColumn;
import com.sap.dirigible.ide.logging.Logger;

public class DataFinder {

	private static final String DELIMETER = "|";
	private static final String DATA_TYPE = "DATA_TYPE";
	private static final String COLUMN_NAME = "COLUMN_NAME";
	private static final String SELECT_FROM = "SELECT * FROM ";
	private static final String THERE_IS_NO_DATA_IN_TABLE = "There is no data in table ";
	private static final String COULD_NOT_RETRIEVE_TABLE_DATA = "Could not rettrieve table data reason: Table name is null";
	private static final String ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_TABLE = Messages.DataExportDialog_ERROR_ON_LOADING_TABLES_FROM_DATABASE_FOR_GENERATION;

	private String tableName;
	private String tableType;
	private TableColumn[] tableColumns;

	private static final Logger logger = Logger.getLogger(DataFinder.class);

	public String getTableData() {
		String data = "";

		if (getTableName() == null) {
			data = COULD_NOT_RETRIEVE_TABLE_DATA;
			logger.error(COULD_NOT_RETRIEVE_TABLE_DATA);
			return data;
		}

		try {

			Connection connection = null;

			try {
				connection = DataSourceFacade.getInstance().getDataSource().getConnection();

				DatabaseMetaData meta = connection.getMetaData();

				List<TableColumn> availableTableColumns = new ArrayList<TableColumn>();

				ResultSet primaryKeys = meta.getPrimaryKeys(null, null,
						getTableName());

				while (primaryKeys.next()) {
					// pk columns
					String columnName = primaryKeys.getString(COLUMN_NAME); //$NON-NLS-1$
					TableColumn tableColumn = new TableColumn(columnName, 0,
							true, true);
					availableTableColumns.add(tableColumn);
				}

				ResultSet columns = meta.getColumns(null, null, getTableName(),
						null);

				while (columns.next()) {
					// columns
					String columnName = columns.getString(COLUMN_NAME); //$NON-NLS-1$
					int columnType = columns.getInt(DATA_TYPE); //$NON-NLS-1$

					TableColumn tableColumn = new TableColumn(columnName,
							columnType, false, true);
					if (!exists(availableTableColumns, tableColumn)) {
						availableTableColumns.add(tableColumn);
					}
				}

				setTableColumns(availableTableColumns.toArray(new TableColumn[] {}));
				data = getDataForTable();
				
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (Exception e) {
			logger.error(ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_TABLE
					+ getTableName(), e);
		}
		return data;
	}

	private String getDataForTable() {
		String sql = null;
		String result = null;
		Statement statement = null;
		ResultSet resultSet = null;
		StringBuilder sb = new StringBuilder();
		String tableName = getTableName();
		TableColumn[] columns = getTableColumns();
		Connection connection = null;
		
		try {
			connection = DataSourceFacade.getInstance().getDataSource().getConnection();
			
			statement = connection.createStatement();
			sql = SELECT_FROM + tableName;
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				for (TableColumn column : columns) {
					sb.append(resultSet.getString(column.getName()));
					sb.append(DELIMETER);
				}
				sb.deleteCharAt(sb.lastIndexOf(DELIMETER));
				sb.append("\n");
			}
			resultSet.close();
			statement.close();

		} catch (SQLException se) {
			logger.error(se.getMessage(), se);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		result = sb.toString();

		if (result.equalsIgnoreCase("")) {
			return THERE_IS_NO_DATA_IN_TABLE+tableName;
		} else {
			return result;
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
		return tableColumns;
	}

	public void setTableColumns(TableColumn[] tableColumns) {
		this.tableColumns = tableColumns;
	}

}
