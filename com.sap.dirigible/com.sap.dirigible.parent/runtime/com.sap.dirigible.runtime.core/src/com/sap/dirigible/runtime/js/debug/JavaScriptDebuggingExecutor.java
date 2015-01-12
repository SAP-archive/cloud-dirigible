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

package com.sap.dirigible.runtime.js.debug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ErrorReporter;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.debug.IDebugProtocol;
import com.sap.dirigible.runtime.js.JavaScriptExecutor;
import com.sap.dirigible.runtime.logger.Logger;

public class JavaScriptDebuggingExecutor extends JavaScriptExecutor {

	private static final Logger logger = Logger.getLogger(JavaScriptDebuggingExecutor.class);

	private static final String JAVA_SCRIPT_DEBUGGER = "JavaScript Debugger";

	private JavaScriptDebuggingExecutor(IRepository repository, String rootPath,
			String secondaryRootPath) {
		super(repository, rootPath, secondaryRootPath);
	}

	private IDebugProtocol debugProtocol;

	public JavaScriptDebuggingExecutor(IRepository repository, String rootPath,
			String secondaryRootPath, IDebugProtocol debugProtocol) {
		super(repository, rootPath, secondaryRootPath);
		this.debugProtocol = debugProtocol;
	}

	protected void beforeExecution(HttpServletRequest request, HttpServletResponse response,
			String module, Context context) {
		logger.debug("entering JavaScriptDebuggingExecutor.beforeExecution()");
		ErrorReporter reporter = new InvocationErrorReporter();
		context.setErrorReporter(reporter);

		logger.debug("creating JavaScriptDebugger");
		JavaScriptDebugger debugger = new JavaScriptDebugger(debugProtocol, request);
		context.setDebugger(debugger, JAVA_SCRIPT_DEBUGGER);
		logger.debug("created JavaScriptDebugger");

		context.setGeneratingDebug(true);
		context.setOptimizationLevel(-1);
		logger.debug("exiting JavaScriptDebuggingExecutor.beforeExecution()");
	}

}
