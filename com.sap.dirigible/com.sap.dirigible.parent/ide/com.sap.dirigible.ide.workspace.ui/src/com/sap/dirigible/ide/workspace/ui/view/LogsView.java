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

package com.sap.dirigible.ide.workspace.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.ui.widget.extbrowser.ExtendedBrowser;

public class LogsView extends ViewPart {
	private static final String LOGGING_LOCATION = CommonParameters.getRuntimeUrl() + "/logging";

	private ExtendedBrowser browser = null;

	public LogsView() {
		super();
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		browser = new ExtendedBrowser(parent, SWT.NONE);
		browser.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browser.setUrl(LOGGING_LOCATION);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	@Override
	public void dispose() {
		browser = null;
		super.dispose();
	}
}
