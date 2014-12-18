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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;

public class JavascriptServiceTemplateGenerator extends TemplateGenerator {

	private static final String LOG_TAG = "JAVASCRIPT_SERVICE_GENERATOR"; //$NON-NLS-1$

	private JavascriptServiceTemplateModel model;

	public JavascriptServiceTemplateGenerator(
			JavascriptServiceTemplateModel model) {
		this.model = model;
	}

	@Override
	protected Map<String, Object> prepareParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("projectName", model.getProjectName()); //$NON-NLS-1$
		parameters.put("tableName", model.getTableName()); //$NON-NLS-1$
		parameters.put("tableType", model.getTableType()); //$NON-NLS-1$
		parameters.put("entityName", toCamelCase(model.getTableName())); //$NON-NLS-1$
		parameters.put("tableColumns", model.getTableColumns()); //$NON-NLS-1$
		parameters.put("tableColumnsWithoutKeys", //$NON-NLS-1$
				getTableColumnsWithoutKeys(model.getTableColumns()));
		parameters.put("fileName", model.getFileName()); //$NON-NLS-1$
		parameters.put("fileNameNoExtension", model.getFileNameNoExtension()); //$NON-NLS-1$

		parameters.put("INTEGER", java.sql.Types.INTEGER); //$NON-NLS-1$
		parameters.put("BIGINT", java.sql.Types.BIGINT); //$NON-NLS-1$
		parameters.put("SMALLINT", java.sql.Types.SMALLINT); //$NON-NLS-1$

		parameters.put("REAL", java.sql.Types.REAL); //$NON-NLS-1$
		parameters.put("DOUBLE", java.sql.Types.DOUBLE); //$NON-NLS-1$
		// parameters.put("REAL", java.sql.Types.REAL);
		// parameters.put("DECIMAL", java.sql.Types.DECIMAL);
		// parameters.put("NUMERIC", java.sql.Types.NUMERIC);

		parameters.put("VARCHAR", java.sql.Types.VARCHAR); //$NON-NLS-1$
		parameters.put("CHAR", java.sql.Types.CHAR); //$NON-NLS-1$

		parameters.put("DATE", java.sql.Types.DATE); //$NON-NLS-1$
		parameters.put("TIME", java.sql.Types.TIME); //$NON-NLS-1$
		parameters.put("TIMESTAMP", java.sql.Types.TIMESTAMP); //$NON-NLS-1$

//		parameters.put("CLOB", java.sql.Types.CLOB); //$NON-NLS-1$
//		parameters.put("BLOB", java.sql.Types.BLOB); //$NON-NLS-1$

		return parameters;
	}

	private TableColumn[] getTableColumnsWithoutKeys(TableColumn[] tableColumns) {
		if (tableColumns == null) {
			return null;
		}
		List<TableColumn> list = new ArrayList<TableColumn>();
		for (int i = 0; i < tableColumns.length; i++) {
			TableColumn tableColumn = tableColumns[i];
			if (!tableColumn.isKey()) {
				list.add(tableColumn);
			}
		}
		return list.toArray(new TableColumn[] {});
	}

	private Object toCamelCase(String input) {
		if (input == null) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		result.append(input.substring(0, 1).toUpperCase());
		result.append(input.substring(1).toLowerCase());
		return result.toString();
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

	@Override
	protected String getLogTag() {
		return LOG_TAG;
	}

	@Override
	protected byte[] afterGeneration(byte[] bytes) {
		String content = new String(bytes);
		content = content.replace("\\$", "$"); //$NON-NLS-1$ //$NON-NLS-2$
		content = content.replace("\\{", "{"); //$NON-NLS-1$ //$NON-NLS-2$
		content = content.replace("\\}", "}"); //$NON-NLS-1$ //$NON-NLS-2$
		content = content.replace("\\.", "."); //$NON-NLS-1$ //$NON-NLS-2$
		byte[] result = content.getBytes();
		return result;
	}

	@Override
	public void generate() throws Exception {
		super.generate();

		if ("/com/sap/dirigible/ide/template/ui/js/templates/database-crud.js" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {
			generateFile(
					"/com/sap/dirigible/ide/template/ui/js/templates/database-crud.entity", //$NON-NLS-1$
					model.getTargetLocation(), model.getFileNameNoExtension()
							+ ".entity"); //$NON-NLS-1$
			generateFile(
					"/com/sap/dirigible/ide/template/ui/js/templates/database-crud-lib.js", //$NON-NLS-1$
					model.getTargetLocation(), model.getFileNameNoExtension()
							+ "_lib.js"); //$NON-NLS-1$

		}

		if ("/com/sap/dirigible/ide/template/ui/js/templates/ruby-service.rb" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {
			generateFile(
					"/com/sap/dirigible/ide/template/ui/js/templates/ruby-module.rb", //$NON-NLS-1$
					model.getTargetLocation(), "module1.rb"); //$NON-NLS-1$

		}
		
		if ("/com/sap/dirigible/ide/template/ui/js/templates/groovy-service.groovy" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {
			generateFile(
					"/com/sap/dirigible/ide/template/ui/js/templates/groovy-module.groovy", //$NON-NLS-1$
					model.getTargetLocation(), "module1.groovy"); //$NON-NLS-1$

		}
	}
}
