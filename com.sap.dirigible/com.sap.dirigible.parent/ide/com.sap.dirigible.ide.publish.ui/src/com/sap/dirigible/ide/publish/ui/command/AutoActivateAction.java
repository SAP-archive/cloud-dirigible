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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.publish.PublishManager;
import com.sap.dirigible.ide.workspace.dual.WorkspaceLocator;
import com.sap.dirigible.ide.workspace.ui.view.WebViewerView;

public class AutoActivateAction implements IWorkbenchWindowActionDelegate, IResourceChangeListener {

	private static final String FAILED_TO_ACTIVATE_PROJECT = Messages.AutoActivateAction_FAILED_TO_ACTIVATE_PROJECT;
	private static final String FAILED_TO_ACTIVATE_FILE = Messages.AutoActivateAction_FAILED_TO_ACTIVATE_FILE;
	private static final String AUTO_ACTIVATION_FAILED = Messages.AutoActivateAction_AUTO_ACTIVATION_FAILED;
	private static boolean AUTO_ACTIVATION = true;

	@Override
	public void run(IAction action) {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
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

	@Override
	public void init(IWorkbenchWindow window) {
		WorkspaceLocator.getWorkspace().addResourceChangeListener(this);
	}

	public static boolean isAutoActivateStrategy() {
		return AUTO_ACTIVATION;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		if (!AUTO_ACTIVATION) {
			return;
		}
		IResource delta = arg0.getResource();
		if (delta != null) {
			if (delta instanceof IFile) {
				activateFile((IFile) delta);
			} else {
				activate(delta.getProject());
			}
		}

	}

	private void activate(IProject project) {
		try {
			PublishManager.activateProject(project);
		} catch (PublishException e) {
			MessageDialog.openError(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					AUTO_ACTIVATION_FAILED, FAILED_TO_ACTIVATE_PROJECT + project.getName());
		}
		WebViewerView.refreshWebViewerViewIfVisible();
	}

	private void activateFile(IFile file) {
		try {
			final List<IPublisher> publishers = PublishManager.getPublishers();

			for (Iterator<IPublisher> iterator = publishers.iterator(); iterator.hasNext();) {
				IPublisher publisher = (IPublisher) iterator.next();
				if (publisher.isAutoActivationAllowed()) {
					publisher.activateFile(file);
				}
			}
		} catch (PublishException e) {
			MessageDialog.openError(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					AUTO_ACTIVATION_FAILED, FAILED_TO_ACTIVATE_FILE + file.getName());
		}
		WebViewerView.refreshWebViewerViewIfVisible();
	}

}
