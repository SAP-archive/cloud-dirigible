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

package com.sap.dirigible.ide.workspace.wizard.project.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.workspace.wizard.project.commands.messages"; //$NON-NLS-1$
	public static String UploadProjectHandler_CANNOT_CLOSE_INPUT_STREAM_TO_AN_UPLOADED_FILE;
	public static String UploadProjectHandler_CANNOT_SAVE_UPLOADED_FILE;
	public static String UploadProjectHandler_CANNOT_UPLOAD;
	public static String UploadProjectHandler_OVERRIDE_PROJECTS;
	public static String UploadProjectHandler_REASON;
	public static String UploadProjectHandler_THIS_PROCESS_WILL_OVERRIDE_YOUR_EXISTING_PROJECTS_DO_YOU_WANT_TO_CONTINUE;
	public static String UploadProjectHandler_UPLOAD_ERROR;
	public static String UploadProjectHandler_UPLOAD_PROJECT_ARCHIVE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
