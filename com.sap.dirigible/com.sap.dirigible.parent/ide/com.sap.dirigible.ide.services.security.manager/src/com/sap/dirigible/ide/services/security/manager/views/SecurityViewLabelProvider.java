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

package com.sap.dirigible.ide.services.security.manager.views;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sap.dirigible.repository.ext.security.SecurityLocationMetadata;

public class SecurityViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private static final long serialVersionUID = 2686057886720931696L;

	public String getColumnText(Object obj, int index) {
		if (obj instanceof SecurityLocationMetadata) {
			SecurityLocationMetadata securityLocationMetadata = (SecurityLocationMetadata) obj;
			if (index == 0) {
				return securityLocationMetadata.getLocation();
			} else if (index == 1) {
				return enumerateRoles(securityLocationMetadata.getRoles());
			}
		}
		return getText(obj);
	}

	private String enumerateRoles(List<String> roles) {
		StringBuilder buff = new StringBuilder();
		int i = 0;
		for (Iterator<String> iterator = roles.iterator(); iterator.hasNext();) {
			if (i++ > 0) {
				buff.append(","); //$NON-NLS-1$
			}
			String role = (String) iterator.next();
			buff.append(role);
		}
		return buff.toString();
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
				.getImage(ISharedImages.IMG_ELCL_STOP);
	}
}
