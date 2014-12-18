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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;
import com.sap.dirigible.repository.api.ICommonConstants;

public class HtmlForEntityTemplateGenerator extends TemplateGenerator {

	private static final String LOG_TAG = "HTML_FOR_ENTITY_GENERATOR"; //$NON-NLS-1$

	private HtmlForEntityTemplateModel model;

	public HtmlForEntityTemplateGenerator(HtmlForEntityTemplateModel model) {
		this.model = model;
	}

	@Override
	protected Map<String, Object> prepareParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pageTitle", model.getPageTitle()); //$NON-NLS-1$
		parameters.put("tableName", model.getTableName()); //$NON-NLS-1$
		parameters.put("tableColumns", model.getTableColumns()); //$NON-NLS-1$
		parameters.put("fileName", model.getFileName()); //$NON-NLS-1$
		parameters.put("serviceFileName", generateServiceFileName()); //$NON-NLS-1$
		parameters.put("createDataModel", createDataModel()); //$NON-NLS-1$
		parameters.put("entityName", getEntityName()); //$NON-NLS-1$
		// parameters.put("projectName", model.getProjectName());
		return parameters;
	}

	protected String getEntityName() {
		return "data"; //$NON-NLS-1$
	}

	protected String createDataModel() {
		return "tableModel = new sap.ui.model.json.JSONModel();\n		tableModel.loadData(\"" //$NON-NLS-1$
				+ generateServiceFileName() + "\");"; //$NON-NLS-1$
	}

	protected Object generateServiceFileName() {
		// /project1/ScriptingServices/te1.entity
		IFile source = model.getSourceFile();
		String entityPath = source.getFullPath().toString();
		String result = entityPath.replaceAll("/" //$NON-NLS-1$
				+ ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES, ""); //$NON-NLS-1$
		result = result.replaceAll(source.getFileExtension(), ""); //$NON-NLS-1$
		result += "js"; //$NON-NLS-1$
		return "/dirigible/js" + result; //$NON-NLS-1$
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

	@Override
	protected String getLogTag() {
		return LOG_TAG;
	}

}
