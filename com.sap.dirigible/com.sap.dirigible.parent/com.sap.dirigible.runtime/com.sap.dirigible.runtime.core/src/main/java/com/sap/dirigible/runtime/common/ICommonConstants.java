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

package com.sap.dirigible.runtime.common;

import com.sap.dirigible.repository.api.IRepository;

public interface ICommonConstants {
	
	public static final String SEPARATOR = IRepository.SEPARATOR; //$NON-NLS-1$

	public interface ARTIFACT_TYPE {
		public final static String DATA_STRUCTURES = "DataStructures"; //$NON-NLS-1$
		public final static String INTEGRATION_SERVICES = "IntegrationServices"; //$NON-NLS-1$
		public final static String SCRIPTING_SERVICES = "ScriptingServices"; //$NON-NLS-1$
		public final static String TEST_CASES = "TestCases"; //$NON-NLS-1$
		public final static String WEB_CONTENT = "WebContent"; //$NON-NLS-1$
		public final static String SECURITY_CONSTRAINTS = "SecurityConstraints"; //$NON-NLS-1$
	}
}
