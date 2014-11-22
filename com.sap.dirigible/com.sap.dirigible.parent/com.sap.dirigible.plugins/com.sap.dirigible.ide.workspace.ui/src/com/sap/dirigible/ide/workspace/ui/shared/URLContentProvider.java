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

package com.sap.dirigible.ide.workspace.ui.shared;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class URLContentProvider implements IContentProvider {

	private static final String INVALID_URL_ADDRESS = Messages.URLContentProvider_INVALID_URL_ADDRESS;
	private String url;

	public URLContentProvider() {
		this(""); //$NON-NLS-1$
	}

	public URLContentProvider(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@SuppressWarnings("unused")
	public IValidationStatus validate() {
		try {
			new URL(url);
			return ValidationStatus.createOk();
		} catch (MalformedURLException ex) {
			return ValidationStatus.createError(INVALID_URL_ADDRESS);
		}
	}

	public InputStream getContent() throws IOException {
		return new URL(url).openStream();
	}

}
