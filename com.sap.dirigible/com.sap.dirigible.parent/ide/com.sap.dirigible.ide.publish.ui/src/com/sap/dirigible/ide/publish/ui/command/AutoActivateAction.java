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

package com.sap.dirigible.ide.publish.ui.command;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.sap.dirigible.ide.common.CommonParameters;

public class AutoActivateAction implements IWorkbenchWindowActionDelegate {

	private static boolean AUTO_ACTIVATION = true;

	@Override
	public void run(IAction action) {
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		AUTO_ACTIVATION = action.isChecked();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	private static String AUTO_ACTIVATOR = "auto-activator"; 

	@Override
	public void init(IWorkbenchWindow window) {
//		WorkspaceLocator.getWorkspace().addResourceChangeListener(this);
		AutoActivator autoActivator = (AutoActivator) CommonParameters.getObject(AUTO_ACTIVATOR);
		if (autoActivator == null) {
			autoActivator = new AutoActivator();
			autoActivator.registerListener();
			CommonParameters.setObject(AUTO_ACTIVATOR, autoActivator);
		}
	}

	public static boolean isAutoActivateStrategy() {
		return AUTO_ACTIVATION;
	}

}
