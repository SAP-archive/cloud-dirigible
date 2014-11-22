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

package com.sap.dirigible.ide.template.ui.ed.wizard;

import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateTypeWizardPage;
import com.sap.dirigible.repository.api.ICommonConstants;

public class ExtensionDefinitionTemplateTypePage extends TemplateTypeWizardPage {

	private static final String MAIN_ACCESS_FILE = Messages.ExtensionDefinitionTemplateTypePage_MAIN_ACCESS_FILE;

	private static final String SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION = Messages.ExtensionDefinitionTemplateTypePage_SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION;

	private static final String SELECTION_OF_TEMPLATE_TYPE = Messages.ExtensionDefinitionTemplateTypePage_SELECTION_OF_TEMPLATE_TYPE;

	private static final long serialVersionUID = -1269424557332755529L;

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.sc.wizard.ExtensionDefinitionTemplateTypePage"; //$NON-NLS-1$

	private ExtensionDefinitionTemplateModel model;

	protected ExtensionDefinitionTemplateTypePage(
			ExtensionDefinitionTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(SELECTION_OF_TEMPLATE_TYPE);
		setDescription(SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION);
	}

	@Override
	protected String getCategory() {
		return ICommonConstants.ARTIFACT_TYPE.EXTENSION_DEFINITIONS;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

}
