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

public class HtmlForEntityTemplateTypePage extends TemplateTypeWizardPage {
	
	private static final String ENTITY_USER_INTERFACE = "EntityUserInterface"; //$NON-NLS-1$

	private static final long serialVersionUID = -9074706494058923056L;

	private static final String LIST_OF_ENTITIES_OPEN_UI5 = Messages.HtmlForEntityTemplateTypePage_LIST_OF_ENTITIES_OPEN_UI5;

	private static final String LIST_AND_DETAILS_VIEW_OPEN_UI5 = Messages.HtmlForEntityTemplateTypePage_LIST_AND_DETAILS_VIEW_OPEN_UI5;

	private static final String LIST_AND_MANAGE_VIEW_OPEN_UI5 = Messages.HtmlForEntityTemplateTypePage_LIST_AND_MANAGE_VIEW_OPEN_UI5;

	private static final String DISPLAY_ENTITY_DETAILS_OPEN_UI5 = Messages.HtmlForEntityTemplateTypePage_DISPLAY_ENTITY_DETAILS_OPEN_UI5;

	private static final String NEW_OR_EDIT_ENTITY_OPEN_UI5 = Messages.HtmlForEntityTemplateTypePage_NEW_OR_EDIT_ENTITY_OPEN_UI5;

	private static final String NEW_OR_EDIT_ENTITY = Messages.HtmlForEntityTemplateTypePage_NEW_OR_EDIT_ENTITY;

	private static final String DISPLAY_ENTITY_DETAILS = Messages.HtmlForEntityTemplateTypePage_DISPLAY_ENTITY_DETAILS;

	private static final String LIST_AND_MANAGE_VIEW = Messages.HtmlForEntityTemplateTypePage_LIST_AND_MANAGE_VIEW;

	private static final String LIST_AND_DETAILS_VIEW = Messages.HtmlForEntityTemplateTypePage_LIST_AND_DETAILS_VIEW;

	private static final String LIST_OF_ENTITIES = Messages.HtmlForEntityTemplateTypePage_LIST_OF_ENTITIES;

	private static final String SELECTION_OF_TEMPLATE_TYPE = Messages.HtmlForEntityTemplateTypePage_SELECTION_OF_TEMPLATE_TYPE;

	private static final String SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION = Messages.HtmlForEntityTemplateTypePage_SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION;

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.html.wizard.HtmlForEntityTemplateTypePage"; //$NON-NLS-1$

	private HtmlForEntityTemplateModel model;

	public HtmlForEntityTemplateTypePage(HtmlForEntityTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(SELECTION_OF_TEMPLATE_TYPE);
		setDescription(SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION);
	}

//	@Override
//	protected TemplateType[] prepareTemplateTypes() throws MalformedURLException {
//		TemplateType[] templateTypes = new TemplateType[] {
//				TemplateType
//						.createTemplateType(
//								LIST_OF_ENTITIES,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-list.html", //$NON-NLS-1$
//								"/icons/ui-for-entity-list.png", //$NON-NLS-1$
//								this.getClass()),
//				TemplateType
//						.createTemplateType(
//								LIST_AND_DETAILS_VIEW,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-details.html", //$NON-NLS-1$
//								"/icons/ui-for-entity-details.png", //$NON-NLS-1$
//								this.getClass()),
//				TemplateType
//						.createTemplateType(
//								LIST_AND_MANAGE_VIEW,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-manage.html", //$NON-NLS-1$
//								"/icons/ui-for-entity-manage.png", //$NON-NLS-1$
//								this.getClass()),
//				TemplateType
//						.createTemplateType(
//								LIST_OF_ENTITIES_OPEN_UI5,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-list.html", //$NON-NLS-1$
//								"/icons/ui-for-entity-list.png", //$NON-NLS-1$
//								this.getClass()),
//				TemplateType
//						.createTemplateType(
//								LIST_AND_DETAILS_VIEW_OPEN_UI5,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-details-open-ui5.html", //$NON-NLS-1$
//								"/icons/ui-for-entity-details.png", //$NON-NLS-1$
//								this.getClass()),
//				TemplateType
//						.createTemplateType(
//								LIST_AND_MANAGE_VIEW_OPEN_UI5,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-manage-open-ui5.html", //$NON-NLS-1$
//								"/icons/ui-for-entity-manage.png", //$NON-NLS-1$
//								this.getClass()),
//				TemplateType
//						.createTemplateType(
//								DISPLAY_ENTITY_DETAILS_OPEN_UI5,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-display-open-ui5.html", //$NON-NLS-1$
//								"/icons/ui-for-display-entity.png", //$NON-NLS-1$
//								this.getClass()),
//				TemplateType
//						.createTemplateType(
//								NEW_OR_EDIT_ENTITY_OPEN_UI5,
//								"/com/sap/dirigible/ide/template/ui/html/templates/ui-for-entity-new-or-edit-open-ui5.html", //$NON-NLS-1$
//								"/icons/ui-for-new-entity.png", //$NON-NLS-1$
//								this.getClass()) };
//		return templateTypes;
//	}
	
	@Override
	protected String getCategory() {
		return ENTITY_USER_INTERFACE;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

}
