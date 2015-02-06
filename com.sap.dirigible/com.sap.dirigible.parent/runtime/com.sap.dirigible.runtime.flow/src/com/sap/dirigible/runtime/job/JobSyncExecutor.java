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

package com.sap.dirigible.runtime.job;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;

public class JobSyncExecutor extends AbstractScriptExecutor {
	
	private static final Logger logger = Logger.getLogger(JobSyncExecutor.class);

	private IRepository repository;
	private String[] rootPaths;
	
	private Gson gson = new Gson();

	public JobSyncExecutor(IRepository repository, String... rootPaths) {
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
			throw new IOException("Job module cannot be null");
		}
		
		String result = null; 
		String jobDefinition = new String(retrieveModule(repository, module, "", rootPaths).getContent());
		
		JsonObject jobDefinitionObject = JobParser.parseJob(jobDefinition);
		
		String jobName = jobDefinitionObject.get(JobParser.NODE_NAME).getAsString(); //$NON-NLS-1$
		String jobType = jobDefinitionObject.get(JobParser.NODE_TYPE).getAsString(); //$NON-NLS-1$
		String jobModule = jobDefinitionObject.get(JobParser.NODE_MODULE).getAsString(); //$NON-NLS-1$
		
		Object inputOutput = null;
		
		inputOutput = processJob(request, response, jobModule, executionContext,
				inputOutput, jobType, jobName);

		result = (inputOutput != null) ? inputOutput.toString() : "";
		
		logger.debug("exiting: executeServiceModule()");
		return result;
	}

	private Object processJob(HttpServletRequest request,
			HttpServletResponse response, String module,
			Map<Object, Object> executionContext, Object inputOutput, String jobType, String jobName)
			throws IOException {
		
		try {
			inputOutput = CronJob.executeByEngineType(request, response, module,
					executionContext, jobName, inputOutput, jobType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return inputOutput;
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name,
			Object value) {
		// do nothing
	}

}
