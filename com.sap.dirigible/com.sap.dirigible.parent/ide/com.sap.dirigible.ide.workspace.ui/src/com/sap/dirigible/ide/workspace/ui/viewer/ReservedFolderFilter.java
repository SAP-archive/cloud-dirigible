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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ReservedFolderFilter extends ViewerFilter {

	private static final long serialVersionUID = 4273811741225537399L;
	private String folderName;

	public ReservedFolderFilter(String folderName) {
		this.folderName = folderName;

	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IProject) {
			return true;
		}
		if (element instanceof IFolder) {
			IFolder iFolder = (IFolder) element;
			if (iFolder.getParent() instanceof IProject) {
				return folderName.equals(iFolder.getName());
			}
			return true;
		}
		return false;
	}

}
