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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.ui.shared.ValidationStatus;

public class RenameWizardNamingPage extends WizardPage {

	private static final long serialVersionUID = -1496466639907638752L;

	private static final String TRYING_TO_SET_FILENAME_TO_A_DISPOSED_OR = 
			Messages.RenameWizardNamingPage_TRYING_TO_SET_FILENAME_TO_A_DISPOSED_OR;

	private static final String FILENAME_CANNOT_BE_NULL = Messages.RenameWizardNamingPage_FILENAME_CANNOT_BE_NULL;

	private static final String ENTER_NEW_NAME = Messages.RenameWizardNamingPage_ENTER_NEW_NAME;

	private static final Logger log = Logger
			.getLogger(RenameWizardNamingPage.class);

	private static final String RENAME_WIZARD_NAMING_PAGE_TITLE = Messages.RenameWizardNamingPage_RENAME_WIZARD_NAMING_PAGE_TITLE;

	private static final String RENAME_WIZARD_NAMING_PAGE_DESCRIPTION = Messages.RenameWizardNamingPage_RENAME_WIZARD_NAMING_PAGE_DESCRIPTION;

	private final RenameWizardModel model;

	private Text nameText = null;

	private String updatedName = null;

	public RenameWizardNamingPage(RenameWizardModel model) {
		super(RENAME_WIZARD_NAMING_PAGE_TITLE);
		setTitle(RENAME_WIZARD_NAMING_PAGE_TITLE);
		setDescription(RENAME_WIZARD_NAMING_PAGE_DESCRIPTION);
		this.model = model;
	}

	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout());

		createNameField(composite);

		initialize();
	}

	private void createNameField(Composite parent) {
		final Label nameLabel = new Label(parent, SWT.NONE);
		nameLabel.setText(ENTER_NEW_NAME);
		nameLabel
				.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));

		nameText = new Text(parent, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		nameText.addModifyListener(new ModifyListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3269793522450746060L;

			public void modifyText(ModifyEvent event) {
				onResourceNameChanged(nameText.getText());
			}
		});
	}

	public void setResourceName(String resourceName) {
		if (resourceName == null) {
			throw new IllegalArgumentException(FILENAME_CANNOT_BE_NULL);
		}
		if (nameText == null || nameText.isDisposed()) {
			log.warn(TRYING_TO_SET_FILENAME_TO_A_DISPOSED_OR);
			return;
		}
		if (!nameText.getText().equals(resourceName)) {
			nameText.setText(resourceName);
		}
	}

	public void setCanFinish(boolean value) {
		if (value && nameText != null) {
			updatedName = nameText.getText();
		}
		setPageComplete(value);
	}

	public String getText() {
		return updatedName;
	}

	public void initialize() {
		this.setResourceName(model.getResourceName());
		validate();
	}

	public void onResourceNameChanged(String resourceName) {
		model.setResourceName(resourceName);
		validate();
	}

	private void validate() {
		final ValidationStatus status = model.validate();
		if (status.hasErrors()) {
			this.setErrorMessage(status.getMessage());
			this.setCanFinish(false);
		} else {
			this.setErrorMessage(null);
			this.setCanFinish(true);
		}
	}

}
