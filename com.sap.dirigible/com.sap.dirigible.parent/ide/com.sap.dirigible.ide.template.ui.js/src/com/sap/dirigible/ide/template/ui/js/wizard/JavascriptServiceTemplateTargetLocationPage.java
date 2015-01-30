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

import com.sap.dirigible.ide.common.CommonUtils;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateTargetLocationPage;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;
import com.sap.dirigible.repository.api.ICommonConstants;

public class JavascriptServiceTemplateTargetLocationPage extends
		TemplateTargetLocationPage {

	private static final long serialVersionUID = 6376197935502791442L;

	private static final String SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME = Messages.JavascriptServiceTemplateTargetLocationPage_SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME;

	private static final String TARGET_LOCATION = Messages.JavascriptServiceTemplateTargetLocationPage_TARGET_LOCATION;

	private static final String PAGE_NAME = "com.sap.dirigible.ide.template.ui.js.wizard.JavascriptServiceTemplateTargetLocationPage"; //$NON-NLS-1$

	private JavascriptServiceTemplateModel model;

	protected JavascriptServiceTemplateTargetLocationPage(
			JavascriptServiceTemplateModel model) {
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
	protected String getDefaultFileName(String preset) {
		String jsOrLibName = ("/com/sap/dirigible/ide/template/ui/js/templates/guid-generator.js" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) ? "library" //$NON-NLS-1$
				: "service"; //$NON-NLS-1$
//		String jsOrLibExt = ("/com/sap/dirigible/ide/template/ui/js/templates/guid-generator.jslib" //$NON-NLS-1$
//				.equals(model.getTemplate().getLocation())) ? "jslib" : "js"; //$NON-NLS-1$ //$NON-NLS-2$
		String jsOrLibExt = "js";
		if ("/com/sap/dirigible/ide/template/ui/js/templates/ruby-service.rb" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {
			jsOrLibExt = "rb"; //$NON-NLS-1$
		}
		else if("/com/sap/dirigible/ide/template/ui/js/templates/groovy-service.groovy" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {
			jsOrLibExt = "groovy"; //$NON-NLS-1$
		}
		else if("/com/sap/dirigible/ide/template/ui/js/templates/terminal-command.command" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {
			jsOrLibExt = "command"; //$NON-NLS-1$
		}
		else if("/com/sap/dirigible/ide/template/ui/js/templates/java-service.java_" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {
			jsOrLibExt = "java"; //$NON-NLS-1$
		}
		return (preset == null) ? jsOrLibName + "_name." + jsOrLibExt : CommonUtils.getFileNameNoExtension(preset) + "." + jsOrLibExt; //$NON-NLS-1$
		
	}

	@Override
	protected boolean isForcedFileName() {
		return true;
	}

	@Override
	protected String getArtifactContainerName() {
		return ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	}
}
