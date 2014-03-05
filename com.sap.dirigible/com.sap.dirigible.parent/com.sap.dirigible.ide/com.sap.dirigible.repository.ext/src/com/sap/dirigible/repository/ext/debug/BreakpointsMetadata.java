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

package com.sap.dirigible.repository.ext.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BreakpointsMetadata extends DebugSessionMetadata {

	private Set<BreakpointMetadata> breakpointsList;

	public BreakpointsMetadata(String sessionId, String executionId, String userId) {
		super(sessionId, executionId, userId);
	}

	public BreakpointsMetadata(String sessionId, String executionId, String userId,
			Set<BreakpointMetadata> breakpointsList) {
		super(sessionId, executionId, userId);
		this.breakpointsList = breakpointsList;
	}

	public Set<BreakpointMetadata> getBreakpoints() {
		return breakpointsList;
	}

	public void setBreakpointsList(Set<BreakpointMetadata> breakpointsList) {
		this.breakpointsList = breakpointsList;
	}

	public int[] getBreakpoints(String fullPath) {
		List<BreakpointMetadata> list = new ArrayList<BreakpointMetadata>();
		for (BreakpointMetadata breakpoint : getBreakpoints()) {
			if (breakpoint.getFullPath().equals(fullPath)) {
				list.add(breakpoint);
			}
		}

		int breakpoints[] = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			breakpoints[i] = list.get(i).getRow();
		}
		return breakpoints;
	}

	@Override
	public String toString() {
		return Arrays.toString((breakpointsList.toArray(new BreakpointMetadata[breakpointsList
				.size()])));
	}
}
