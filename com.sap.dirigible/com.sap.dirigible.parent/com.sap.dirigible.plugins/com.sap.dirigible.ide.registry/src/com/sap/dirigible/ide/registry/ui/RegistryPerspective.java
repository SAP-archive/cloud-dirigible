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

package com.sap.dirigible.ide.registry.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class RegistryPerspective implements IPerspectiveFactory {

	private static final String PERSPECTIVE_ID = "registry"; //$NON-NLS-1$

	private static final String REGISTRY_VIEW_ID = "com.sap.dirigible.ide.registry.ui.RegistryView"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		// Left
		IFolderLayout left = layout.createFolder(
				"left", IPageLayout.LEFT, 0.40f, editorArea); //$NON-NLS-1$
		left.addView(REGISTRY_VIEW_ID);
		layout.getViewLayout(REGISTRY_VIEW_ID).setCloseable(false);

		layout.addShowViewShortcut(REGISTRY_VIEW_ID);
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);

	}

}
