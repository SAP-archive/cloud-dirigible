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

package com.sap.dirigible.ide.workspace.ui.view;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.workspace.ui.view.messages"; //$NON-NLS-1$
	public static String WorkspaceExplorerView_CHECK_LOGS_FOR_MORE_INFO;
	public static String WorkspaceExplorerView_COULD_NOT_EXECUTE_COMMAND;
	public static String WorkspaceExplorerView_COULD_NOT_EXECUTE_OPEN_COMMAND;
	public static String WorkspaceExplorerView_COULD_NOT_EXECUTE_OPEN_COMMAND_DUE_TO_THE_FOLLOWING_ERROR;
	public static String WorkspaceExplorerView_COULD_NOT_EXECUTE_COMMAND_DUE_TO_THE_FOLLOWING_ERROR;
	public static String WorkspaceExplorerView_OPERATION_FAILED;
	public static String WorkspaceExplorerView_PUBLISH_FAILED;
	public static String WorkspaceExplorerView_ACTIVATION_FAILED;
	public static String WorkspaceExplorerView_PUBLISH;
	public static String WorkspaceExplorerView_ACTIVATE;
	public static String WorkspaceExplorerView_INVALID_DEFAULT_HANDLER_IMPLEMENTING_CLASS_CONFIGURED;
	public static String WorkspaceExplorerView_COULD_NOT_CREATE_NEW_MENU_ITEM_INSTANCE;
	public static String WorkspaceExplorerView_EXTENSION_POINT_0_COULD_NOT_BE_FOUND;
	public static String WorkspaceExplorerView_NEW;
	public static String WorkspaceExplorerView_SAVE;
	public static String WorkspaceExplorerView_SAVE_ALL;
	public static String WebViewerView_OPEN;
	public static String WebViewerView_PUBLIC;
	public static String WebViewerView_SANDBOX;
	public static String WebViewerView_REFRESH;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
