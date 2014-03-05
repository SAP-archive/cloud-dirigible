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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.sap.dirigible.ide.common.CommonParameters;

public class DirigibleWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final String WORKBENCH = Messages.DirigibleWorkbenchWindowAdvisor_WORKBENCH;

	public DirigibleWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new DirigibleActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowPerspectiveBar(false);
		configurer
				.setTitle(CommonParameters.DIRIGIBLE_PRODUCT_NAME + WORKBENCH); // TODO:
																				// I18N
		configurer.setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE);
		configurer.setShowProgressIndicator(true);
		configurer.setShowStatusLine(true);
	}

	public void postWindowOpen() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		Shell shell = window.getShell();
		Rectangle shellBounds = shell.getBounds();
		if (!shell.getMaximized() && shellBounds.x == 0 && shellBounds.y == 0) {
			shell.setLocation(70, 25);
		}
	}

}
