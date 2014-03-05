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

package com.sap.dirigible.ide.workspace.ui.viewer;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.sap.dirigible.ide.workspace.ui.view.WorkspaceExplorerView;

public class WorkspaceViewerUtils {
	
	public static void expandElement(Object element) {
		if (element instanceof IFolder
				|| element instanceof IProject) {
			
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart view = page.findView(WorkspaceExplorerView.VIEW_ID);
			if (view != null
					&& view instanceof WorkspaceExplorerView) {
				TreeViewer treeViewer = ((WorkspaceExplorerView)view).getViewer().getViewer();
				if (element instanceof IFolder
						|| element instanceof IProject) {
					treeViewer.setExpandedState(element, true);
				}
			}
		}
	}
	
	public static void doubleClickedElement(Object element) {
		if (element instanceof IFolder
				|| element instanceof IProject) {
			
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart view = page.findView(WorkspaceExplorerView.VIEW_ID);
			if (view != null
					&& view instanceof WorkspaceExplorerView) {
				TreeViewer treeViewer = ((WorkspaceExplorerView)view).getViewer().getViewer();
				if (element instanceof IFolder
						|| element instanceof IProject) {
					treeViewer.setExpandedState(element, 
							(!treeViewer.getExpandedState(element)));
				}
			}
		}
	}

	public static void selectElement(final Object element) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart view = page.findView(WorkspaceExplorerView.VIEW_ID);
		if (view != null
				&& view instanceof WorkspaceExplorerView) {
			TreeViewer treeViewer = ((WorkspaceExplorerView)view).getViewer().getViewer();
			treeViewer.setSelection(new StructuredSelection(element));
		}
	
	}
	
}
