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

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class GenericPerspective implements IPerspectiveFactory {

	private static final String PERSPECTIVE_ID = "help"; //$NON-NLS-1$

	public static final String GENERIC_VIEW_ID = "com.sap.dirigible.ide.generic.ui.GenericView"; //$NON-NLS-1$
	public static final String GENERIC_LIST_VIEW_ID = "com.sap.dirigible.ide.generic.ui.GenericListView"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

//		// Left
		IFolderLayout left = layout.createFolder(
				"left", IPageLayout.LEFT, 0.60f, editorArea); //$NON-NLS-1$
		left.addView(GENERIC_LIST_VIEW_ID);

		
		
		// Bottom
		IFolderLayout bottom = layout.createFolder("bottom", //$NON-NLS-1$
				IPageLayout.BOTTOM, 0.65f, editorArea);
		bottom.addView(GENERIC_VIEW_ID);
		bottom.addPlaceholder(GENERIC_VIEW_ID + ":*");

		layout.addShowViewShortcut(GENERIC_LIST_VIEW_ID);
//		layout.addShowViewShortcut(GENERIC_VIEW_ID);
		
		layout.getViewLayout(GENERIC_LIST_VIEW_ID).setCloseable(false);
//		layout.getViewLayout(GENERIC_VIEW_ID).setCloseable(false);
		
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);
	}
	

}
