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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;

public class WorkspaceViewer {

	private static final String WORKSPACE_MENU = "Workspace Menu"; //$NON-NLS-1$

	private final TreeViewer viewer;

	private final MenuManager menuManager;

	private final IResourceChangeListener changeListener = new CustomResourceChangeListener();

	public WorkspaceViewer(Composite parent, int style) {
		this(parent, style, RemoteResourcesPlugin.getWorkspace());
	}

	private WorkspaceViewer(Composite parent, int style, IWorkspace workspace) {
		// Create and configure viewer
		PatternFilter filter = new PatternFilter();
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL, filter, true);
		viewer = tree.getViewer();
		viewer.setContentProvider(new WorkspaceContentProvider());
		viewer.setLabelProvider(new WorkspaceLabelProvider());
		viewer.setSorter(new WorkspaceSorter());

//		viewer.addDragSupport(
//				DND.DROP_MOVE,
//				WorkspaceDragSourceListener.SUPPORTED_DND_SOURCE_TRANSFER_TYPES,
//				new WorkspaceDragSourceListener(viewer));
//		viewer.addDropSupport(
//				DND.DROP_MOVE,
//				WorkspaceDropTargetListener.SUPPORTED_DND_TARGET_TRANSFER_TYPES,
//				new WorkspaceDropTargetListener(viewer, workspace));

		viewer.getControl().addDisposeListener(new DisposeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2504065180094207979L;

			public void widgetDisposed(DisposeEvent event) {
				releaseData();
			}
		});

		// Configure context menu
		menuManager = new MenuManager(WORKSPACE_MENU, "sample.MenuManager"); //$NON-NLS-1$
		menuManager
				.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		final Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		if (workspace != null) {
			setInput(workspace.getRoot());
		} else {
			setInput(null);
		}
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}

	public Control getControl() {
		return (viewer != null) ? viewer.getControl() : null;
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public void setInput(IContainer container) {
		final IContainer currentInput = getInput();
		if (!areEqual(currentInput, container)) {
			changeInput(currentInput, container);
		}
		// container.getWorkspace().addResourceChangeListener(changeListener);
		// viewer.setInput(container);
	}

	public IContainer getInput() {
		return (IContainer) viewer.getInput();
	}

	public void refresh() {
		Object[] elements = viewer.getExpandedElements();
		viewer.refresh();
		viewer.setExpandedElements(elements);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void dispose() {
		viewer.getControl().dispose();
	}

	private void changeInput(IContainer oldInput, IContainer newInput) {
		final IWorkspace oldWorkspace = (oldInput != null) ? oldInput
				.getWorkspace() : null;
		final IWorkspace newWorkspace = (newInput != null) ? newInput
				.getWorkspace() : null;
		if (!areEqual(oldWorkspace, newWorkspace)) {
			changeWorkspace(oldWorkspace, newWorkspace);
		}
		viewer.setInput(newInput);
	}

	private void changeWorkspace(IWorkspace oldWorkspace,
			IWorkspace newWorkspace) {
		if (oldWorkspace != null) {
			oldWorkspace.removeResourceChangeListener(changeListener);
		}
		if (newWorkspace != null) {
			newWorkspace.addResourceChangeListener(changeListener);
		}
	}

	private boolean areEqual(Object a, Object b) {
		if (a != null) {
			return a.equals(b);
		} else {
			return (b == null);
		}
	}

	private void releaseData() {
		final IContainer input = getInput();
		if (input != null) {
			input.getWorkspace().removeResourceChangeListener(changeListener);
		}
	}

	private class CustomResourceChangeListener implements
			IResourceChangeListener {
		public void resourceChanged(IResourceChangeEvent event) {
			if (!viewer.getControl().isDisposed()) {
//				viewer.getContentProvider().dispose();
//				viewer.setContentProvider(new WorkspaceContentProvider());
				refresh();
			}
		}
	}

}
