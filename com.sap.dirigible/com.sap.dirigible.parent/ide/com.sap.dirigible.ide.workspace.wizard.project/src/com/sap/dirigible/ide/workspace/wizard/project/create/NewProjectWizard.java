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

package com.sap.dirigible.ide.workspace.wizard.project.create;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.logging.Logger;

public class NewProjectWizard extends Wizard {

	private static final String COULD_NOT_CREATE_PROJECT = Messages.NewProjectWizard_COULD_NOT_CREATE_PROJECT;

	private static final String OPERATION_FAILED = Messages.NewProjectWizard_OPERATION_FAILED;

	private static final String PROJECT_S_CREATED_SUCCESSFULLY = Messages.NewProjectWizard_PROJECT_S_CREATED_SUCCESSFULLY;

	private static final String WINDOW_TITLE = Messages.NewProjectWizard_WINDOW_TITLE;

	private final NewProjectWizardModel model;

	private final NewProjectWizardMainPage mainPage;

	private final NewProjectWizardTemplateTypePage templatesPage;

	private static final Logger logger = Logger
			.getLogger(NewProjectWizard.class);

	public NewProjectWizard() {
		setWindowTitle(WINDOW_TITLE);

		model = new NewProjectWizardModel();
		mainPage = new NewProjectWizardMainPage(model);
		templatesPage = new NewProjectWizardTemplateTypePage(model);
	}

	@Override
	public void addPages() {
		addPage(mainPage);
		addPage(templatesPage);
	}

	public boolean performFinish() {
		logger.info(String.format(PROJECT_S_CREATED_SUCCESSFULLY,
				mainPage.getProjectName()));
		boolean result = this.onFinish();
		if (result) {
			StatusLineManagerUtil.setInfoMessage(String.format(
					StatusLineManagerUtil.ARTIFACT_HAS_BEEN_CREATED,
					mainPage.getProjectName()));
		}
		return result;
	}

	public void showErrorDialog(String title, String message) {
		logger.error(message);
		MessageDialog.openError(null, title, message);
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

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (model.isUseTemplate()) {
			return super.getNextPage(page);
		} else {
			return null;
		}
	}

}
