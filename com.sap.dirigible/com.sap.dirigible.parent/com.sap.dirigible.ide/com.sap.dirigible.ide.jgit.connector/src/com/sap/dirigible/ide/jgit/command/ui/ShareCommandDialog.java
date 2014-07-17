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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ShareCommandDialog extends PushCommandDialog {
	private static final long serialVersionUID = -5124345102495879231L;

	private static final String SHARE_TO_REMOTE_GIT_REPOSITORY = Messages.ShareCommandDialog_SHARE_TO_REMOTE_GIT_REPOSITORY;
	private static final String REPOSITORY_URI = Messages.ShareCommandDialog_REPOSITORY_URI;

	private Text textGitRepositoryURI;

	private String gitRepositoryURI;

	public ShareCommandDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle(SHARE_TO_REMOTE_GIT_REPOSITORY);
	}

	@Override
	protected void addControls(Composite container) {
		createGitRepositroyField(container);
		super.addControls(container);
	}

	private void createGitRepositroyField(Composite container) {
		Label labelCommitMessage = new Label(container, SWT.NONE);
		labelCommitMessage.setText(REPOSITORY_URI);

		GridData dataGitRepositoryURI = new GridData();
		dataGitRepositoryURI.grabExcessHorizontalSpace = true;
		dataGitRepositoryURI.horizontalAlignment = GridData.FILL;

		textGitRepositoryURI = new Text(container, SWT.BORDER);
		textGitRepositoryURI.setLayoutData(dataGitRepositoryURI);
	}

	@Override
	protected void saveInput() {
		super.saveInput();
		gitRepositoryURI = textGitRepositoryURI.getText();
	}

	public String getGitRepositoryURI() {
		return gitRepositoryURI;
	}
}