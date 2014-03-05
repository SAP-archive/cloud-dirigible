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

package com.sap.dirigible.ide.ui.rap.entry;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class DirigibleWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String INITIAL_PERSPECTIVE_ID = "workspace"; //$NON-NLS-1$

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}

	public String getInitialWindowPerspectiveId() {
		String activeWorkspace = RWT.getRequest().getParameter("perspective"); //$NON-NLS-1$
		if (activeWorkspace != null) {
			return activeWorkspace;
		}
		return INITIAL_PERSPECTIVE_ID;
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer windowConfigurer) {
		return new DirigibleWorkbenchWindowAdvisor(windowConfigurer);
	}
}
