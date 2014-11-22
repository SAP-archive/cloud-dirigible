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

package com.sap.dirigible.ide.debug.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.debug.ui.messages"; //$NON-NLS-1$
	public static String DebugView_CONTINUE;
	public static String DebugView_FILE;
	public static String DebugView_REFRESH;
	public static String DebugView_ROW;
	public static String DebugView_SKIP_BREAKPOINTS;
	public static String DebugView_SOURCE;
	public static String DebugView_STEP_INTO;
	public static String DebugView_STEP_OVER;
	public static String DebugView_VALUES;
	public static String DebugView_VARIABLES;
	public static String DebugView_SESSIONS;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
