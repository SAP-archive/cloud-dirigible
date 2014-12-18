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

import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateTypeWizardPage;
import com.sap.dirigible.repository.api.ICommonConstants;

public class HtmlTemplateTypePage extends TemplateTypeWizardPage {

	private static final long serialVersionUID = -4671616868380963154L;

	private static final String SAMPLE_FORM = Messages.HtmlTemplateTypePage_SAMPLE_FORM;

	private static final String INDEX_PAGE_WITH_MAIN_MENU = Messages.HtmlTemplateTypePage_INDEX_PAGE_WITH_MAIN_MENU;

	private static final String SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION = Messages.HtmlTemplateTypePage_SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION;

	private static final String SELECTION_OF_TEMPLATE_TYPE = Messages.HtmlTemplateTypePage_SELECTION_OF_TEMPLATE_TYPE;

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.html.wizard.HtmlTemplateTypePage"; //$NON-NLS-1$

	private HtmlTemplateModel model;

	protected HtmlTemplateTypePage(HtmlTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(SELECTION_OF_TEMPLATE_TYPE);
		setDescription(SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION);
	}

	@Override
	protected String getCategory() {
		return ICommonConstants.ARTIFACT_TYPE.WEB_CONTENT;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

}
