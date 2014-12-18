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

package com.sap.dirigible.ide.workspace.wizard.project.export;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class DownloadDialog extends Dialog {

	private static final long serialVersionUID = -380351602370874305L;

	Browser b;
	String url;

	public DownloadDialog(Shell parent) {
		super(parent);
	}

	public void setURL(String url) {
		if (b != null && !b.isDisposed()) {
			b.setUrl(url);
		}
		this.url = url;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		b = new Browser(parent, SWT.NONE);
		if (url != null) {
			b.setUrl(url);
		}
		return control;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}

	@Override
	protected int getShellStyle() {
		return SWT.NO_TRIM;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(1, 1);
		newShell.setMinimized(true);
	}
}
