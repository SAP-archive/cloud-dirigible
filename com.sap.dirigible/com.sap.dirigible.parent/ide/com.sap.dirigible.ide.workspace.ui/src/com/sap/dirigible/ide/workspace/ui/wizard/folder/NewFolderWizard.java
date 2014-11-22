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

package com.sap.dirigible.ide.workspace.ui.wizard.folder;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.logging.Logger;

public class NewFolderWizard extends Wizard {

	private static final String COULD_NOT_CREATE_FOLDER = Messages.NewFolderWizard_COULD_NOT_CREATE_FOLDER;

	private static final String OPERATION_FAILED = Messages.NewFolderWizard_OPERATION_FAILED;

	private static final String FOLDER_S_CREATED_SUCCESSFULLY = Messages.NewFolderWizard_FOLDER_S_CREATED_SUCCESSFULLY;

	private static final Logger logger = Logger
			.getLogger(NewFolderWizard.class);

	private static final String WINDOW_TITLE = Messages.NewFolderWizard_WINDOW_TITLE;

	private final NewFolderWizardMainPage mainPage;

	private final NewFolderWizardModel model;

	public NewFolderWizard() {
		this(null);
	}

	public NewFolderWizard(IContainer selection) {
		setWindowTitle(WINDOW_TITLE);

		model = new NewFolderWizardModel();
		if (selection != null) {
			model.setParentLocation(selection.getFullPath().toString());
		}
		mainPage = new NewFolderWizardMainPage(model);
	}

	@Override
	public void addPages() {
		addPage(mainPage);
	}

	public boolean performFinish() {
		logger.info(String.format(FOLDER_S_CREATED_SUCCESSFULLY,
				model.getFolderName()));
		boolean result = this.onFinish();
		if (result) {
			StatusLineManagerUtil.setInfoMessage(String.format(
					StatusLineManagerUtil.ARTIFACT_HAS_BEEN_CREATED,
					model.getFolderName()));
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
		} catch (CoreException e) {
			this.showErrorDialog(OPERATION_FAILED, COULD_NOT_CREATE_FOLDER);
			return false;
		}
	}

}
