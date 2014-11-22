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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import com.sap.dirigible.repository.ext.debug.BreakpointMetadata;

public class DebuggerActionManager {

	private static final String DEBUGGER_ACTION_MANAGER = "debugger.action.manager";

	private String sessionId;

	private Set<BreakpointMetadata> breakpoints = new TreeSet<BreakpointMetadata>();

	private Map<String, DebuggerActionCommander> commanders = Collections
			.synchronizedMap(new HashMap<String, DebuggerActionCommander>());

	public static DebuggerActionManager getInstance(HttpSession session) {
		DebuggerActionManager debuggerActionManager = (DebuggerActionManager) session
				.getAttribute(DEBUGGER_ACTION_MANAGER);
		if (debuggerActionManager == null) {
			debuggerActionManager = new DebuggerActionManager(session.getId());
			session.setAttribute(DEBUGGER_ACTION_MANAGER, debuggerActionManager);
		}
		return debuggerActionManager;
	}

	private DebuggerActionManager(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void addCommander(DebuggerActionCommander commander) {
		commanders.put(commander.getExecutionId(), commander);
	}

	public void removeCommander(DebuggerActionCommander commander) {
		commanders.remove(commander.getExecutionId());
	}

	public DebuggerActionCommander getCommander(String executionId) {
		return commanders.get(executionId);
	}

	public Map<String, DebuggerActionCommander> getCommanders() {
		return commanders;
	}

	public Set<BreakpointMetadata> getBreakpoints() {
		return breakpoints;
	}

}
