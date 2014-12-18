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

import org.eclipse.jgit.util.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ShareCommandDialog extends PushCommandDialog {
	private static final long serialVersionUID = -5124345102495879231L;

	private static final String SHARE_TO_REMOTE_GIT_REPOSITORY = Messages.ShareCommandDialog_SHARE_TO_REMOTE_GIT_REPOSITORY;
	private static final String REPOSITORY_URI = Messages.CommandDialog_REPOSITORY_URI;
	private static final String REPOSITORY_URI_IS_EMPTY = Messages.ShareCommandDialog_REPOSITORY_URI_IS_EMPTY;

	private Text textRepositoryURI;

	private String repositoryURI;

	public ShareCommandDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle(SHARE_TO_REMOTE_GIT_REPOSITORY);
	}

	@Override
	protected void addWidgets(Composite container) {
		createRepositoryURIField(container);
		super.addWidgets(container);
	}

	private void createRepositoryURIField(Composite container) {
		Label labelRepositoryURI = new Label(container, SWT.NONE);
		labelRepositoryURI.setText(REPOSITORY_URI);

		GridData dataGitRepositoryURI = new GridData();
		dataGitRepositoryURI.grabExcessHorizontalSpace = true;
		dataGitRepositoryURI.horizontalAlignment = GridData.FILL;

		textRepositoryURI = new Text(container, SWT.BORDER);
		textRepositoryURI.setLayoutData(dataGitRepositoryURI);
	}

	@Override
	protected boolean validateInput() {
		boolean valid = false;
		if (super.validateInput()) {
			if (StringUtils.isEmptyOrNull(textRepositoryURI.getText())) {
				errorMessage = REPOSITORY_URI_IS_EMPTY;
			} else {
				valid = true;
			}
		}
		return valid;
	}

	@Override
	protected void saveInput() {
		super.saveInput();
		repositoryURI = textRepositoryURI.getText();
	}

	public String getRepositoryURI() {
		return repositoryURI;
	}
}