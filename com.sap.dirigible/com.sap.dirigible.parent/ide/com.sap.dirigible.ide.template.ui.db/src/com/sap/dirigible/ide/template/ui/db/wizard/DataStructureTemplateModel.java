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


public class DataStructureTemplateModel extends TableTemplateModel {

	private String query;

	private String tableName;

	private String[] dsvSampleRows;
	
	public String getQuery() {
		return query;
	}

	public String getTableName() {
		return tableName;
	}

	public String[] getDsvSampleRows() {
		return dsvSampleRows;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setDsvSampleRows(String[] dsvSampleRows) {
		this.dsvSampleRows = dsvSampleRows;
	}

}
