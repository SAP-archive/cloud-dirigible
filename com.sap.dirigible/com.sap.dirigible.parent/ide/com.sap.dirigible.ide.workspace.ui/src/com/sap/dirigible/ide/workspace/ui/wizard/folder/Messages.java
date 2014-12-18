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

package com.sap.dirigible.ide.workspace.ui.wizard.folder;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.workspace.ui.wizard.folder.messages"; //$NON-NLS-1$
	public static String NewFolderWizard_COULD_NOT_CREATE_FOLDER;
	public static String NewFolderWizard_FOLDER_S_CREATED_SUCCESSFULLY;
	public static String NewFolderWizard_OPERATION_FAILED;
	public static String NewFolderWizard_WINDOW_TITLE;
	public static String NewFolderWizardMainPage_FOLDER_NAME;
	public static String NewFolderWizardMainPage_PAGE_DESCRIPTION;
	public static String NewFolderWizardMainPage_PAGE_TITLE;
	public static String NewFolderWizardMainPage_PARENT_LOCATION;
	public static String NewFolderWizardModel_A_RESOURCE_WITH_THAT_PATH_ALREADY_EXISTS;
	public static String NewFolderWizardModel_FOLDER_NAME_CANNOT_BE_NULL;
	public static String NewFolderWizardModel_INVALID_FOLDER_NAME;
	public static String NewFolderWizardModel_INVALID_PARENT_LOCATION;
	public static String NewFolderWizardModel_PARENT_LOCATION_CANNOT_BE_NULL;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
