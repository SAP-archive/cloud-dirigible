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

package com.sap.dirigible.ide.workspace.ui.commands;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sap.dirigible.ide.workspace.ui.view.WorkspaceExplorerView;
import com.sap.dirigible.repository.api.ICollection;

public abstract class AbstractWorkspaceHandler extends AbstractHandler {

	private static final ISelectionListener selectionListener = new SelectionListenerImpl();
	private static boolean hasSelectionListener = false;
	private static Object lastSelectedElement;

	public Object getLastSelectedWorkspaceElement() {
		validateLastSelectedElement();
		return lastSelectedElement;
	}

	private void validateLastSelectedElement() {
		try {
			boolean validCollection = (lastSelectedElement instanceof ICollection)
					&& (((ICollection) lastSelectedElement).exists());
			boolean validResource = (lastSelectedElement instanceof IResource)
					&& (((IResource) lastSelectedElement).exists());
			if (!validCollection && !validResource) {
				lastSelectedElement = null;
			}
		} catch (IOException e) {
			lastSelectedElement = null;
		}

	}

	public static void attachSelectionListener(IWorkbenchPartSite site) {
		if (site != null) {
			final ISelectionService selectionService = getSelectionService(site);
			if (selectionService != null && !hasSelectionListener) {
				selectionService.addSelectionListener(selectionListener);
				hasSelectionListener = true;
			}
		}
	}

	public static void detachSelectionListener(IWorkbenchPartSite site) {
		if (site != null) {
			final ISelectionService selectionService = getSelectionService(site);
			if (selectionService != null) {
				selectionService.removeSelectionListener(selectionListener);
				hasSelectionListener = false;
				lastSelectedElement = null;
			}
		}
	}

	protected void refreshWorkspace() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IWorkbenchPart part = page.getActivePart();
		if (part instanceof WorkspaceExplorerView) {
			((WorkspaceExplorerView) part).refresh();
		}
	}

	private static ISelectionService getSelectionService(IWorkbenchPartSite site) {
		final IWorkbenchWindow window = site.getWorkbenchWindow();
		return window != null ? window.getSelectionService() : null;
	}

	private static class SelectionListenerImpl implements ISelectionListener {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				selectionChanged((IStructuredSelection) selection);
			}
		}

		private void selectionChanged(IStructuredSelection selection) {
			Object element = selection.getFirstElement();
			if (element != null) {
				handleElementSelected(element);
			}
		}

		private void handleElementSelected(Object element) {
			lastSelectedElement = element;
		}

	}

}
