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

package com.sap.dirigible.ide.debug.model;

import com.sap.dirigible.repository.ext.debug.BreakpointMetadata;
import com.sap.dirigible.repository.ext.debug.BreakpointsMetadata;
import com.sap.dirigible.repository.ext.debug.VariableValuesMetadata;

public class DebugModel {

	private IDebugController debugController;

	private VariableValuesMetadata variableValuesMetadata;
	
	private BreakpointsMetadata breakpointsMetadata;
	
	private BreakpointMetadata currentLineBreak;
	
	private String sessionId;
	
	private String executionId;
	
	private String userId;

	
	DebugModel(IDebugController debugController) {
		this.debugController = debugController;
	}

	public void setBreakpoint(String path, int row) {
		debugController.setBreakpoint(this, path, row);
	}

	public void clearBreakpoint(String path, int row) {
		debugController.clearBreakpoint(this, path, row);
	}

	public void clearAllBreakpoints() {
		debugController.clearAllBreakpoints(this);
	}

	public void clearAllBreakpoints(String path) {
		debugController.clearAllBreakpoints(this, path);
	}

	public VariableValuesMetadata getVariableValuesMetadata() {
		return variableValuesMetadata;
	}

	public void setVariableValuesMetadata(
			VariableValuesMetadata variableValuesMetadata) {
		this.variableValuesMetadata = variableValuesMetadata;
	}

	public BreakpointsMetadata getBreakpointsMetadata() {
		return breakpointsMetadata;
	}

	public void setBreakpointsMetadata(BreakpointsMetadata breakpointMetadata) {
		this.breakpointsMetadata = breakpointMetadata;
	}

	public BreakpointMetadata getCurrentLineBreak() {
		return currentLineBreak;
	}

	public void setCurrentLineBreak(BreakpointMetadata currentLineBreak) {
		this.currentLineBreak = currentLineBreak;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}
