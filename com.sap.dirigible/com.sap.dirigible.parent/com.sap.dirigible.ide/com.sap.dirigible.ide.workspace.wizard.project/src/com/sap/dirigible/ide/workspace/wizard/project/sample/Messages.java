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

package com.sap.dirigible.ide.workspace.wizard.project.sample;

import org.eclipse.osgi.util.NLS;

import com.sap.dirigible.ide.workspace.wizard.project.sample.Messages;

public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.workspace.wizard.project.sample.messages";//$NON-NLS-1$
	public static String SampleProjectWizard_COULD_NOT_CREATE_PROJECT;
	public static String SampleProjectWizard_OPERATION_FAILED ;
	public static String SampleProjectWizard_PROJECT_S_CREATED_SUCCESSFULLY;
	public static String SampleProjectWizardGitTemplatePage_ERROR_ON_LOADING_GIT_TEMPLATES_FOR_GENERATION;
	public static String SampleProjectWizardGitTemplatePage_PAGE_NAME;
	public static String SampleProjectWizardGitTemplatePage_PAGE_TITLE;
	public static String SampleProjectWizardGitTemplatePage_PAGE_DESCRIPTION;
	public static String SampleProjectWizardMainPage_PAGE_NAME;
	public static String SampleProjectWizardMainPage_PAGE_TITLE;
	public static String SampleProjectWizardMainPage_ENTER_PROJECT_NAME;
	public static String SampleProjectWizardMainPage_PAGE_DESCRIPTION;
	public static String SampleProjectWizard_WINDOW_TITLE;
	public static String SampleProjectWizardGitTemplatePage_AVAILABLE_GIT_TEMPLATES;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
