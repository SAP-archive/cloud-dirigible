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

package com.sap.dirigible.ide.db.viewer.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class DatabasePerspective implements IPerspectiveFactory {

	private static final String BOTTOM_NAME = "bottom"; //$NON-NLS-1$

	private static final String LEFT_NAME = "left"; //$NON-NLS-1$

	private static final String PERSPECTIVE_ID = "database"; //$NON-NLS-1$

	// private static final String PROPERTY_SHEET_VIEW_ID =
	// "org.eclipse.ui.views.PropertySheet";

	private static final String DATABASE_VIEW_ID = "com.sap.dirigible.ide.db.viewer.views.DatabaseViewer"; //$NON-NLS-1$

	private static final String SQL_CONSOLE_VIEW_ID = "com.sap.dirigible.ide.db.viewer.views.SQLConsole"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		final String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		// Left
		final IFolderLayout left = layout.createFolder(LEFT_NAME,
				IPageLayout.LEFT, 0.35f, editorArea);
		left.addView(DATABASE_VIEW_ID);
		layout.getViewLayout(DATABASE_VIEW_ID).setCloseable(false);

		// // Right
		// final IFolderLayout right = layout.createFolder("right",
		// IPageLayout.RIGHT, 0.20f, DATABASE_VIEW_ID);
		// right.addView(SQL_CONSOLE_VIEW_ID);
		// layout.getViewLayout(SQL_CONSOLE_VIEW_ID).setCloseable(false);

		// Bottom
		final IFolderLayout bottom = layout.createFolder(BOTTOM_NAME,
				IPageLayout.BOTTOM, 0.60f, editorArea);
		bottom.addView(SQL_CONSOLE_VIEW_ID);
		layout.getViewLayout(SQL_CONSOLE_VIEW_ID).setCloseable(false);
		// bottom.addView(PROPERTY_SHEET_VIEW_ID);

		layout.addShowViewShortcut(DATABASE_VIEW_ID);
		layout.addShowViewShortcut(SQL_CONSOLE_VIEW_ID);
		// layout.addShowViewShortcut(PROPERTY_SHEET_VIEW_ID);
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);

	}

}
