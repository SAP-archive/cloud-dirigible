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

package com.sap.dirigible.ide.repository.ui.command;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.repository.ui.command.messages"; //$NON-NLS-1$
	public static String DeleteHandler_ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEM;
	public static String DeleteHandler_ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEMS_D;
	public static String DeleteHandler_CONFIRM_DELETE;
	public static String DeleteHandler_DELETE_ERROR;
	public static String DeleteHandler_SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_DELETED;
	public static String OpenHandler_COULD_NOT_OPEN_ONE_OR_MORE_FILES;
	public static String OpenHandler_OPEN_FAILURE;
	public static String PasteHandler_PASTE_ERROR;
	public static String PasteHandler_SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_PASTED;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
