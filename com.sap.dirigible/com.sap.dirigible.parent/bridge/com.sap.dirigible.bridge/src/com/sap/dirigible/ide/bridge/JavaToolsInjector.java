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

package com.sap.dirigible.ide.bridge;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaToolsInjector implements Injector {
	
	public static final String JAVA_TOOLS_COMPILER = "JAVA_TOOLS_COMPILER"; //$NON-NLS-1$
	
	private static final Logger logger = LoggerFactory.getLogger(JavaToolsInjector.class);
	
	/* (non-Javadoc)
	 * @see com.sap.dirigible.ide.bridge.Injector#inject(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void inject(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JavaCompiler javaCompiler = (JavaCompiler) req.getSession().getAttribute(JAVA_TOOLS_COMPILER);
		if (javaCompiler == null) {
			javaCompiler = ToolProvider.getSystemJavaCompiler();
			req.getSession().setAttribute(JAVA_TOOLS_COMPILER, javaCompiler);
		}
	}
	
}
