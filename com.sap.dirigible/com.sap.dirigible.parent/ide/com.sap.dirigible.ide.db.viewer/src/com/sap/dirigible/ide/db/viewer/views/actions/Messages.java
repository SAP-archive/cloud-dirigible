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

package com.sap.dirigible.ide.db.viewer.views.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.db.viewer.views.actions.messages"; //$NON-NLS-1$
	public static String DeleteTableAction_DATABASE_VIEW;
	public static String DeleteTableAction_DELETE_TABLE;
	public static String DeleteTableAction_FAILED_TO_DELETE_TABLE_S;
	public static String DeleteTableAction_WARNING_THIS_ACTION_WILL_DELETE_THE_TABLE_AND_ALL_OF_ITS_CONTENT_CONTINUE;
	public static String DeleteTableAction_WILL_DELETE_THE_TABLE_AND_ITS_CONTENT;
	public static String RefreshViewAction_REFRESH;
	public static String RefreshViewAction_REFRESH_DATABASE_BROWSER;
	public static String ShowTableDefinitionAction_OPEN_TABLE_DEFINITION;
	public static String ShowTableDefinitionAction_WILL_SHOW_TABLE_DEFINITION_CONTENT;
	public static String ViewTableContentAction_CANNOT_OPEN_SQL_VIEW;
	public static String ViewTableContentAction_DATABASE_VIEW;
	public static String ViewTableContentAction_SHOW_CONTENT;
	public static String ViewTableContentAction_WILL_SHOW_TABLE_CONTENT;
	
	public static String ExportDataAction_EXPORT_DATA;
	public static String ExportDataAction_EXPORT_DATA_AS_DSV_FILE;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
