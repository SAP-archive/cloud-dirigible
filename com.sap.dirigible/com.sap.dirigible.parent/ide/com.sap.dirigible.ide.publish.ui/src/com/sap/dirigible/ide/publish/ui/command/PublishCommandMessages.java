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

package com.sap.dirigible.ide.publish.ui.command;

import org.eclipse.osgi.util.NLS;

public final class PublishCommandMessages extends NLS {

	public static String PUBLISH_FAIL_TITLE;
	public static String NO_PROJECTS_IN_SELECTION_NOTHING_TO_PUBLISH;
	public static String NOTHING_IS_SELECTED_TO_BE_PUBLISHED;
	public static String UNKNOWN_SELECTION_TYPE;
	public static String ACTIVATION_FAIL_TITLE;
	public static String NO_PROJECTS_IN_SELECTION_NOTHING_TO_ACTIVATE;
	public static String NOTHING_IS_SELECTED_TO_BE_ACTIVATED;
	
	
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.publish.ui.command.publish_messages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, PublishCommandMessages.class);
	}

	/*
	 * Disable instantiation
	 */
	private PublishCommandMessages() {
		super();
	}

}
