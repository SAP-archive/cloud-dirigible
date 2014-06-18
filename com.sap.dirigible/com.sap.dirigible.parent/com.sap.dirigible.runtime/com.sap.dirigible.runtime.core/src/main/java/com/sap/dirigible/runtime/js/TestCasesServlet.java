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

package com.sap.dirigible.runtime.js;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.runtime.common.ICommonConstants;
import com.sap.dirigible.runtime.filter.SandboxFilter;
import com.sap.dirigible.runtime.registry.AbstractRegistryServlet;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public class TestCasesServlet extends JavaScriptServlet {

	private static final long serialVersionUID = 1274655492926255449L;

	public static final String TEST_CASES = "/TestCases"; //$NON-NLS-1$  

	public static final String REGISTRY_TESTS_DEPLOY_PATH = AbstractRegistryServlet.REGISTRY_DEPLOY_PATH
			+ TEST_CASES; //$NON-NLS-1$

	@Override
	protected String getScriptingRegistryPath(HttpServletRequest request) {
		if (request.getAttribute(SandboxFilter.SANDBOX_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.SANDBOX_CONTEXT)) {
			return AbstractRegistryServlet.SANDBOX_DEPLOY_PATH + ICommonConstants.SEPARATOR
					+ RepositoryFacade.getUser(request) + TEST_CASES;
		}
		return REGISTRY_TESTS_DEPLOY_PATH;
	}

	@Override
	protected JavaScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		JavaScriptExecutor executor = new JavaScriptExecutor(getRepository(request),
				getScriptingRegistryPath(request), super.getScriptingRegistryPath(request),
				REGISTRY_SCRIPTING_DEPLOY_PATH, REGISTRY_TESTS_DEPLOY_PATH);
		return executor;
	}

}
