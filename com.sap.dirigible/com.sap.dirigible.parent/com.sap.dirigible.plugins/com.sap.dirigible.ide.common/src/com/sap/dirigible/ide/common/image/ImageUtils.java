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

package com.sap.dirigible.ide.common.image;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

public class ImageUtils {

	private static ResourceManager resourceManager = new LocalResourceManager(
			JFaceResources.getResources());
	
	public static URL getIconURL(String bundleName, String prefix, String iconName) {
		URL url = FileLocator.find(Platform.getBundle(bundleName),
				new Path(prefix + iconName), null);
		return url;
	}
	
	public static Image createImage(URL imageURL) {
		final ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(imageURL);
		return resourceManager.createImage(imageDescriptor);
	}
}
