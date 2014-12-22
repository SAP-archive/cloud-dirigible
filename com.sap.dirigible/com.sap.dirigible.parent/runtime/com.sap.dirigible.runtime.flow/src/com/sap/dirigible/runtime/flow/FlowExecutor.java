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

package com.sap.dirigible.runtime.flow;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;
import com.sap.dirigible.runtime.utils.EngineUtils;

public class FlowExecutor extends AbstractScriptExecutor {
	
	private static final Logger logger = Logger.getLogger(FlowExecutor.class);

	private IRepository repository;
	private String[] rootPaths;
	
	private Gson gson = new Gson();

	public FlowExecutor(IRepository repository, String... rootPaths) {
		super();
		logger.debug("entering: constructor()");
		this.repository = repository;
		this.rootPaths = rootPaths;
		if (this.rootPaths == null || this.rootPaths.length == 0) {
			this.rootPaths = new String[] { null, null };
		}
		logger.debug("exiting: constructor()");
	}

	@Override
	public Object executeServiceModule(HttpServletRequest request, HttpServletResponse response,
			Object input, String module, Map<Object, Object> executionContext) throws IOException {

		logger.debug("entering: executeServiceModule()"); //$NON-NLS-1$
		logger.debug("module=" + module); //$NON-NLS-1$
		
		if (module == null) {
			throw new IOException("Flow module cannot be null");
		}
		
		String result = null; 
		String flowSource = new String(retrieveModule(repository, module, "", rootPaths).getContent());
		
		Flow flow = gson.fromJson(flowSource, Flow.class);
		
		Object inputOutput = null;
		
		inputOutput = processFlow(request, response, module, executionContext,
				flow, inputOutput);

		result = (inputOutput != null) ? inputOutput.toString() : "";
		
		logger.debug("exiting: executeServiceModule()");
		return result;
	}

	private Object processFlow(HttpServletRequest request,
			HttpServletResponse response, String module,
			Map<Object, Object> executionContext, Flow flow, Object inputOutput)
			throws IOException {
		executionContext.putAll(flow.getProperties());
		
		// TODO make extension point
		for (FlowStep flowStep : flow.getSteps()) {
			try {
				inputOutput = executeByEngineType(request, response, module,
						executionContext, flow, inputOutput, flowStep);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}
		return inputOutput;
	}

	private Object executeByEngineType(HttpServletRequest request,
			HttpServletResponse response, String module,
			Map<Object, Object> executionContext, Flow flow,
			Object inputOutput, FlowStep flowStep) throws IOException {
		if (ICommonConstants.ENGINE_TYPE.JAVASCRIPT.equalsIgnoreCase(flowStep.getType())) {
			inputOutput = EngineUtils.executeJavascript(request, response,
					executionContext, inputOutput, flowStep.getModule());
//				} else if (ICommonConstants.ENGINE_TYPE.JAVA.equalsIgnoreCase(flowStep.getType())) {
//					inputOutput = EngineUtils.executeJava(request, response,
//						executionContext, inputOutput, flowStep);
		} else if (ICommonConstants.ENGINE_TYPE.COMMAND.equalsIgnoreCase(flowStep.getType())) {
			inputOutput = EngineUtils.executeCommand(request, response, executionContext,
					inputOutput, flowStep.getModule());
		} else if (ICommonConstants.ENGINE_TYPE.CONDITION.equalsIgnoreCase(flowStep.getType())) {
			
			FlowCase[] cases = flowStep.getCases();
			for (FlowCase flowCase : cases) {
				Object value = executionContext.get(flowCase.getKey());
				if (value != null
						&& value.equals(flowCase.getValue())) {
					processFlow(request, response, module, executionContext, flowCase.getFlow(), inputOutput);
					break;
				}
			}
			
		} else if (ICommonConstants.ENGINE_TYPE.FLOW.equalsIgnoreCase(flowStep.getType())) {
			inputOutput = EngineUtils.executeFlow(request, response, executionContext,
					inputOutput, flowStep.getModule());
		} else { // groovy etc...
			throw new IllegalArgumentException(String.format("Unknown execution type [%s] of step %s in flow %s at %s", 
					flowStep.getType(), flowStep.getName(), flow.getName(), module));
		}
		return inputOutput;
	}

	

	@Override
	protected void registerDefaultVariable(Object scope, String name,
			Object value) {
		// do nothing
	}

}
