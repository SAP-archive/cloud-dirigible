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

package com.sap.dirigible.ide.generic.ui;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sap.dirigible.ide.logging.Logger;

public class GenericListViewContentProvider implements ITreeContentProvider {

	private static final long serialVersionUID = 1309704265765047023L;

	private static final Logger logger = Logger
			.getLogger(GenericListViewContentProvider.class);

	private GenericListView genericListView;

	public GenericListViewContentProvider(GenericListView genericListView) {
		this.genericListView = genericListView;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		//
	}

	public void dispose() {
		//
	}

	public Object[] getElements(Object parent) {
		List<GenericLocationMetadata> list = null;
		GenericLocationMetadata[] elements = new GenericLocationMetadata[] {};
		try {
			list = genericListView.getGenericListManager().getLocationsList();
			elements = list.toArray(new GenericLocationMetadata[] {});
		} catch (Exception e) {
			logger.error(GenericListView.GENERIC_VIEW_ERROR, e);
			MessageDialog.openError(this.genericListView.getViewer()
					.getControl().getShell(),
					GenericListView.GENERIC_VIEW_ERROR, e.getMessage());
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
