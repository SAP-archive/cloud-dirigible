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

package com.sap.dirigible.ide.template.ui.is.wizard;

import org.eclipse.core.resources.IResource;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;
import com.sap.dirigible.ide.template.ui.common.TemplateWizard;

public class IntegrationServiceTemplateWizard extends TemplateWizard {

	private static final String CREATE_INTEGRATION_SERVICE = Messages.IntegrationServiceTemplateWizard_CREATE_INTEGRATION_SERVICE;
	private final IntegrationServiceTemplateModel model;
	private final IntegrationServiceTemplateTypePage typesPage;
	private final IntegrationServiceTemplateTargetLocationPage targetLocationPage;
	private final IntegrationServiceTemplateServiceParametersPage serviceEndpointPage;

	public IntegrationServiceTemplateWizard(IResource resource) {
		setWindowTitle(CREATE_INTEGRATION_SERVICE);

		model = new IntegrationServiceTemplateModel();
		model.setSourceResource(resource);
		typesPage = new IntegrationServiceTemplateTypePage(model);
		targetLocationPage = new IntegrationServiceTemplateTargetLocationPage(
				model);
		serviceEndpointPage = new IntegrationServiceTemplateServiceParametersPage(
				model);
	}

	@Override
	public void addPages() {
		addPage(typesPage);
		addPage(targetLocationPage);
		addPage(serviceEndpointPage);
	}

	@Override
	public TemplateGenerator getTemplateGenerator() {
		IntegrationServiceTemplateGenerator generator = new IntegrationServiceTemplateGenerator(
				model);
		return generator;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
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
