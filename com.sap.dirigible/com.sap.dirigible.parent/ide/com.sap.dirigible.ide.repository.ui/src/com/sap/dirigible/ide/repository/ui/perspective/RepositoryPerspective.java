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

package com.sap.dirigible.ide.repository.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.sap.dirigible.ide.repository.ui.view.RepositoryView;
import com.sap.dirigible.ide.repository.ui.view.ResourceHistoryView;

public class RepositoryPerspective implements IPerspectiveFactory {

	private static final String PERSPECTIVE_ID = "repository"; //$NON-NLS-1$

	private static final String PROPERTY_SHEET_VIEW_ID = "org.eclipse.ui.views.PropertySheet"; //$NON-NLS-1$

	public void createInitialLayout(IPageLayout layout) {
		final String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		// Left
		final IFolderLayout left = layout.createFolder("left", //$NON-NLS-1$
				IPageLayout.LEFT, 0.35f, editorArea);
		left.addView(RepositoryView.ID);
		layout.getViewLayout(RepositoryView.ID).setCloseable(false);

		// Bottom
		final IFolderLayout bottom = layout.createFolder("bottom", //$NON-NLS-1$
				IPageLayout.BOTTOM, 0.60f, editorArea);
		bottom.addView(ResourceHistoryView.ID);
		bottom.addView(PROPERTY_SHEET_VIEW_ID);

		layout.addShowViewShortcut(RepositoryView.ID);
		layout.addShowViewShortcut(PROPERTY_SHEET_VIEW_ID);
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);
	}

}
