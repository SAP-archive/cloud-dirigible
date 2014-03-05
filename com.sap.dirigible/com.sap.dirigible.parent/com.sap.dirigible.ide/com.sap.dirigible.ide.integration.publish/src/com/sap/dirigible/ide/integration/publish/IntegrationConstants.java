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

package com.sap.dirigible.ide.integration.publish;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.common.ICommonConstants;

public interface IntegrationConstants {

	public static final String IS_CONTENT_FOLDER = ICommonConstants.ARTIFACT_TYPE.INTEGRATION_SERVICES;
	public static final String IS_REGISTYRY_PUBLISH_LOCATION = CommonParameters.DB_DIRIGIBLE_REGISTRY + IS_CONTENT_FOLDER; //$NON-NLS-1$

	public static final String EXTENSION_ROUTE = ".routes"; //$NON-NLS-1$
	public static final String EXTENSION_WS = ".ws"; //$NON-NLS-1$
	public static final String EXTENSION_XSL = ".xsl"; //$NON-NLS-1$
	public static final String EXTENSION_XSLT = ".xslt"; //$NON-NLS-1$
	
	// Actions
	public static final String ACTION 			= "ACTION"; //$NON-NLS-1$
	public static final String ACTION_LOAD 		= "ACTION_LOAD"; //$NON-NLS-1$
	public static final String ACTION_PING 		= "ACTION_PING"; //$NON-NLS-1$
	public static final String ACTION_ROUTES 	= "ACTION_ROUTES"; //$NON-NLS-1$
	public static final String ACTION_WS 		= "ACTION_WS"; //$NON-NLS-1$

	// Statuses
	public static final String STATUS 			= "STATUS"; //$NON-NLS-1$
	public static final String STATUS_OK 		= "STATUS_OK"; //$NON-NLS-1$
	public static final String STATUS_FAILED 	= "STATUS_FAILED"; //$NON-NLS-1$

}
