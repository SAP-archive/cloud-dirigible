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

package com.sap.dirigible.ide.debug.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.sap.dirigible.ide.workspace.ui.perspective.WorkspacePerspective;

public class DebugPerspective implements IPerspectiveFactory {

	private static final String PERSPECTIVE_ID = "debug"; //$NON-NLS-1$

	private static final String DEBUG_VIEW_ID = "com.sap.dirigible.ide.debug.ui.DebugView"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		// Left
		IFolderLayout left = layout.createFolder(
				"left", IPageLayout.LEFT, 0.20f, editorArea); //$NON-NLS-1$
		left.addView(WorkspacePerspective.WORKSPACE_EXPLORER_VIEW_ID);
		layout.getViewLayout(WorkspacePerspective.WORKSPACE_EXPLORER_VIEW_ID).setCloseable(false);
		
		// Top
		IFolderLayout top = layout.createFolder(
						"top", IPageLayout.TOP, 0.20f, editorArea); //$NON-NLS-1$
				
		top.addView(DEBUG_VIEW_ID);
		layout.getViewLayout(DEBUG_VIEW_ID).setCloseable(false);

		layout.addShowViewShortcut(DEBUG_VIEW_ID);
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);
		
		// Bottom
			IFolderLayout bottom = layout.createFolder("bottom", //$NON-NLS-1$
					IPageLayout.BOTTOM, 0.80f, editorArea);
			bottom.addView(WorkspacePerspective.WEB_VIEWER_VIEW_ID);
			// bottom.addView(CONTENT_OUTLINE_VIEW_ID);
			bottom.addView(WorkspacePerspective.LOGS_VIEW_ID);

	}

}
