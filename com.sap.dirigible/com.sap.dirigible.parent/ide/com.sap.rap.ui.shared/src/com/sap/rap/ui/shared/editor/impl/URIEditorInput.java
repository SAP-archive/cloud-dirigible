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

package com.sap.rap.ui.shared.editor.impl;

import java.net.URI;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IURIEditorInput;

public class URIEditorInput implements IURIEditorInput {

	private final URI uri;

	private final String name;

	private final String toolTipText;

	public URIEditorInput(URI uri) {
		this.uri = uri;
		this.name = extractNameFromUri(uri);
		this.toolTipText = uri.toString();
	}

	public URIEditorInput(URI uri, String name) {
		this.uri = uri;
		this.name = name;
		this.toolTipText = uri.toString();
	}

	public URIEditorInput(URI uri, String name, String toolTipText) {
		this.uri = uri;
		this.name = name;
		this.toolTipText = toolTipText;
	}

	private static String extractNameFromUri(URI uri) {
		String location = uri.toString();
		int lastSlashIndex = location.lastIndexOf('/');
		if (lastSlashIndex != -1 && lastSlashIndex != location.length() - 1) {
			return location.substring(lastSlashIndex + 1);
		} else {
			return location;
		}
	}

	public boolean exists() {
		return true; // XXX: Not clean...
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	public String getName() {
		return name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public URI getURI() {
		return uri;
	}

}
