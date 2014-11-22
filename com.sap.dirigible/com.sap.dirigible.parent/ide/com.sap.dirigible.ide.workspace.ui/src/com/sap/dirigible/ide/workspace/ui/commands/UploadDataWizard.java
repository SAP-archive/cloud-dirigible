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

package com.sap.dirigible.ide.workspace.ui.commands;

import org.eclipse.jface.wizard.Wizard;

/**
 * Wizard via which the user inserts data into DB tables, providing a list of
 * .dsv files to import from
 * 
 */
public class UploadDataWizard extends Wizard {

	private static final String WINDOW_TITLE = Messages.UploadDataWizard_WINDOW_TITLE;

	private UploadDataWizardPage uploadDataWizardPage;

	private UploadDataHandler uploadDataHandler;

	public UploadDataWizard(UploadDataHandler uploadDataHandler) {
		setWindowTitle(WINDOW_TITLE);
		this.uploadDataHandler = uploadDataHandler;
		uploadDataWizardPage = new UploadDataWizardPage();
	}

	@Override
	public boolean performFinish() {
		uploadDataHandler
				.insertIntoDbAsync(uploadDataWizardPage.getFilePaths());
		return true;
	}

	@Override
	public void addPages() {
		addPage(uploadDataWizardPage);
	}

}
