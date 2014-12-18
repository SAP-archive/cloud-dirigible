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

package com.sap.dirigible.ide.services.security.manager.views;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.repository.ext.security.SecurityException;
import com.sap.dirigible.repository.ext.security.SecurityLocationMetadata;

public class SecurityViewContentProvider implements ITreeContentProvider {

	private static final long serialVersionUID = 1309704265765047023L;

	private static final Logger logger = Logger
			.getLogger(SecurityViewContentProvider.class);

	private SecurityManagerView securityManagerView;

	public SecurityViewContentProvider(SecurityManagerView securityManagerView) {
		this.securityManagerView = securityManagerView;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		//
	}

	public void dispose() {
		//
	}

	public Object[] getElements(Object parent) {
		List<SecurityLocationMetadata> list = null;
		SecurityLocationMetadata[] elements = new SecurityLocationMetadata[] {};
		try {
			list = securityManagerView.getSecurityManager().getAccessList();
			elements = list.toArray(new SecurityLocationMetadata[] {});
		} catch (SecurityException e) {
			logger.error(SecurityManagerView.SECURITY_ERROR, e);
			MessageDialog.openError(this.securityManagerView.getViewer()
					.getControl().getShell(),
					SecurityManagerView.SECURITY_ERROR, e.getMessage());
		}
		return elements;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}
}
