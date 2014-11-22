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

package com.sap.dirigible.ide.template.ui.ed.view;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sap.dirigible.ide.common.image.ImageUtils;
import com.sap.dirigible.repository.ext.extensions.ExtensionDefinition;
import com.sap.dirigible.repository.ext.extensions.ExtensionPointDefinition;

public class ExtensionsLabelProvider extends LabelProvider {
	private static final long serialVersionUID = 1L;

	private static final Image EXTENSION_DEFINITION_ICON = ImageUtils.createImage(ImageUtils
			.getIconURL("com.sap.dirigible.ide.repository.ui", //$NON-NLS-1$
					"/resources/icons/", "icon-extension.png")); //$NON-NLS-1$ //$NON-NLS-1$

	private static final Image EXTENSION_POINT_DEFINITION_ICON = ImageUtils.createImage(ImageUtils
			.getIconURL("com.sap.dirigible.ide.repository.ui", //$NON-NLS-1$
					"/resources/icons/", "icon-extension-point.png")); //$NON-NLS-1$ //$NON-NLS-1$

	@Override
	public String getText(Object element) {
		String text = null;
		if (element instanceof ExtensionPointDefinition) {
			text = ((ExtensionPointDefinition) element).getLocation();
		} else if (element instanceof ExtensionDefinition) {
			text = ((ExtensionDefinition) element).getLocation();
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {
		Image image = null;
		if (element instanceof ExtensionPointDefinition) {
			image = EXTENSION_POINT_DEFINITION_ICON;
		} else if (element instanceof ExtensionDefinition) {
			image = EXTENSION_DEFINITION_ICON;
		}
		return image;
	}

}
