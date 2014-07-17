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

package com.sap.dirigible.ide.jgit.command.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.jgit.command.ui.messages"; //$NON-NLS-1$
	public static String BaseCommandDialog_PASSWORD_IS_EMPTY;
	public static String BaseCommandDialog_USERNAME_IS_EMPTY;
	public static String CloneCommandDialog_CLONING_GIT_REPOSITORY;
	public static String CloneCommandDialog_ENTER_GIT_REPOSITORY_URL;
	public static String CloneCommandDialog_INVALID_GIT_REPOSITORY_URL;
	public static String CommandDialog_COMMIT_MESSAGE;
	public static String BaseCommandDialog_PASSWORD;
	public static String BaseCommandDialog_PUSH_CHANGES_TO_REMOTE_GIT_REPOSITORY;
	public static String BaseCommandDialog_USERNAME;
	public static String CommandDialog_REPOSITORY_URI;
	public static String ShareCommandDialog_REPOSITORY_URI_IS_EMPTY;
	public static String ShareCommandDialog_SHARE_TO_REMOTE_GIT_REPOSITORY;
	public static String PushCommandDialog_COMMIT_MESSAGE_IS_EMPTY;
	public static String PushCommandDialog_EMAIL;
	public static String PushCommandDialog_EMAIL_IS_EMPTY;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
