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

package com.sap.dirigible.ide.jgit.command.ui;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PushCommandDialog extends TitleAreaDialog {
	private static final long serialVersionUID = -5124345102495879231L;

	private static final String PUSH_CHANGES_TO_REMOTE_GIT_REPOSITORY = Messages.PushCommandDialog_PUSH_CHANGES_TO_REMOTE_GIT_REPOSITORY;
	private static final String COMMIT_MESSAGE = Messages.PushCommandDialog_COMMIT_MESSAGE;
	private static final String USERNAME = Messages.PushCommandDialog_USERNAME;
	private static final String EMAIL = Messages.PushCommandDialog_EMAIL;
	private static final String PASSWORD = Messages.PushCommandDialog_PASSWORD;

	private Text textCommitMessage;
	private Text textUsername;
	private Text textEmail;
	private Text textPassword;

	private String commitMessage;
	private String username;
	private String email;
	private String password;

	public PushCommandDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle(PUSH_CHANGES_TO_REMOTE_GIT_REPOSITORY);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);
		addControls(container);
		return area;
	}

	protected void addControls(Composite container) {
		createCommitMessageField(container);
		createUsernameField(container);
		createEmailField(container);
		createPasswordField(container);
	}

	private void createCommitMessageField(Composite container) {
		Label labelCommitMessage = new Label(container, SWT.NONE);
		labelCommitMessage.setText(COMMIT_MESSAGE);

		GridData dataCommitMessage = new GridData();
		dataCommitMessage.grabExcessHorizontalSpace = true;
		dataCommitMessage.horizontalAlignment = GridData.FILL;

		textCommitMessage = new Text(container, SWT.BORDER);
		textCommitMessage.setLayoutData(dataCommitMessage);
	}

	private void createUsernameField(Composite container) {
		Label labelUsername = new Label(container, SWT.NONE);
		labelUsername.setText(USERNAME);

		GridData dataUsername = new GridData();
		dataUsername.grabExcessHorizontalSpace = true;
		dataUsername.horizontalAlignment = GridData.FILL;

		textUsername = new Text(container, SWT.BORDER);
		textUsername.setLayoutData(dataUsername);
	}

	private void createEmailField(Composite container) {
		Label labelEmail = new Label(container, SWT.NONE);
		labelEmail.setText(EMAIL);

		GridData dataEmail = new GridData();
		dataEmail.grabExcessHorizontalSpace = true;
		dataEmail.horizontalAlignment = GridData.FILL;

		textEmail = new Text(container, SWT.BORDER);
		textEmail.setLayoutData(dataEmail);
	}

	private void createPasswordField(Composite container) {
		Label labelPassword = new Label(container, SWT.NONE);
		labelPassword.setText(PASSWORD);

		GridData dataPassword = new GridData();
		dataPassword.grabExcessHorizontalSpace = true;
		dataPassword.horizontalAlignment = GridData.FILL;

		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(dataPassword);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	protected void saveInput() {
		commitMessage = textCommitMessage.getText();
		username = textUsername.getText();
		email = textEmail.getText();
		password = textPassword.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getCommitMessage() {
		return commitMessage;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}