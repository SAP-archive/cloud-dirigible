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

package com.sap.dirigible.ide.common;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;

public class CommonParameters {

	public static final String EMPTY_STRING = "";  //$NON-NLS-1$
	public static final String DIRIGIBLE_PRODUCT_NAME = "Dirigible"; //$NON-NLS-1$
	public static final String DIRIGIBLE_PRODUCT_VERSION = "1.2.0-beta"; //$NON-NLS-1$

	public static final String SEPARATOR = "/"; //$NON-NLS-1$
	public static final String DEBUG_SEPARATOR = ":"; //$NON-NLS-1$
	
	public static final String DB_DIRIGIBLE_ROOT = "/db/dirigible/"; //$NON-NLS-1$
	public static final String DB_DIRIGIBLE_REGISTRY = DB_DIRIGIBLE_ROOT
			+ "registry/public/"; //$NON-NLS-1$
	public static final String DB_DIRIGIBLE_SANDBOX = DB_DIRIGIBLE_ROOT
			+ "sandbox/"; //$NON-NLS-1$
	public static final String DB_DIRIGIBLE_USERS = DB_DIRIGIBLE_ROOT
			+ "users/"; //$NON-NLS-1$
	public static final String DB_DIRIGIBLE_TEMPLATES = DB_DIRIGIBLE_ROOT
			+ "templates/"; //$NON-NLS-1$
	public static final String DB_DIRIGIBLE_TEMPLATES_PROJECTS = DB_DIRIGIBLE_TEMPLATES
			+ "projects/"; //$NON-NLS-1$

	public static final String WORKSPACE_FOLDER_NAME = "workspace"; //$NON-NLS-1$

	public static String getWorkspace() {
		return DB_DIRIGIBLE_USERS+ getUserName() + SEPARATOR + WORKSPACE_FOLDER_NAME;
	}
	
	public static final String DB_DIRIGIBLE_CONFIGURATIONS = DB_DIRIGIBLE_REGISTRY
			+ "ConfigurationSettings"; //$NON-NLS-1$

	public static final String REGISTRY = "/registry"; //$NON-NLS-1$
	public static final String SANDBOX = "/sandbox"; //$NON-NLS-1$

	public static final String RUNTIME_URL = "runtimeUrl"; //$NON-NLS-1$
	public static final String RUNTIME_URL_DEFAULT = "/dirigible"; //$NON-NLS-1$

	public static final String WEB_CONTENT_FOLDER = "WebContent"; //$NON-NLS-1$
	public static final String WEB_CONTENT_REGISTRY_PUBLISH_LOCATION = DB_DIRIGIBLE_REGISTRY
			+ WEB_CONTENT_FOLDER;
		
	public static String getWebContentSandbox() {
		return DB_DIRIGIBLE_SANDBOX + getUserName() + SEPARATOR + WEB_CONTENT_FOLDER;
	}
	
	public static final String WIKI_CONTENT_FOLDER = "WikiContent"; //$NON-NLS-1$
	public static final String WIKI_CONTENT_REGISTRY_PUBLISH_LOCATION = DB_DIRIGIBLE_REGISTRY
			+ WIKI_CONTENT_FOLDER;

	public static String getWikiContentSandbox() {
		return DB_DIRIGIBLE_SANDBOX + getUserName() + SEPARATOR + WIKI_CONTENT_FOLDER;
	}
	
	
	public static final String SCRIPTING_CONTENT_FOLDER = ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	public static final String SCRIPTING_REGISTRY_PUBLISH_LOCATION = CommonParameters.DB_DIRIGIBLE_REGISTRY
			+ SCRIPTING_CONTENT_FOLDER;
	
	public static String getScriptingContentSandbox() {
		return DB_DIRIGIBLE_SANDBOX + getUserName() + SEPARATOR + SCRIPTING_CONTENT_FOLDER;
	}

	public static final String TESTS_CONTENT_FOLDER = ICommonConstants.ARTIFACT_TYPE.TEST_CASES;
	public static final String TESTS_REGISTRY_PUBLISH_LOCATION = CommonParameters.DB_DIRIGIBLE_REGISTRY
			+ TESTS_CONTENT_FOLDER;
	
	public static String getTestingContentSandbox() {
		return DB_DIRIGIBLE_SANDBOX + getUserName() + SEPARATOR + TESTS_CONTENT_FOLDER;
	}
	
	public static final String EXTENSION_CONTENT_FOLDER = ICommonConstants.ARTIFACT_TYPE.EXTENSION_DEFINITIONS;
	public static final String EXTENSION_REGISTRY_PUBLISH_LOCATION = CommonParameters.DB_DIRIGIBLE_REGISTRY
			+ EXTENSION_CONTENT_FOLDER;
	
	public static String getExtensionContentSandbox() {
		return DB_DIRIGIBLE_SANDBOX + getUserName() + SEPARATOR + EXTENSION_CONTENT_FOLDER;
	}
	
	
	
	//github url
	public static final String GIT_REPOSITORY_URL = "https://github.com/SAP/cloud-dirigible-samples.git";
	public static final String WEB_CONTENT_CONTAINER_MAPPING = "/web"; //$NON-NLS-1$
	public static final String JAVASCRIPT_CONTAINER_MAPPING = "/js"; //$NON-NLS-1$
	public static final String JAVASCRIPT_DEBUG_CONTAINER_MAPPING = "/js-debug"; //$NON-NLS-1$
	public static final String RUBY_CONTAINER_MAPPING = "/rb"; //$NON-NLS-1$
	public static final String GROOVY_CONTAINER_MAPPING = "/groovy"; //$NON-NLS-1$
	public static final String TEST_CASES_CONTAINER_MAPPING = "/test"; //$NON-NLS-1$
	public static final String WIKI_CONTENT_CONTAINER_MAPPING = "/wiki"; //$NON-NLS-1$
	
	public static final String WEB_CONTENT_SANDBOX_MAPPING = "/web-sandbox"; //$NON-NLS-1$
	public static final String JAVASCRIPT_SANDBOX_MAPPING = "/js-sandbox"; //$NON-NLS-1$
	public static final String RUBY_SANDBOX_MAPPING = "/rb-sandbox"; //$NON-NLS-1$
	public static final String GROOVY_SANDBOX_MAPPING = "/groovy-sandbox"; //$NON-NLS-1$
	public static final String TEST_CASES_SANDBOX_MAPPING = "/test-sandbox"; //$NON-NLS-1$
	public static final String WIKI_CONTENT_SANDBOX_MAPPING = "/wiki-sandbox"; //$NON-NLS-1$

	public static final String JAVASCRIPT_SERVICE_EXTENSION = ICommonConstants.ARTIFACT_EXTENSION.JAVASCRIPT; //$NON-NLS-1$
	public static final String RUBY_SERVICE_EXTENSION = ICommonConstants.ARTIFACT_EXTENSION.RUBY; //$NON-NLS-1$
	public static final String GROOVY_SERVICE_EXTENSION = ICommonConstants.ARTIFACT_EXTENSION.GROOVY; //$NON-NLS-1$
	public static final String EXTENSION_POINT_EXTENSION = ICommonConstants.ARTIFACT_EXTENSION.EXTENSION_POINT; //$NON-NLS-1$
	public static final String EXTENSION_EXTENSION = ICommonConstants.ARTIFACT_EXTENSION.EXTENSION; //$NON-NLS-1$
	public static final String GUEST_USER = "guest"; //$NON-NLS-1$

	public static final String CONTENT_EXPORT = "/content-export/"; //$NON-NLS-1$

	public static final String HC_LOCAL_HTTP_PORT = "HC_LOCAL_HTTP_PORT"; //$NON-NLS-1$
	public static final String HC_APPLICATION_URL = "HC_APPLICATION_URL"; //$NON-NLS-1$
	public static final String HC_APPLICATION = "HC_APPLICATION"; //$NON-NLS-1$
	public static final String HC_ACCOUNT = "HC_ACCOUNT"; //$NON-NLS-1$
	public static final String HC_REGION = "HC_REGION"; //$NON-NLS-1$
	public static final String HC_HOST = "HC_HOST"; //$NON-NLS-1$

	// Workbench Parameters
	public static final String PARAMETER_PERSPECTIVE = "perspective"; //$NON-NLS-1$
	public static final String PARAMETER_PROJECT = "project"; //$NON-NLS-1$
	public static final String PARAMETER_PACKAGE = "package"; //$NON-NLS-1$
	
	
	public static final String DATABASE_PRODUCT_NAME = "DATABASE_PRODUCT_NAME"; //$NON-NLS-1$
	public static final String DATABASE_PRODUCT_VERSION = "DATABASE_PRODUCT_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_MINOR_VERSION = "DATABASE_MINOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_MAJOR_VERSION = "DATABASE_MAJOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_DRIVER_NAME = "DATABASE_DRIVER_NAME"; //$NON-NLS-1$
	public static final String DATABASE_DRIVER_MINOR_VERSION = "DATABASE_DRIVER_MINOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_DRIVER_MAJOR_VERSION = "DATABASE_DRIVER_MAJOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_CONNECTION_CLASS_NAME = "DATABASE_CONNECTION_CLASS_NAME"; //$NON-NLS-1$
	
	
	
	public static final String ROLE_OPERATOR = "Operator"; //$NON-NLS-1$
	public static final String ROLE_DEVELOPER = "Developer"; //$NON-NLS-1$
	
	public static final String DIRIGIBLE_DEBUGGER_BRIDGE = "dirigible.debugger.bridge"; //$NON-NLS-1$
	public static final String DIRIGIBLE_RUNTIME_BRIDGE = "dirigible.runtime.bridge"; //$NON-NLS-1$

	
	public static final String SYSTEM_TABLE = "SYSTEM TABLE"; //$NON-NLS-1$
	public static final String LOCAL_TEMPORARY = "LOCAL TEMPORARY"; //$NON-NLS-1$
	public static final String GLOBAL_TEMPORARY = "GLOBAL TEMPORARY"; //$NON-NLS-1$
	public static final String SYNONYM = "SYNONYM"; //$NON-NLS-1$
	public static final String ALIAS = "ALIAS"; //$NON-NLS-1$
	public static final String VIEW = "VIEW"; //$NON-NLS-1$
	public static final String TABLE = "TABLE"; //$NON-NLS-1$
	
	public static final String[] TABLE_TYPES = { TABLE, VIEW, ALIAS, SYNONYM, GLOBAL_TEMPORARY, LOCAL_TEMPORARY,
			SYSTEM_TABLE };

	
//=====================================================================================================================================	
	
	
	
	

	public static String get(String name) {
		String parameter = (String) RWT.getRequest().getSession().getAttribute(name);
		return parameter;
	}
	
	public static void set(String name, String value) {
		RWT.getRequest().getSession().setAttribute(name, value);
	}

	public static String getRuntimeUrl() {
		String runtimeUrl = CommonParameters.get(CommonParameters.RUNTIME_URL);
		if (runtimeUrl == null) {
			runtimeUrl = CommonParameters.RUNTIME_URL_DEFAULT;
		}
		return runtimeUrl;
	}

	static {
		HttpServletRequest req = RWT.getRequest();
		String parameterHC_HOST = System.getProperty(HC_HOST);
		req.getSession().setAttribute(HC_HOST, parameterHC_HOST);
		String parameterHC_REGION = System.getProperty(HC_REGION);
		req.getSession().setAttribute(HC_REGION, parameterHC_REGION);
		String parameterHC_ACCOUNT = System.getProperty(HC_ACCOUNT);
		req.getSession().setAttribute(HC_ACCOUNT, parameterHC_ACCOUNT);
		String parameterHC_APPLICATION = System.getProperty(HC_APPLICATION);
		req.getSession().setAttribute(HC_APPLICATION, parameterHC_APPLICATION);
		String parameterHC_APPLICATION_URL = System
				.getProperty(HC_APPLICATION_URL);
		req.getSession().setAttribute(HC_APPLICATION_URL,
				parameterHC_APPLICATION_URL);
		String parameterHC_LOCAL_HTTP_PORT = System
				.getProperty(HC_LOCAL_HTTP_PORT);
		req.getSession().setAttribute(HC_LOCAL_HTTP_PORT,
				parameterHC_LOCAL_HTTP_PORT);
	}
	
	public static String getUserName() {
		String user = RWT.getRequest().getRemoteUser();
		if (user == null) {
			user = GUEST_USER;
		}
		return user;
	}
	
	public static String getDatabaseProductName() {
		return get(DATABASE_PRODUCT_NAME);
	}
	
	public static String getDatabaseProductVersion() {
		return get(DATABASE_PRODUCT_VERSION);
	}
		
	public static String getDriverName() {
		return get(DATABASE_DRIVER_NAME);
	}

	public static boolean isUserInRole(String role) {
		return RWT.getRequest().isUserInRole(role);
	}
	
	public static String getSessionId() {
		String sessionId = RWT.getRequest().getSession(true).getId();
		return sessionId;
	}
	
	}
