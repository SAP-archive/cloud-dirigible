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

import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateTypeWizardPage;
import com.sap.dirigible.repository.api.ICommonConstants;

public class JavascriptServiceTemplateTypePage extends TemplateTypeWizardPage {

	private static final long serialVersionUID = -9193812975192988551L;

	private static final String RUBY_SERVICE = Messages.JavascriptServiceTemplateTypePage_RUBY_SERVICE;

	private static final String GROOVY_SERVICE = Messages.JavascriptServiceTemplateTypePage_GROOVY_SERVICE;
	
	private static final String ENTITY_SERVICE_ON_TABLE = Messages.JavascriptServiceTemplateTypePage_ENTITY_SERVICE_ON_TABLE;

	private static final String DATABASE_ACCESS_SAMPLE = Messages.JavascriptServiceTemplateTypePage_DATABASE_ACCESS_SAMPLE;

	private static final String SERVER_SIDE_JAVA_SCRIPT_GUID_GENERATOR_LIBRARY = Messages.JavascriptServiceTemplateTypePage_SERVER_SIDE_JAVA_SCRIPT_GUID_GENERATOR_LIBRARY;

	private static final String BLANK_SERVER_SIDE_JAVA_SCRIPT_SERVICE = Messages.JavascriptServiceTemplateTypePage_BLANK_SERVER_SIDE_JAVA_SCRIPT_SERVICE;

	private static final String SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION = Messages.JavascriptServiceTemplateTypePage_SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION;

	private static final String SELECTION_OF_TEMPLATE_TYPE = Messages.JavascriptServiceTemplateTypePage_SELECTION_OF_TEMPLATE_TYPE;

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.js.wizard.JavascriptServiceTemplateTypePage"; //$NON-NLS-1$

	private JavascriptServiceTemplateModel model;

	protected JavascriptServiceTemplateTypePage(
			JavascriptServiceTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(SELECTION_OF_TEMPLATE_TYPE);
		setDescription(SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION);
	}

	@Override
	protected String getCategory() {
		return ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

}
