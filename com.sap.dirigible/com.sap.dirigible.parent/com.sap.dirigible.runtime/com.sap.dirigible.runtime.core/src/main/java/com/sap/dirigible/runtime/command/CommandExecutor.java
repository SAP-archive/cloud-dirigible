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

package com.sap.dirigible.runtime.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.command.Piper;
import com.sap.dirigible.repository.ext.command.ProcessUtils;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;

public class CommandExecutor extends AbstractScriptExecutor {

	private static final String COMMAND_MODULE_NAME_CANNOT_BE_NULL = Messages
			.getString("CommandExecutor.COMMAND_MODULE_NAME_CANNOT_BE_NULL"); //$NON-NLS-1$
	
	private static final Logger logger = Logger.getLogger(CommandExecutor.class);

	private static final String COMMAND_EXTENSION = ".command";

	private IRepository repository;
	private String[] rootPaths;

	public CommandExecutor(IRepository repository, String... rootPaths) {
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
			Object input, String module) throws IOException {

		logger.debug("entering: executeServiceModule()"); //$NON-NLS-1$
		logger.debug("module=" + module); //$NON-NLS-1$
		
		if (module == null) {
			throw new IOException(COMMAND_MODULE_NAME_CANNOT_BE_NULL);
		}
		
//		String commandSource = new String(retrieveModule(repository, module, COMMAND_EXTENSION, rootPaths));
		String commandSource = new String(retrieveModule(repository, module, "", rootPaths));
		
		JsonParser parser = new JsonParser();
		JsonObject commandObject = (JsonObject) parser.parse(commandSource);
		
		
		String[] args = ProcessUtils.translateCommandline(commandObject.get("command").getAsString());
		ProcessBuilder processBuilder = ProcessUtils.createProcess(args);
		
		ProcessUtils.addEnvironmentVariables(processBuilder, null);
		ProcessUtils.removeEnvironmentVariables(processBuilder, null);
		//processBuilder.directory(new File(workingDirectory));
		processBuilder.redirectErrorStream(true);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Process process = ProcessUtils.startProcess(args, processBuilder);
		Piper pipe = new Piper(process.getInputStream(), out);
        new Thread(pipe).start();
        try {
			//process.waitFor();
        	
        	int i=0;
            boolean deadYet = false;
            do {
                Thread.sleep(1000);
                try {
                    process.exitValue();
                    deadYet = true;
                } catch (IllegalThreadStateException e) {
                    if (++i >= 5) {
                    	process.destroy();
                    	throw new RuntimeException("Exeeds timeout - 5s");
                    }
                }
            } while (!deadYet);
            
		} catch (Exception e) {
			logger.error(e.getMessage());
			return e.getMessage();
		}
		String result = new String(out.toByteArray());
		
		response.getWriter().write(result);
		response.getWriter().flush();
		response.getWriter().close();
		
		logger.debug("exiting: executeServiceModule()");
		return result;
	}

	protected void beforeExecution(HttpServletRequest request, HttpServletResponse response,
			String module, Context context) {
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name, Object value) {
		if (scope instanceof ScriptableObject) {
			ScriptableObject local = (ScriptableObject) scope;
			local.put(name, local, value);
		}
	}

}
