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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TextContentProvider implements IContentProvider {

	private String text;

	public TextContentProvider() {
		this(""); //$NON-NLS-1$
	}

	public TextContentProvider(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public IValidationStatus validate() {
		return ValidationStatus.createOk();
	}

	public InputStream getContent() {
		return new ByteArrayInputStream(text.getBytes());
	}

}
