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

package com.sap.dirigible.ide.services.security.manager.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.services.security.manager.views.messages"; //$NON-NLS-1$
	public static String SecurityManagerView_ARE_YOU_SURE_YOU_WANT_TO_REMOVE_THE_SELECTED_LOCATION_FROM_THE_LIST_OF_PROTECTED_LOCATIONS;
	public static String SecurityManagerView_LOCATION;
	public static String SecurityManagerView_LOCATION_IS_TOO_LONG;
	public static String SecurityManagerView_PROTECT_A_GIVEN_RELATIVE_URL_TRANSITIVELY;
	public static String SecurityManagerView_PROTECTED_URL;
	public static String SecurityManagerView_REFRESH;
	public static String SecurityManagerView_REFRESH_THE_LIST_OF_PROTECTED_LOCATIONS;
	public static String SecurityManagerView_REMOVE_THE_SELECTED_LOCATION_FROM_THE_LIST_OF_PROTECTED_LOCATIONS;
	public static String SecurityManagerView_ROLES;
	public static String SecurityManagerView_SECURE_LOCATION;
	public static String SecurityManagerView_SECURITY_ERROR;
	public static String SecurityManagerView_UNSECURE_LOCATION;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
