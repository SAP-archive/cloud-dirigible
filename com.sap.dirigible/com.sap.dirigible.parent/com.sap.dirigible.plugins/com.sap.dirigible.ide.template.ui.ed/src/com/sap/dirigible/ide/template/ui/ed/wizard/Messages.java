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

package com.sap.dirigible.ide.template.ui.ed.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.template.ui.ed.wizard.messages"; //$NON-NLS-1$
	public static String ExtensionDefinitionTemplateModel_TARGET_LOCATION_IS_NOT_ALLOWED;
	public static String ExtensionDefinitionTemplateTargetLocationPage_SELECT_THE_TARGET_LOCATION_AND_THE_TARGET_FILE_NAME;
	public static String ExtensionDefinitionTemplateTargetLocationPage_TARGET_LOCATION;
	public static String ExtensionDefinitionTemplateTypePage_MAIN_ACCESS_FILE;
	public static String ExtensionDefinitionTemplateTypePage_SELECT_THE_TYPE_OF_THE_TEMPLATE_WHICH_WILL_BE_USED_DURING_GENERATION;
	public static String ExtensionDefinitionTemplateTypePage_SELECTION_OF_TEMPLATE_TYPE;
	public static String ExtensionDefinitionTemplateWizard_CREATE_EXTENSION_FILE;
	public static String ExtensionsView_EXTENSIONS_ERROR;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
