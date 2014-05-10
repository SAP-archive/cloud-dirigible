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

package com.sap.dirigible.ide.template.ui.db.wizard;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.common.ICommonConstants;
import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;
import com.sap.dirigible.ide.ui.common.validation.ValidationStatus;

public class TableTemplateModel extends GenerationModel {
	
	private static final String TABLE_OR_VIEW_WITH_THE_SAME_NAME_ALREADY_EXISTS = Messages.TableTemplateModel_TABLE_OR_VIEW_WITH_THE_SAME_NAME_ALREADY_EXISTS;

	private static final Logger logger = Logger.getLogger(TableTemplateModel.class);

	private static final String TARGET_LOCATION_IS_NOT_ALLOWED = Messages.TableTemplateModel_TARGET_LOCATION_IS_NOT_ALLOWED;
	private static final String NO_PRIMARY_KEY_FOUND = Messages.TableTemplateModel_NO_PRIMARY_KEY_FOUND;
	private static final String DUPLICATE_COLUMN_NAMES_FOUND = Messages.TableTemplateModel_DUPLICATE_COLUMN_NAMES_FOUND;
	private static final String NO_COLUMNS_DEFINED = Messages.TableTemplateModel_NO_COLUMNS_DEFINED;
	private ColumnDefinition[] columnDefinitions;

	public TableTemplateModel() {
		super();
	}

	public ColumnDefinition[] getColumnDefinitions() {
		return columnDefinitions; // NOPMD
	}

	public void setColumnDefinitions(ColumnDefinition[] columnDefinitions) {
		this.columnDefinitions = columnDefinitions;
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
			// TODO - more precise test for the location ../DataStructures/...
			if (location.toString().indexOf(
					ICommonConstants.ARTIFACT_TYPE.DATA_STRUCTURES) == -1) {
				return ValidationStatus
						.createError(TARGET_LOCATION_IS_NOT_ALLOWED);
			}
			
			// check the file name against the existing table names
			if (isTableExists(getFileNameNoExtension().toLowerCase())
					|| isTableExists(getFileNameNoExtension().toUpperCase())) {
				return ValidationStatus
						.createError(TABLE_OR_VIEW_WITH_THE_SAME_NAME_ALREADY_EXISTS);
			}
			
		} catch (Exception e) {
			// temp workaround due to another bug - context menu is not context
			// aware => target location and name are null (in the first page of
			// the wizard)
			return ValidationStatus.createError(""); //$NON-NLS-1$
		}
		return status;
	}

	private boolean isTableExists(String tableName) {
		try {
			DataSource dataSource = DataSourceFacade.getInstance().getDataSource();
			Connection connection = dataSource.getConnection();

			try {
				// get the database metadata
				DatabaseMetaData dmd = connection.getMetaData();
		
				ResultSet rs = dmd.getTables(null, null, tableName, CommonParameters.TABLE_TYPES);
	
				try {
					if (rs.next()) {
						return true;
					}
				} finally {
					rs.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	public IValidationStatus validateColumnDefinitions() {
		if (columnDefinitions.length <= 0) {
			return ValidationStatus.createError(NO_COLUMNS_DEFINED);
		}
		if (duplicateNames(columnDefinitions)) {
			return ValidationStatus.createError(DUPLICATE_COLUMN_NAMES_FOUND);
		}
		if (!thereIsPrimaryKey()) {
			return ValidationStatus.createError(NO_PRIMARY_KEY_FOUND);
		}
		return ValidationStatus.createOk();
	}

	private boolean duplicateNames(ColumnDefinition[] columnDefinitions) {
		Set<String> temp = new HashSet<String>();
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (temp.contains(columnDefinition.getName()))
				return true;
			temp.add(columnDefinition.getName());
		}
		return false;
	}

	private boolean thereIsPrimaryKey() {
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (columnDefinition.isPrimaryKey()) {
				return true;
			}
		}
		return false;
	}

}