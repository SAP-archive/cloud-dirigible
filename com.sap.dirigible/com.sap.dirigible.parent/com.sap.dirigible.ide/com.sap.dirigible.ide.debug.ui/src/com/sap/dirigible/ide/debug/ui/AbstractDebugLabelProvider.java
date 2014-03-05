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

import java.net.URL;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sap.dirigible.ide.common.image.ImageUtils;

public class AbstractDebugLabelProvider extends LabelProvider {
	
	private static final long serialVersionUID = 4166213744757581077L;
	
	public static final URL DEBUG_SESSION_ICON_URL = getIconURL("debug-session.png"); //$NON-NLS-1$
	public static final URL BREAKPOINT_ICON_URL = getIconURL("breakpoint.png"); //$NON-NLS-1$
	public static final URL VARIABLE_ICON_URL = getIconURL("variable.png"); //$NON-NLS-1$

	public static URL getIconURL(String iconName) {
		URL url = ImageUtils.getIconURL("com.sap.dirigible.ide.debug.ui", "/resources/", iconName);
		return url;
	}

	protected Image createImage(URL imageURL) {
		return ImageUtils.createImage(imageURL);
	}

	

}
