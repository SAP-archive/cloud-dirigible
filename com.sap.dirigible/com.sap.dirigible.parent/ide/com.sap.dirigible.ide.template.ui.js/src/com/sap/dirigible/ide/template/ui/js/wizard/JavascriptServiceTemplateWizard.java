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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizardPage;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;
import com.sap.dirigible.ide.template.ui.common.TemplateWizard;

public class JavascriptServiceTemplateWizard extends TemplateWizard {

	private static final String CREATE_SCRIPTING_SERVICE = Messages.JavascriptServiceTemplateWizard_CREATE_SCRIPTING_SERVICE;
	private final JavascriptServiceTemplateModel model;
	private final JavascriptServiceTemplateTypePage typesPage;
	private final TablesTemplateTablePage tablesTemplateTablePage;
	private final JavascriptServiceTemplateTargetLocationPage targetLocationPage;

	public JavascriptServiceTemplateWizard(IResource resource) {
		setWindowTitle(CREATE_SCRIPTING_SERVICE);

		model = new JavascriptServiceTemplateModel();
		model.setSourceResource(resource);
		typesPage = new JavascriptServiceTemplateTypePage(model);
		tablesTemplateTablePage = new TablesTemplateTablePage(model);
		targetLocationPage = new JavascriptServiceTemplateTargetLocationPage(
				model);
	}

	@Override
	public void addPages() {
		addPage(typesPage);
		addPage(tablesTemplateTablePage);
		addPage(targetLocationPage);
	}

	@Override
	public TemplateGenerator getTemplateGenerator() {
		JavascriptServiceTemplateGenerator generator = new JavascriptServiceTemplateGenerator(
				model);
		return generator;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof JavascriptServiceTemplateTypePage) {
			if (("/com/sap/dirigible/ide/template/ui/js/templates/database-access.js" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return tablesTemplateTablePage;
			} else if (("/com/sap/dirigible/ide/template/ui/js/templates/database-crud.js" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return tablesTemplateTablePage;
			} else {
				return targetLocationPage;
			}
		}
		if (page instanceof TablesTemplateTablePage) {
			return targetLocationPage;
		}
		return super.getNextPage(page);
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		if (page instanceof TablesTemplateTablePage) {
			return typesPage;
		}
		if (page instanceof JavascriptServiceTemplateTargetLocationPage) {
			if (("/com/sap/dirigible/ide/template/ui/js/templates/database-access.js" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return tablesTemplateTablePage;
			} else if (("/com/sap/dirigible/ide/template/ui/js/templates/database-crud.js" //$NON-NLS-1$
					.equals(model.getTemplate().getLocation()))) {
				return tablesTemplateTablePage;
			}
		}
		return super.getPreviousPage(page);
	}

	@Override
	protected String openEditorForFileWithExtension() {
		return CommonParameters.JAVASCRIPT_SERVICE_EXTENSION;
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
