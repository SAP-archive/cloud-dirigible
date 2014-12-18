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

package com.sap.dirigible.runtime.agent;

import com.sap.dirigible.repository.api.IRepositoryPaths;

public interface ConfigurationCommands {

	// Topics
	public static final String TOPIC_DIRIGIBLE_ESB_CONFIGURATION_ACTION = "/dirigible/esb/configuration/action"; //$NON-NLS-1$
	public static final String TOPIC_DIRIGIBLE_ESB_CONFIGURATION_STATUS = "/dirigible/esb/configuration/status"; //$NON-NLS-1$

	// Comments
	public static final String COMMENT_ACTION = Messages.getString("ConfigurationCommands.COMMENT_ACTION"); //$NON-NLS-1$
	public static final String COMMENT_STATUS = Messages.getString("ConfigurationCommands.COMMENT_STATUS"); //$NON-NLS-1$

	// Properties
	public static final String PROPERTY_ACTION 		= "conf.action"; //$NON-NLS-1$
	public static final String PROPERTY_STATUS 		= "conf.status"; //$NON-NLS-1$
	public static final String PROPERTY_MESSAGE 	= "conf.message"; //$NON-NLS-1$
	public static final String PROPERTY_LOCATION 	= "conf.location"; //$NON-NLS-1$

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

	public static final String LOCAL_CONFIGURATION_FILE = "/WEB-INF/beans.xml"; //$NON-NLS-1$

	public static final String REPOSITORY_INTEGRATION_DEPLOY_PATH = IRepositoryPaths.REGISTRY_DEPLOY_PATH
			+ "/IntegrationServices"; //$NON-NLS-1$

	public static final String EXTENSION_ROUTE = ".routes"; //$NON-NLS-1$
	public static final String EXTENSION_WS = ".ws"; //$NON-NLS-1$
	public static final String PROPERTY_REPOSITORY_LOCATION = "repository-location"; //$NON-NLS-1$
	public static final String PROPERTY_EXPOSED_AT = "exposed-at"; //$NON-NLS-1$

}
