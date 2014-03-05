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

package com.sap.dirigible.ide.workspace.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class WorkspacePerspective implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "workspace"; //$NON-NLS-1$

	public static final String WORKSPACE_EXPLORER_VIEW_ID = "com.sap.dirigible.ide.workspace.ui.view.WorkspaceExplorerView"; //$NON-NLS-1$

	public static final String PROPERTY_SHEET_VIEW_ID = "org.eclipse.ui.views.PropertySheet"; //$NON-NLS-1$

	// private static final String CONTENT_OUTLINE_VIEW_ID =
	// "org.eclipse.ui.views.ContentOutline";

	public static final String LOGS_VIEW_ID = "com.sap.dirigible.ide.workspace.ui.view.LogsView";

	public static final String WEB_VIEWER_VIEW_ID = "com.sap.dirigible.ide.publish.ui.view.WebViewerView"; //$NON-NLS-1$

	public static final String SECURITY_MANAGER_VIEW_ID = "com.sap.dirigible.ide.services.security.manager.views.SecurityManagerView"; //$NON-NLS-1$

	public static final String CHEAT_SHEET_VIEW_ID = "org.eclipse.ui.cheatsheets.views.CheatSheetView"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		// Left
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, //$NON-NLS-1$
				0.40f, editorArea);
		left.addView(WORKSPACE_EXPLORER_VIEW_ID);
		layout.getViewLayout(WORKSPACE_EXPLORER_VIEW_ID).setCloseable(false);

		// TODO - Throws exceptions that the view has been already registered?!
		// // Right
		// IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT,
		// 0.85f, editorArea);
		// right.addView(CHEAT_SHEET_VIEW_ID);

		// Bottom
		IFolderLayout bottom = layout.createFolder("bottom", //$NON-NLS-1$
				IPageLayout.BOTTOM, 0.35f, editorArea);
		bottom.addView(PROPERTY_SHEET_VIEW_ID);
		bottom.addView(WEB_VIEWER_VIEW_ID);
		// bottom.addView(CONTENT_OUTLINE_VIEW_ID);
		bottom.addView(LOGS_VIEW_ID);
		bottom.addView(SECURITY_MANAGER_VIEW_ID);

		layout.addShowViewShortcut(WORKSPACE_EXPLORER_VIEW_ID);
		layout.addShowViewShortcut(PROPERTY_SHEET_VIEW_ID);
		layout.addShowViewShortcut(WEB_VIEWER_VIEW_ID);
		layout.addShowViewShortcut(CHEAT_SHEET_VIEW_ID);
		// layout.addShowViewShortcut(CONTENT_OUTLINE_VIEW_ID);
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);
	}

}
