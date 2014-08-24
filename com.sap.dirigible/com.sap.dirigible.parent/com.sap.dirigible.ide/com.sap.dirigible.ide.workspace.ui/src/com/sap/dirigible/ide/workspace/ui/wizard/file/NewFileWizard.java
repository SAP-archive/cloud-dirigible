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

package com.sap.dirigible.ide.workspace.ui.wizard.file;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.logging.Logger;

public class NewFileWizard extends Wizard {

	private static final String COULD_NOT_SAVE_FILE = Messages.NewFileWizard_COULD_NOT_SAVE_FILE;

	private static final String OPERATION_FAILED = Messages.NewFileWizard_OPERATION_FAILED;

	private static final String FILE_S_CREATED_SUCCESSFULLY = Messages.NewFileWizard_FILE_S_CREATED_SUCCESSFULLY;

	private static final Logger logger = Logger.getLogger(NewFileWizard.class);

	private static final String WINDOW_TITLE = Messages.NewFileWizard_WINDOW_TITLE;

	private final NewFileWizardMainPage mainPage;

	private final NewFileWizardModel model;

	public NewFileWizard() {
		this(null);
	}

	public NewFileWizard(IContainer selection) {
		setWindowTitle(WINDOW_TITLE);

		model = new NewFileWizardModel();
		if (selection != null) {
			model.setParentLocation(selection.getFullPath().toString());
		}

		mainPage = new NewFileWizardMainPage(model);
	}

	@Override
	public void addPages() {
		addPage(mainPage);
	}

	public boolean performFinish() {
		logger.info(String.format(FILE_S_CREATED_SUCCESSFULLY,
				model.getFileName()));
		boolean result = onFinish();
		if (result) {
			StatusLineManagerUtil.setInfoMessage(String.format(
					StatusLineManagerUtil.ARTIFACT_HAS_BEEN_CREATED,
					model.getFileName()));
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
			logger.error(e.getMessage(), e);
			showErrorDialog(OPERATION_FAILED, COULD_NOT_SAVE_FILE);
			return false;
		}
	}

}
