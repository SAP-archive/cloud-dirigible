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

package com.sap.dirigible.ide.editor.text.editor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.editor.text.editor.messages"; //$NON-NLS-1$
	public static String AbstractTextEditor_CANNOT_SAVE_DOCUMENT;
	public static String AbstractTextEditor_SAVE_ERROR;
	public static String ContentProviderFactory_CANNOT_READ_CONTENT_PROVIDER_EXTENSION_CLASS_0_INPUT_TYPE_1;
	public static String ContentProviderFactory_UNSUPPORTED_I_EDITOR_INPUT;
	public static String DefaultContentProvider_CANNOT_READ_FILE_CONTENTS;
	public static String DefaultContentProvider_CANNOT_SAVE_FILE_CONTENTS;
	public static String DefaultContentProvider_WE_SHOULD_NEVER_GET_HERE;
	public static String TextEditor_CANNOT_LOAD_DOCUMENT;
	public static String TextEditor_EDITOR_INPUT_CANNOT_BE_NULL;
	public static String TextEditor_ERROR;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
