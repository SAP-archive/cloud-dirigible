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

package com.sap.dirigible.ide.workspace.wizard.project.sample;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.logging.Logger;


public class SampleProjectWizard extends Wizard {

	private static final String SampleProjectWizard_WINDOW_TITLE = Messages.SampleProjectWizard_WINDOW_TITLE;
	private static final String COULD_NOT_CREATE_PROJECT = Messages.SampleProjectWizard_COULD_NOT_CREATE_PROJECT;
	private static final String OPERATION_FAILED = Messages.SampleProjectWizard_OPERATION_FAILED;
	private static final String PROJECT_S_CREATED_SUCCESSFULLY = Messages.SampleProjectWizard_PROJECT_S_CREATED_SUCCESSFULLY;
	private final SampleProjectWizardModel model;
	private final SampleProjectWizardGitTemplatePage samplesPage;
	
	private static final Logger logger = Logger
			.getLogger(SampleProjectWizard.class);
	
	public SampleProjectWizard() {
		setWindowTitle(SampleProjectWizard_WINDOW_TITLE);

		model = new SampleProjectWizardModel();
		samplesPage = new SampleProjectWizardGitTemplatePage(model);
	}

	@Override
	public void addPages() {
		addPage(samplesPage);
	}
	
	@Override
	public boolean performFinish() {
		String projectName = model.getTemplate().getName();
		logger.info(String.format(PROJECT_S_CREATED_SUCCESSFULLY,
				projectName));
		boolean result = this.onFinish();
		if (result) {
			StatusLineManagerUtil.setInfoMessage(String.format(
					StatusLineManagerUtil.ARTIFACT_HAS_BEEN_CREATED,
					projectName));
		}
		return result;
	}
	
	public boolean onFinish() {
		try {
			model.execute();
			return true;
		} catch (CoreException ex) {
			logger.error(ex.getMessage(), ex);
			this.showErrorDialog(OPERATION_FAILED, String.format(COULD_NOT_CREATE_PROJECT, ex.getMessage()));
			return false;
		}
	}
	
	public void showErrorDialog(String title, String message) {
		logger.error(message);
		MessageDialog.openError(null, title, message);
	}
}
