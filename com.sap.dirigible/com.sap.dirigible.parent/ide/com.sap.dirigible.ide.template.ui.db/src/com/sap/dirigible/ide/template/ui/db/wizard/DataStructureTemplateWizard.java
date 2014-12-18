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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizardPage;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;
import com.sap.dirigible.ide.template.ui.common.TemplateWizard;

public class DataStructureTemplateWizard extends TemplateWizard {

	private final DataStructureTemplateModel model;
	private final DataStructureTemplateTypePage typesPage;
	private final DataStructureTemplateStructurePage structurePage;
	private final DataStructureTemplateQueryPage queryPage;
	private final DataStructureTemplateDSVPage dsvPage;
	private final DataStructureTemplateTargetLocationPage targetLocationPage;

	public DataStructureTemplateWizard(IResource resource) {
		setWindowTitle(Messages.DataStructureTemplateWizard_CREATE_DATA_STRUCTURE);

		model = new DataStructureTemplateModel();
		model.setSourceResource(resource);
		typesPage = new DataStructureTemplateTypePage(model);
		structurePage = new DataStructureTemplateStructurePage(model);
		queryPage = new DataStructureTemplateQueryPage(model);
		dsvPage = new DataStructureTemplateDSVPage(model);
		targetLocationPage = new DataStructureTemplateTargetLocationPage(model);
	}

	@Override
	public void addPages() {
		addPage(typesPage);
		addPage(structurePage);
		addPage(queryPage);
		addPage(dsvPage);
		addPage(targetLocationPage);
	}

	@Override
	public TemplateGenerator getTemplateGenerator() {
		DataStructureTemplateGenerator generator = new DataStructureTemplateGenerator(model);
		return generator;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage nextPage = null;
		if (page instanceof DataStructureTemplateTypePage) {
			if (DataStructureTemplateLocations.TABLE //$NON-NLS-1$
					.equals(model.getTemplate().getLocation())) {
				nextPage = structurePage;
			}else if (DataStructureTemplateLocations.VIEW //$NON-NLS-1$
					.equals(model.getTemplate().getLocation())) {
				nextPage = queryPage;
			}else if (DataStructureTemplateLocations.DSV //$NON-NLS-1$
					.equals(model.getTemplate().getLocation())) {
				nextPage = dsvPage;
			}
		} else if (page instanceof DataStructureTemplateStructurePage
				|| page instanceof DataStructureTemplateDSVPage) {
			nextPage = targetLocationPage;
		} else {
			nextPage = super.getNextPage(page);
		}
		
		return nextPage;
	}

	@Override
	public boolean performFinish() {
		boolean result = super.performFinish();
		if (result) {
			StatusLineManagerUtil.setInfoMessage(String.format(
					StatusLineManagerUtil.ARTIFACT_HAS_BEEN_CREATED, model.getFileName()));
		}
		return result;
	}

}
