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

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.workspace.wizard.project.export.DownloadDialog;
import com.sap.dirigible.ide.workspace.wizard.project.export.DownloadProjectServiceHandler;
import com.sap.dirigible.ide.workspace.wizard.project.export.RepositoryDataStore;

public class DownloadProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);

		if (selection.size() == 1) {
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IProject) {
				IProject project = (IProject) firstElement;
				DownloadDialog downloadDialog = new DownloadDialog(
						HandlerUtil.getActiveShell(event));
				downloadDialog.setURL(DownloadProjectServiceHandler
						.getUrl(project.getName()));
				downloadDialog.open();
			}
		} else if (selection.size() > 1) {
			StringBuffer projectNames = new StringBuffer();
			Iterator<?> iterator = selection.iterator();
			boolean dot = false;
			while (iterator.hasNext()) {
				Object element = iterator.next();
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					if (dot) {
						projectNames
								.append(RepositoryDataStore.PROJECT_NAME_SEPARATOR);
					}
					projectNames.append(project.getName());
					dot = true;
				}
			}
			DownloadDialog downloadDialog = new DownloadDialog(
					HandlerUtil.getActiveShell(event));
			downloadDialog.setURL(DownloadProjectServiceHandler
					.getUrl(projectNames.toString()));
			downloadDialog.open();
		}
		return null;
	}

}
