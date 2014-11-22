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

package com.sap.dirigible.ide.repository.ui.view;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.repository.ui.view.messages"; //$NON-NLS-1$
	public static String RepositoryView_CHECK_LOGS_FOR_MORE_INFO;
	public static String RepositoryView_COULD_NOT_ACCESS_REPOSITORY;
	public static String RepositoryView_COULD_NOT_EXECUTE_OPEN_COMMAND_DUE_TO_THE_FOLLOWING_ERROR;
	public static String RepositoryView_OPERATION_FAILED;
	public static String ResourceHistoryView_CREATED_AT;
	public static String ResourceHistoryView_CREATED_BY;
	public static String ResourceHistoryView_RESOURCE_HISTORY;
	public static String ResourceHistoryView_VERSION;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
