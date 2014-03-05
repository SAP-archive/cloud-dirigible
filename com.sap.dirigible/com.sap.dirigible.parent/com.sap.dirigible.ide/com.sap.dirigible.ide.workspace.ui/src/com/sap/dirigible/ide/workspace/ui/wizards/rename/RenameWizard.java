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

package com.sap.dirigible.ide.workspace.ui.wizards.rename;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import com.sap.dirigible.ide.logging.Logger;

public class RenameWizard extends Wizard {

	private static final String CHECK_LOGS_FOR_MORE_INFO = Messages.RenameWizard_CHECK_LOGS_FOR_MORE_INFO;

	private static final String COULD_NOT_COMPLETE_WIZARD_DUE_TO_THE_FOLLOWING_ERROR = Messages.RenameWizard_COULD_NOT_COMPLETE_WIZARD_DUE_TO_THE_FOLLOWING_ERROR;

	private static final String OPERATION_ERROR = Messages.RenameWizard_OPERATION_ERROR;

	private static final String COULD_NOT_COMPLETE_RESOURCE_RENAME = Messages.RenameWizard_COULD_NOT_COMPLETE_RESOURCE_RENAME;

	private static final Logger logger = Logger.getLogger(RenameWizard.class);

	private static final String RENAME_WIZARD_TITLE = Messages.RenameWizard_RENAME_WIZARD_TITLE;

	private final RenameWizardModel model;

	private final RenameWizardNamingPage namingPage;

	public RenameWizard(IResource resource) {
		setWindowTitle(RENAME_WIZARD_TITLE);
		model = new RenameWizardModel(resource);
		namingPage = new RenameWizardNamingPage(model);
	}

	@Override
	public void addPages() {
		addPage(namingPage);
	}

	public boolean performFinish() {
		return onFinish();
	}

	public void showErrorDialog(String title, String message) {
		logger.error(message);
		MessageDialog.openError(null, title, message);
	}

	public String getText() {
		if (namingPage != null) {
			return namingPage.getText();
		}
		return null;
	}

	public boolean onFinish() {
		try {
			model.persist();
			return true;
		} catch (IOException ex) {
			logger.error(COULD_NOT_COMPLETE_RESOURCE_RENAME, ex);
			this.showErrorDialog(
					OPERATION_ERROR,
					COULD_NOT_COMPLETE_WIZARD_DUE_TO_THE_FOLLOWING_ERROR
							+ ex.getMessage() + ". " + CHECK_LOGS_FOR_MORE_INFO); //$NON-NLS-1$
			return false;
		}
	}
}
