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

package com.sap.dirigible.ide.db.viewer.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import com.sap.dirigible.ide.db.viewer.views.DatabaseViewContentProvider;
import com.sap.dirigible.ide.db.viewer.views.TreeObject;

public class RefreshViewAction extends Action {

	private static final String REFRESH_DATABASE_BROWSER = Messages.RefreshViewAction_REFRESH_DATABASE_BROWSER;

	private static final String REFRESH = Messages.RefreshViewAction_REFRESH;

	private static final long serialVersionUID = -6466267290652926585L;
	private TreeViewer viewer;
	private TreeObject treeObject;

	public RefreshViewAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(REFRESH);
		setToolTipText(REFRESH_DATABASE_BROWSER);
	}

	public RefreshViewAction(TreeViewer viewer, TreeObject treeObject) {
		this.viewer = viewer;
		this.treeObject = treeObject;
		setText(REFRESH);
		setToolTipText(REFRESH_DATABASE_BROWSER);
	}

	public void run() {
		if (DatabaseViewContentProvider.class.isInstance(viewer
				.getContentProvider())) {
			((DatabaseViewContentProvider) viewer.getContentProvider())
					.requestRefreshContent();
		}
		viewer.refresh();
		if (treeObject != null) {
			viewer.expandToLevel(treeObject, 100);
		}
	}
}
