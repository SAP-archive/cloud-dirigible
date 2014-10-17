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

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class GenericListViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private static final long serialVersionUID = 2686057886720931696L;

	public String getColumnText(Object obj, int index) {
		if (obj instanceof GenericLocationMetadata) {
			GenericLocationMetadata genericLocationMetadata = (GenericLocationMetadata) obj;
			if (index == 0) {
				return genericLocationMetadata.getName();
			} else if (index == 1) {
				return genericLocationMetadata.getLocation();
			}
		}
		return getText(obj);
	}

	public Image getColumnImage(Object obj, int index) {
		if (index == 0) {
			return getImage(obj);
		} else {
			return null;
		}
	}

	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_DEF_VIEW);
	}
}
