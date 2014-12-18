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

package com.sap.dirigible.ide.repository.ui.viewer;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;

/**
 * This is a TreeViewer class responsible for displaying the contents of a
 * repository.
 * 
 */
public class RepositoryViewer {

	private static final String REPOSITORY_MENU_MANAGER = "Repository Menu Manager"; //$NON-NLS-1$

	public static final String MENU_REPOSITORY = "menu.repository"; //$NON-NLS-1$

	private final TreeViewer viewer;

	private final MenuManager menuManager;

	private IRepository repository = null;

	public RepositoryViewer(Composite parent, int style) {
		PatternFilter filter = new PatternFilter();
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL, filter, true);
		viewer = tree.getViewer();
		viewer.setLabelProvider(new ArtifactLabelProvider());
		viewer.setContentProvider(new ArtifactTreeContentProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged((ITreeSelection) event.getSelection());
			}
		});

		menuManager = new MenuManager(REPOSITORY_MENU_MANAGER, MENU_REPOSITORY); // FIXME:
																					// Change!
		menuManager
				.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		final Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		refresh();
	}

	public RepositoryViewer(IRepository repository, Composite parent, int style) {
		this(parent, style);
		setRepository(repository);
	}

	public Control getControl() {
		return (viewer != null) ? viewer.getControl() : null;
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	public void setFocus() {
		final Control control = getControl();
		if (control != null) {
			control.setFocus();
		}
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}

	/**
	 * Changes the repository that is being displayed.
	 * 
	 * @param repository
	 */
	public void setRepository(IRepository repository) {
		this.repository = repository;
		this.viewer.setInput(createViewerInput());
	}

	/**
	 * Returns the repository that is being displayed.
	 * 
	 * @return
	 */
	public IRepository getRepository() {
		return repository;
	}

	/**
	 * Refreshes the viewer.
	 * <p>
	 * This method is useful for example when resources in the repository have
	 * been changed to force the view to display the changes.
	 */
	public void refresh() {
		final Object[] expandedElements = viewer.getExpandedElements();
		viewer.refresh();
		viewer.setExpandedElements(expandedElements);
	}

	/**
	 * Checks if this viewer or any resources it holds and uses have been
	 * disposed.
	 * 
	 * @return
	 */
	public boolean isDisposed() {
		final Control control = getControl();
		return (control != null) ? control.isDisposed() : true;
	}

	/**
	 * Disposes this viewer and any controls held by it.
	 */
	public void dispose() {
		final Control control = getControl();
		if (control != null && !control.isDisposed()) {
			control.dispose();
		}
	}

	private void handleSelectionChanged(ITreeSelection selection) {
		final Object element = selection.getFirstElement();
		if (element instanceof RepositoryViewerResolveNode) {
			refresh();
		}
	}

	private Object createViewerInput() {
		if (repository == null) {
			return new Object[0];
		}
		final ICollection root = repository.getRoot();
		return new Object[] { root };
	}

}
