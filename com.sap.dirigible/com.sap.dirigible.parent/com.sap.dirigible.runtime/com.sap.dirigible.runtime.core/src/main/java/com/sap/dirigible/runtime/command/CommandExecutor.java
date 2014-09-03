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
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

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
		
		String commandSource = new String(retrieveModule(repository, module, "", rootPaths).getContent());
		
		CommandData commandData;
		try {
			commandData = CommandDataParser.parseCommandData(commandSource);
		} catch (IllegalArgumentException e2) {
			logger.error(e2.getMessage());
			return e2.getMessage();
		}
		
		String commandLine = commandData.getTargetCommand().getCommand(); 
		
		String[] args = null;
		try {
			args = ProcessUtils.translateCommandline(commandLine);
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return e1.getMessage();
		}
		
		logger.debug("executing command=" + commandLine); //$NON-NLS-1$
		
		ByteArrayOutputStream out;
		try {
			ProcessBuilder processBuilder = ProcessUtils.createProcess(args);
			
			ProcessUtils.addEnvironmentVariables(processBuilder, commandData.getEnvAdd());
			ProcessUtils.removeEnvironmentVariables(processBuilder, commandData.getEnvRemove());
			processBuilder.directory(new File(commandData.getWorkDir()));
			processBuilder.redirectErrorStream(true);
			
			out = new ByteArrayOutputStream();
			Process process = ProcessUtils.startProcess(args, processBuilder);
			Piper pipe = new Piper(process.getInputStream(), out);
			new Thread(pipe).start();
			try {
				//process.waitFor();
				
				int i=0;
			    boolean deadYet = false;
			    do {
			        Thread.sleep(ProcessUtils.DEFAULT_WAIT_TIME);
			        try {
			            process.exitValue();
			            deadYet = true;
			        } catch (IllegalThreadStateException e) {
			            if (++i >= ProcessUtils.DEFAULT_LOOP_COUNT) {
			            	process.destroy();
			            	throw new RuntimeException("Exeeds timeout - " + ((ProcessUtils.DEFAULT_WAIT_TIME/1000) * ProcessUtils.DEFAULT_LOOP_COUNT));
			            }
			        }
			    } while (!deadYet);
			    
			} catch (Exception e) {
				logger.error(e.getMessage());
				return e.getMessage();
			}
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
