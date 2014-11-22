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

package com.sap.dirigible.ide.template.ui.js.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.template.ui.js.wizard.messages"; //$NON-NLS-1$
	public static String JavascriptServiceTemplateModel_ERROR_ON_LOADING_TABLE_COLUMNS_FROM_DATABASE_FOR_GENERATION;
	public static String JavascriptServiceTemplateModel_TARGET_LOCATION_IS_NOT_ALLOWED;
	public static String JavascriptServiceTemplateTargetLocationPage_SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME;
	public static String JavascriptServiceTemplateTargetLocationPage_TARGET_LOCATION;
	public static String JavascriptServiceTemplateTypePage_BLANK_SERVER_SIDE_JAVA_SCRIPT_SERVICE;
	public static String JavascriptServiceTemplateTypePage_DATABASE_ACCESS_SAMPLE;
	public static String JavascriptServiceTemplateTypePage_ENTITY_SERVICE_ON_TABLE;
	public static String JavascriptServiceTemplateTypePage_RUBY_SERVICE;
	public static String JavascriptServiceTemplateTypePage_GROOVY_SERVICE;
	public static String JavascriptServiceTemplateTypePage_SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION;
	public static String JavascriptServiceTemplateTypePage_SELECTION_OF_TEMPLATE_TYPE;
	public static String JavascriptServiceTemplateTypePage_SERVER_SIDE_JAVA_SCRIPT_GUID_GENERATOR_LIBRARY;
	public static String JavascriptServiceTemplateWizard_CREATE_SCRIPTING_SERVICE;
	public static String TablesTemplateTablePage_AVAILABLE_TABLES_AND_VIEWS;
	public static String TablesTemplateTablePage_ERROR_ON_LOADING_TABLES_FROM_DATABASE_FOR_GENERATION;
	public static String TablesTemplateTablePage_SELECT_THE_TABLE_WHICH_WILL_BE_USED_DURING_GENERATION;
	public static String TablesTemplateTablePage_SELECTION_OF_TABLE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
