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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.common.ICommonConstants;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;
import com.sap.dirigible.ide.ui.common.validation.ValidationStatus;

public class HtmlForEntityTemplateModel extends GenerationModel {

	private static final String TARGET_LOCATION_IS_NOT_ALLOWED = Messages.HtmlForEntityTemplateModel_TARGET_LOCATION_IS_NOT_ALLOWED;

	private String tableName;

	private String tableType;

	private TableColumn[] tableColumns;

	private String pageTitle;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public TableColumn[] getOriginalTableColumns() {
		return this.tableColumns; // NOPMD
	}

	public TableColumn[] getTableColumns() {
		TableColumn[] originalCased = this.tableColumns;
		TableColumn[] lowerCased = new TableColumn[originalCased.length];
		for (int i = 0; i < originalCased.length; i++) {
			TableColumn originalColumn = originalCased[i];
			TableColumn lowerColumn = new TableColumn(originalColumn.getName()
					.toLowerCase(), originalColumn.isKey(),
					originalColumn.isVisible(), originalColumn.getType(),
					originalColumn.getSize());
			lowerCased[i] = lowerColumn;
		}
		return lowerCased;
	}

	public void setTableColumns(TableColumn[] tableColumns) {
		this.tableColumns = tableColumns;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
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
			// TODO - more precise test for the location ../WebContent/...
			if (location.toString().indexOf(
					ICommonConstants.ARTIFACT_TYPE.WEB_CONTENT) == -1) {
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

	public boolean validateTableColumns() {
		for (int i = 0; i < tableColumns.length; i++) {
			TableColumn tableColumn = tableColumns[i];
			if (tableColumn.isVisible()) {
				return true;
			}
		}
		return false;
	}

	public IFile getSourceFile() {
		IResource resource = getSourceResource();
		if (resource instanceof IFile) {
			return (IFile) resource;
		}
		return null;
	}

}
