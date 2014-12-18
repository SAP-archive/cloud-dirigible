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

package com.sap.dirigible.ide.help.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class HelpPerspective implements IPerspectiveFactory {

	private static final String PERSPECTIVE_ID = "help"; //$NON-NLS-1$

	private static final String HELP_VIEW_ID = "com.sap.dirigible.ide.help.ui.HelpView"; //$NON-NLS-1$
	private static final String SAMPLES_VIEW_ID = "com.sap.dirigible.ide.help.ui.SamplesView"; //$NON-NLS-1$
	private static final String FORUM_VIEW_ID = "com.sap.dirigible.ide.help.ui.ForumView"; //$NON-NLS-1$
//	private static final String ISSUES_VIEW_ID = "com.sap.dirigible.ide.help.ui.IssuesView"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		// Left
		IFolderLayout left = layout.createFolder(
				"left", IPageLayout.LEFT, 0.60f, editorArea); //$NON-NLS-1$
		left.addView(HELP_VIEW_ID);

		
		
		// Bottom
		IFolderLayout bottom = layout.createFolder("bottom", //$NON-NLS-1$
				IPageLayout.BOTTOM, 0.35f, editorArea);
//		bottom.addView(ISSUES_VIEW_ID);
		bottom.addView(FORUM_VIEW_ID);
		bottom.addView(SAMPLES_VIEW_ID);


		layout.addShowViewShortcut(HELP_VIEW_ID);
		layout.addShowViewShortcut(SAMPLES_VIEW_ID);
		layout.addShowViewShortcut(FORUM_VIEW_ID);
//		layout.addShowViewShortcut(ISSUES_VIEW_ID);
		
		
		layout.getViewLayout(HELP_VIEW_ID).setCloseable(false);
		layout.getViewLayout(SAMPLES_VIEW_ID).setCloseable(false);
		layout.getViewLayout(FORUM_VIEW_ID).setCloseable(false);
//		layout.getViewLayout(HELP_VIEW_ID).setCloseable(false);
		
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);
	}
	

}
