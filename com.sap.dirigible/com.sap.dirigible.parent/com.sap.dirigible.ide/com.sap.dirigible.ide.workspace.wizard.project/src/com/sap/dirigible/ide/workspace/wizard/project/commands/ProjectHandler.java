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

package com.sap.dirigible.ide.workspace.wizard.project.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;

import com.sap.dirigible.ide.workspace.wizard.project.create.NewProjectWizard;

import com.sap.dirigible.ide.workspace.ui.commands.AbstractWorkspaceHandler;

public class ProjectHandler extends AbstractWorkspaceHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Wizard wizard = new NewProjectWizard();
		WizardDialog dialog = new WizardDialog(null, wizard);
		dialog.setMinimumPageSize(500, 700);
		dialog.open();

		refreshWorkspace();

		return null;
	}

}
