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

package com.sap.dirigible.ide.template.ui.db.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.template.ui.db.wizard.messages"; //$NON-NLS-1$
	public static String AddColumnDialog_ADD_COLUMN;
	public static String AddColumnDialog_DEFAULT_VALUE;
	public static String AddColumnDialog_DUPLICATE_COLUMN_NAME;
	public static String AddColumnDialog_INPUT_THE_LENGTH;
	public static String AddColumnDialog_INPUT_THE_LENGTH_AS_INTEGER;
	public static String AddColumnDialog_INPUT_THE_NAME;
	public static String AddColumnDialog_INPUT_THE_TYPE;
	public static String AddColumnDialog_LENGTH;
	public static String AddColumnDialog_NAME;
	public static String AddColumnDialog_NOT_NULL;
	public static String AddColumnDialog_PRIMARY_KEY;
	public static String AddColumnDialog_TYPE;
	public static String DataStructureTemplateDSVPage_0;
	public static String DataStructureTemplateDSVPage_1;
	public static String DataStructureTemplateDSVPage_2;
	public static String DataStructureTemplateDSVPage_3;
	public static String DataStructureTemplateQueryPage_INPUT_THE_SQL_QUERY_FOR_THE_VIEW;
	public static String DataStructureTemplateQueryPage_QUERY;
	public static String DataStructureTemplateQueryPage_SET_THE_QUERY_FOR_THE_VIEW_WHICH_WILL_BE_USED_DURING_THE_GENERATION;
	public static String DataStructureTemplateStructurePage_ADD;
	public static String DataStructureTemplateStructurePage_ADD_COLUMN_DEFINITIONS_FOR_THE_SELECTED_DATA_STRUCTURE;
	public static String DataStructureTemplateStructurePage_ARE_YOU_SURE_YOU_WANT_TO_REMOVE_THE_SELECTED_COLUMN;
	public static String DataStructureTemplateStructurePage_COLUMN_DEFINITIONS;
	public static String DataStructureTemplateStructurePage_DEFINITION_OF_COLUMNS;
	public static String DataStructureTemplateStructurePage_REMOVE;
	public static String DataStructureTemplateStructurePage_REMOVE_COLUMN;
	public static String DataStructureTemplateStructurePage_TREE_DEFAULT;
	public static String DataStructureTemplateStructurePage_TREE_LENGTH;
	public static String DataStructureTemplateStructurePage_TREE_NAME;
	public static String DataStructureTemplateStructurePage_TREE_NN;
	public static String DataStructureTemplateStructurePage_TREE_PK;
	public static String DataStructureTemplateStructurePage_TREE_TYPE;
	public static String DataStructureTemplateTargetLocationPage_SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME;
	public static String DataStructureTemplateTargetLocationPage_TARGET_LOCATION;
	public static String DataStructureTemplateTypePage_DATABASE_TABLE;
	public static String DataStructureTemplateTypePage_DATABASE_VIEW;
	public static String DataStructureTemplateTypePage_SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION;
	public static String DataStructureTemplateTypePage_SELECTION_OF_TEMPLATE_TYPE;
	public static String DataStructureTemplateWizard_CREATE_DATA_STRUCTURE;
	public static String TableTemplateModel_DUPLICATE_COLUMN_NAMES_FOUND;
	public static String TableTemplateModel_NO_COLUMNS_DEFINED;
	public static String TableTemplateModel_NO_PRIMARY_KEY_FOUND;
	public static String TableTemplateModel_TABLE_OR_VIEW_WITH_THE_SAME_NAME_ALREADY_EXISTS;
	public static String TableTemplateModel_TARGET_LOCATION_IS_NOT_ALLOWED;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
