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

package com.sap.dirigible.ide.ui.widgets.connection;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.ui.widgets.connection.messages"; //$NON-NLS-1$
	public static String ConnectionViewer_COLOR_CANNOT_BE_NULL;
	public static String ConnectionViewer_CONTENT_PROVIDER_MUST_NOT_RETURN_NULL;
	public static String ConnectionViewer_INVALID_OR_MISSING_CONTENT_PROVIDER;
	public static String ConnectionViewer_INVALID_OR_MISSING_LABEL_PROVIDER;
	public static String ConnectionViewer_INVALID_OR_MISSING_SOURCE_ITEM_RESOLVER;
	public static String ConnectionViewer_INVALID_OR_MISSING_TARGET_ITEM_RESOLVER;
	public static String ConnectionViewer_INVALID_OR_NULL_SELECTION;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
