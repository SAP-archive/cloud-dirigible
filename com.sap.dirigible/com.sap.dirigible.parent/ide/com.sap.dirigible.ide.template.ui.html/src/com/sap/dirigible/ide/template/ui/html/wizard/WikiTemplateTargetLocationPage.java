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
import com.sap.dirigible.ide.template.ui.common.TemplateTargetLocationPage;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;
import com.sap.dirigible.repository.api.ICommonConstants;

public class WikiTemplateTargetLocationPage extends TemplateTargetLocationPage {

	private static final long serialVersionUID = 11784306852854208L;

	private static final String SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME = Messages.HtmlTemplateTargetLocationPage_SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME;

	private static final String TARGET_LOCATION = Messages.HtmlTemplateTargetLocationPage_TARGET_LOCATION;

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.html.wizard.WikiTemplateTargetLocationPage"; //$NON-NLS-1$

	private WikiTemplateModel model;

	protected WikiTemplateTargetLocationPage(WikiTemplateModel model) {
		super(PAGE_NAME);
		this.model = model;
		setTitle(TARGET_LOCATION);
		setDescription(SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME);
	}

	@Override
	protected void checkPageStatus() {
		if (getModel().getTargetLocation() == null
				|| "".equals(getModel().getTargetLocation())) { //$NON-NLS-1$
			setPageComplete(false);
			return;
		}
		if (getModel().getFileName() == null
				|| "".equals(getModel().getFileName())) { //$NON-NLS-1$
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
		return "wiki_page.wiki"; //$NON-NLS-1$
	}

	@Override
	protected String getArtifactContainerName() {
		return ICommonConstants.ARTIFACT_TYPE.WIKI_CONTENT;
	}

}
