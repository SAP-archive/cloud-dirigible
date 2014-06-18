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

import com.sap.dirigible.ide.common.ICommonConstants;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateTargetLocationPage;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;

public class ExtensionDefinitionTemplateTargetLocationPage extends TemplateTargetLocationPage {

	private static final long serialVersionUID = 5413819137031452222L;

	private static final String EXTENSION_EXTENSION = "extension.extension";

	private static final String EXTENSIONPOINT_EXTENSIONPOINT = "extensionpoint.extensionpoint";

	private static final String SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME = Messages.ExtensionDefinitionTemplateTargetLocationPage_SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME;

	private static final String TARGET_LOCATION = Messages.ExtensionDefinitionTemplateTargetLocationPage_TARGET_LOCATION;

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.sc.wizard.ExtensionDefinitionTemplateTargetLocationPage"; //$NON-NLS-1$

	private ExtensionDefinitionTemplateModel model;

	protected ExtensionDefinitionTemplateTargetLocationPage(ExtensionDefinitionTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(TARGET_LOCATION);
		setDescription(SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME);
	}

	@Override
	protected void checkPageStatus() {
		if (getModel().getTargetLocation() == null || "".equals(getModel().getTargetLocation())) { //$NON-NLS-1$
			setPageComplete(false);
			return;
		}
		if (getModel().getFileName() == null || "".equals(getModel().getFileName())) { //$NON-NLS-1$
			setPageComplete(false);
			return;
		}
		IValidationStatus status = model.validateLocation();
		if (status.hasErrors()) {
			setErrorMessage(status.getMessage());
			setPageComplete(false);
		} else if (status.hasWarnings()) {
			setErrorMessage(status.getMessage());
			setPageComplete(true);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

	@Override
	protected String getDefaultFileName() {
		String templateLocation = model.getTemplateLocation();
		String defaultName = EXTENSION_EXTENSION;
		if (templateLocation.equals(ExtensionDefinitionTemplateLocations.EXTENSION_POINT)) {
			defaultName = EXTENSIONPOINT_EXTENSIONPOINT;
		} else if (templateLocation.equals(ExtensionDefinitionTemplateLocations.EXTENSION)) {
			defaultName = EXTENSION_EXTENSION;
		}
		return defaultName;
	}

	@Override
	protected boolean isForcedFileName() {
		return true;
	}

	@Override
	protected String getArtifactContainerName() {
		return ICommonConstants.ARTIFACT_TYPE.EXTENSION_DEFINITIONS;
	}

}
