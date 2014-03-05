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
import org.eclipse.jface.wizard.IWizardPage;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;
import com.sap.dirigible.ide.template.ui.common.TemplateWizard;


public class HtmlForEntityTemplateWizard extends TemplateWizard {

	private static final String CREATE_USER_INTERFACE_FOR_RES_TFUL_PERSISTENCE_SERVICE = Messages.HtmlForEntityTemplateWizard_CREATE_USER_INTERFACE_FOR_RES_TFUL_PERSISTENCE_SERVICE;
	private final HtmlForEntityTemplateModel model;
	private final HtmlForEntityTemplateTablePage tablePage;
	private final HtmlForEntityTemplateTypePage typesPage;
	private final HtmlForEntityTemplateTargetLocationPage targetLocationPage;
	private final HtmlForEntityTemplateTitlePage titlePage;

	public HtmlForEntityTemplateWizard(IFile file) {
		setWindowTitle(CREATE_USER_INTERFACE_FOR_RES_TFUL_PERSISTENCE_SERVICE);

		model = new HtmlForEntityTemplateModel();
		model.setSourceResource(file);
		typesPage = new HtmlForEntityTemplateTypePage(model);
		tablePage = new HtmlForEntityTemplateTablePage(model);
		targetLocationPage = new HtmlForEntityTemplateTargetLocationPage(model);
		titlePage = new HtmlForEntityTemplateTitlePage(model);
	}

	@Override
	public void addPages() {
		addPage(typesPage);
		addPage(tablePage);
		addPage(targetLocationPage);
		addPage(titlePage);
	}

	@Override
	public TemplateGenerator getTemplateGenerator() {
		HtmlForEntityTemplateGenerator generator = new HtmlForEntityTemplateGenerator(
				model);
		return generator;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof HtmlForEntityTemplateTypePage) {
			if (("/com/sap/dirigible/ide/template/ui/html/templates/angular/display_single_entity/ui-for-display-single-entity.html" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return targetLocationPage;
			}
			if (("/com/sap/dirigible/ide/template/ui/html/templates/angular/new_or_edit/ui-for-new-or-edit-entity.html" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return targetLocationPage;
			}
			if (("/com/sap/dirigible/ide/template/ui/html/templates/open_ui5/display_single_entity/ui-for-display-single-entity-open-ui5.html" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return targetLocationPage;
			}
			if (("/com/sap/dirigible/ide/template/ui/html/templates/open_ui5/new_or_edit/ui-for-entity-new-or-edit-open-ui5.html" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return targetLocationPage;
			}
		}
		return super.getNextPage(page);
	}
	
	@Override
	public boolean performFinish() {
		boolean result = super.performFinish();
		if (result) {
			StatusLineManagerUtil.setInfoMessage(String.format(
					StatusLineManagerUtil.ARTIFACT_HAS_BEEN_CREATED,
					model.getFileName()));
		}
		return result;
	}

}
