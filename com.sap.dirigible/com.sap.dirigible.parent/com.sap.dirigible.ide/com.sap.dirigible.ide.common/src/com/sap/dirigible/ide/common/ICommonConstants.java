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

public interface ICommonConstants {

	public interface ARTIFACT_EXTENSION {
		public static final String JAVASCRIPT= ".js"; //$NON-NLS-1$
		public static final String RUBY = ".rb"; //$NON-NLS-1$
		public static final String GROOVY = ".groovy"; //$NON-NLS-1$
		public static final String JAVA = ".java"; //$NON-NLS-1$
		public static final String COMMAND = ".command"; //$NON-NLS-1$
		public static final String EXTENSION_POINT = ".extensionpoint"; //$NON-NLS-1$
		public static final String EXTENSION = ".extension"; //$NON-NLS-1$
		public static final String SECURITY = ".access"; //$NON-NLS-1$
	}
	
	public interface ARTIFACT_TYPE {
		public final static String DATA_STRUCTURES = "DataStructures"; //$NON-NLS-1$
		public final static String INTEGRATION_SERVICES = "IntegrationServices"; //$NON-NLS-1$
		public final static String SCRIPTING_SERVICES = "ScriptingServices"; //$NON-NLS-1$
		public final static String TEST_CASES = "TestCases"; //$NON-NLS-1$
		public final static String WEB_CONTENT = "WebContent"; //$NON-NLS-1$
		public final static String SECURITY_CONSTRAINTS = "SecurityConstraints"; //$NON-NLS-1$
		public final static String WIKI_CONTENT = "WikiContent"; //$NON-NLS-1$
		public final static String EXTENSION_DEFINITIONS = "ExtensionDefinitions"; //$NON-NLS-1$
		public static final String CONFIGURATION_SETTINGS = "ConfigurationSettings";
	}

}
