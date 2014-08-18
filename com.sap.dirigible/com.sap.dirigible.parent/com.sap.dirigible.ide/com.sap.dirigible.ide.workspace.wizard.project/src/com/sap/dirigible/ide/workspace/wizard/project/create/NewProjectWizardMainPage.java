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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.sap.dirigible.ide.workspace.ui.shared.FocusableWizardPage;
import com.sap.dirigible.ide.workspace.ui.shared.IValidationStatus;

public class NewProjectWizardMainPage extends FocusableWizardPage {

	private static final long serialVersionUID = -2191030355904715681L;
	private static final String ENTER_PROJECT_NAME = Messages.NewProjectWizardMainPage_ENTER_PROJECT_NAME;
	private static final String PAGE_NAME = "Main Page"; //$NON-NLS-1$
	private static final String PAGE_TITLE = Messages.NewProjectWizardMainPage_PAGE_TITLE;
	private static final String PAGE_DESCRIPTION = Messages.NewProjectWizardMainPage_PAGE_DESCRIPTION;

	private final NewProjectWizardModel model;

	private Text projectNameField = null;
	
	public NewProjectWizardMainPage(NewProjectWizardModel model) {
		super(PAGE_NAME);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
		this.model = model;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout());

		Label projectNameLabel = new Label(composite, SWT.NONE);
		projectNameLabel.setText(ENTER_PROJECT_NAME);
		projectNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM,
				false, false));

		projectNameField = new Text(composite, SWT.BORDER);
		projectNameField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		projectNameField.addModifyListener(new ModifyListener() {
			private static final long serialVersionUID = -3384299735086998756L;

			public void modifyText(ModifyEvent event) {
				onProjectNameChanged(projectNameField.getText());
			}
		});
		setFocusable(projectNameField);
		
		this.initialize();
	}

	public String getProjectName() {
		return projectNameField.getText();
	}

	public void setProjectName(String location) {
		if (projectNameField == null || projectNameField.isDisposed()) {
			return;
		}
		if (!areEqual(projectNameField.getText(), location)) {
			projectNameField.setText(location);
		}
	}

	private static boolean areEqual(Object a, Object b) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null) {
			return a.equals(b);
		} else {
			return b.equals(a);
		}
	}

	public void setWarningMessage(String message) {
		setMessage(message, WARNING);
	}

	public void setCanFinish(boolean value) {
		setPageComplete(value);
	}

	@Override
	public void dispose() {
		projectNameField = null;
		super.dispose();
	}

	public void initialize() {
		this.setProjectName(this.model.getProjectName());
		revalidateModel();
	}

	public void onProjectNameChanged(String location) {
		this.model.setProjectName(location);
		revalidateModel();
	}

	private void revalidateModel() {
		IValidationStatus status = model.validate();
		if (status.hasErrors()) {
			this.setErrorMessage(status.getMessage());
			this.setWarningMessage(null);
			this.setCanFinish(false);
		} else if (status.hasWarnings()) {
			this.setErrorMessage(null);
			this.setWarningMessage(status.getMessage());
			this.setCanFinish(true);
		} else {
			this.setErrorMessage(null);
			this.setWarningMessage(null);
			this.setCanFinish(true);
		}
	}

}
