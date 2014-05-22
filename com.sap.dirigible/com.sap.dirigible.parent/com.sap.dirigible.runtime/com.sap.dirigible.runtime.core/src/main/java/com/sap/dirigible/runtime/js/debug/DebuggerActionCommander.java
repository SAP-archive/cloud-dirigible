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

import java.util.Iterator;
import java.util.Set;

import com.sap.dirigible.repository.ext.debug.BreakpointMetadata;

public class DebuggerActionCommander implements IDebugCommands {

	private boolean executing = false;
	private DebugCommand currentCommand;

	private JavaScriptDebugFrame javaScriptDebugFrame;

	private JavaScriptDebugger javaScriptDebugger;

	private String executionId;
	
	private String userId;
	
	private DebuggerActionManager debuggerActionManager;

	public DebuggerActionCommander(DebuggerActionManager debuggerActionManager, String executionId, String userId) {
		this.debuggerActionManager = debuggerActionManager;
		this.executionId = executionId;
		this.userId = userId;
		
		this.debuggerActionManager.addCommander(this);
		init();
	}

	public void init() {
		currentCommand = null;
		executing = false;
	}

	@Override
	public void continueExecution() {
		currentCommand = DebugCommand.CONTINUE;
	}

	@Override
	public void pauseExecution() {
		executing = false;
	}

	@Override
	public boolean isExecuting() {
		return executing;
	}

	@Override
	public void resumeExecution() {
		executing = true;
	}

	@Override
	public void stepOver() {
		currentCommand = DebugCommand.STEPOVER;
	}

	@Override
	public void stepInto() {
		currentCommand = DebugCommand.STEPINTO;
	}

	@Override
	public void addBreakpoint(BreakpointMetadata breakpoint) {
		getDebuggerActionManager().getBreakpoints().add(breakpoint);
	}

	@Override
	public void clearBreakpoint(BreakpointMetadata breakpoint) {
		getDebuggerActionManager().getBreakpoints().remove(breakpoint);
	}

	@Override
	public void clearAllBreakpoints() {
		getDebuggerActionManager().getBreakpoints().clear();
	}

	@Override
	public void clearAllBreakpoints(String path) {
		Iterator<BreakpointMetadata> iterator = getDebuggerActionManager().getBreakpoints().iterator();
		while (iterator.hasNext()) {
			BreakpointMetadata breakpoint = iterator.next();
			if (breakpoint.getFullPath().equals(path)) {
				iterator.remove();
			}
		}
	}

	@Override
	public Set<BreakpointMetadata> getBreakpoints() {
		return getDebuggerActionManager().getBreakpoints();
	}

	@Override
	public void skipAllBreakpoints() {
		currentCommand = DebugCommand.SKIPALLBREAKPOINTS;
	}

	@Override
	public DebugCommand getCommand() {
		return currentCommand;
	}

	public DebuggerActionManager getDebuggerActionManager() {
		return debuggerActionManager;
	}
	
	public JavaScriptDebugFrame getJavaScriptDebugFrame() {
		return javaScriptDebugFrame;
	}

	public void setJavaScriptDebugFrame(JavaScriptDebugFrame javaScriptDebugFrame) {
		this.javaScriptDebugFrame = javaScriptDebugFrame;
	}
	
	public JavaScriptDebugger getJavaScriptDebugger() {
		return javaScriptDebugger;
	}

	public void setJavaScriptDebugger(JavaScriptDebugger javaScriptDebugger) {
		this.javaScriptDebugger = javaScriptDebugger;
	}

	public void clean() {
		this.javaScriptDebugFrame = null;
		this.javaScriptDebugger = null;
		this.debuggerActionManager.removeCommander(this);
	}
	
	public String getSessionId() {
		return getDebuggerActionManager().getSessionId();
	}
	
	public String getExecutionId() {
		return this.executionId;
	}
	
	public String getUserId() {
		return userId;
	}

	
}
