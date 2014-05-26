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

package com.sap.dirigible.runtime.camel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.runtime.js.JavaScriptExecutor;
import com.sap.dirigible.runtime.scripting.AbstractScriptingServlet;

public class JavascriptBeanProcessor extends AbstractBeanProcessor {

	public Object doExecution(HttpServletRequest request,
			HttpServletResponse response, Object input, String module)
			throws ServletException, IOException {

		JavaScriptExecutor executor = new JavaScriptExecutor(
				getRepository(request),
				getScriptingRegistryPath(request), AbstractScriptingServlet.REGISTRY_SCRIPTING_DEPLOY_PATH);
		return executor.executeServiceModule(request, response, input, module);

	}

}
