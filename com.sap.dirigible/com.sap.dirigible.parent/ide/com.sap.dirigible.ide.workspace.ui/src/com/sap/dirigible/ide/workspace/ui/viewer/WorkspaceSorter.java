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
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class WorkspaceSorter extends ViewerSorter {

	private static final long serialVersionUID = -6643939337586499946L;

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if ((e2 instanceof IContainer) && (e1 instanceof IResource)
				&& !(e1 instanceof IContainer)) {
			return 1;
		}
		if ((e1 instanceof IContainer) && (e2 instanceof IResource)
				&& !(e2 instanceof IContainer)) {
			return -1;
		}
		if ((e1 instanceof IResource) && (e2 instanceof IResource)) {
			final IResource resource1 = (IResource) e1;
			final IResource resource2 = (IResource) e2;
			final String path1 = resource1.getFullPath().toString();
			final String path2 = resource2.getFullPath().toString();
			return path1.compareTo(path2);
		}
		return 0;
	}

}
