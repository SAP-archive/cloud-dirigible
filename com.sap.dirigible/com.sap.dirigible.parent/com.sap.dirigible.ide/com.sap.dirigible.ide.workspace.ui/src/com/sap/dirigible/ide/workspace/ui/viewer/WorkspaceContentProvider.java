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

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sap.dirigible.ide.logging.Logger;

public class WorkspaceContentProvider implements ITreeContentProvider {

	private static final long serialVersionUID = -2997727533953364324L;
	private static final String COULD_NOT_DETERMINE_IF_CONTAINER_HAS_CHILDREN = Messages.WorkspaceContentProvider_COULD_NOT_DETERMINE_IF_CONTAINER_HAS_CHILDREN;
	private static final String COULD_NOT_GET_THE_CONTAINER_S_CHILDREN = Messages.WorkspaceContentProvider_COULD_NOT_GET_THE_CONTAINER_S_CHILDREN;
	private static final Logger logger = Logger
			.getLogger(WorkspaceContentProvider.class.getCanonicalName());

	public WorkspaceContentProvider() {
		super();
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		}
		if (inputElement instanceof List<?>) {
			return ((List<?>) inputElement).toArray();
		}
		return getChildren(inputElement);
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IContainer) {
			final IContainer container = (IContainer) parentElement;
			try {
				return container.members();
			} catch (CoreException ex) {
				logger.error(COULD_NOT_GET_THE_CONTAINER_S_CHILDREN);
				logger.error(WorkspaceContentProvider.class.getCanonicalName(),
						ex);
				return new Object[0];
			}
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof IResource) {
			final IResource resource = (IResource) element;
			return resource.getParent();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof IContainer) {
			final IContainer container = (IContainer) element;
			try {
				return (container.members().length > 0);
			} catch (CoreException ex) {
				logger.error(COULD_NOT_DETERMINE_IF_CONTAINER_HAS_CHILDREN);
				logger.error(WorkspaceContentProvider.class.getCanonicalName(),
						ex);
				return false;
			}
		}
		return false;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		//
	}

	public void dispose() {
		//
	}

}
