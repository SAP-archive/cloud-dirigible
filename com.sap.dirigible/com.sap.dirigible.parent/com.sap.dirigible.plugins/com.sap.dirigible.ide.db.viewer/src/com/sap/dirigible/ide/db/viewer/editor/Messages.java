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

package com.sap.dirigible.ide.db.viewer.editor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.db.viewer.editor.messages"; //$NON-NLS-1$
	public static String DbEditorInput_DATABASE_METADATA_FOR;
	public static String DbTableMetadataEditor_TABLE_DETAILS;
	public static String TableDetailsEditorPage_ALLOW_NULL_TEXT;
	public static String TableDetailsEditorPage_ASC_DESC_TEXT;
	public static String TableDetailsEditorPage_CARDINALITY_TEXT;
	public static String TableDetailsEditorPage_COLUMN_NAME_TEXT;
	public static String TableDetailsEditorPage_DELETE_RULE_TEXT;
	public static String TableDetailsEditorPage_FILTER_CONDITION_TEXT;
	public static String TableDetailsEditorPage_FOREIGN_KEY_COLUMN_TEXT;
	public static String TableDetailsEditorPage_FOREIGN_KEY_TABLE_TEXT;
	public static String TableDetailsEditorPage_FOREIGN_KEY_TEXT;
	public static String TableDetailsEditorPage_INDEX_NAME_TEXT;
	public static String TableDetailsEditorPage_INDEXES_TEXT;
	public static String TableDetailsEditorPage_KEY_TEXT;
	public static String TableDetailsEditorPage_LENGTH_TEXT;
	public static String TableDetailsEditorPage_NON_UNIQUE_TEXT;
	public static String TableDetailsEditorPage_ORDINAL_POSITION_TEXT;
	public static String TableDetailsEditorPage_PAGES_TEXT;
	public static String TableDetailsEditorPage_PK_COLUMN_NAME_TEXT;
	public static String TableDetailsEditorPage_PK_TABLE_TEXT;
	public static String TableDetailsEditorPage_PRIMARY_KEY_NAME_TEXT;
	public static String TableDetailsEditorPage_QUALIFIER_TEXT;
	public static String TableDetailsEditorPage_TYPE_TEXT;
	public static String TableDetailsEditorPage_UPDATE_RULE_TEXT;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
