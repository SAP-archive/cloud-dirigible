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

import java.util.Set;

import com.sap.dirigible.repository.ext.debug.BreakpointMetadata;

public interface IDebugCommands {
	public enum DebugCommand {
		CONTINUE, PAUSE, STEPOVER, STEPINTO, SKIP_ALL_BREAKPOINTS;
	}

	public void continueExecution();

	public void pauseExecution();

	public boolean isExecuting();

	public void resumeExecution();

	public void stepOver();

	public void stepInto();

	public void addBreakpoint(BreakpointMetadata breakpoint);

	public void clearBreakpoint(BreakpointMetadata breakpoint);

	public void clearAllBreakpoints();

	public void clearAllBreakpoints(String path);

	public Set<BreakpointMetadata> getBreakpoints();

	public void skipAllBreakpoints();

	public DebugCommand getCommand();
}
