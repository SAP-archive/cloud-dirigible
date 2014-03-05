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

import java.util.Arrays;
import java.util.List;

public class VariableValuesMetadata extends DebugSessionMetadata {
	
	private List<VariableValue> variableValueList;
	
	public VariableValuesMetadata(String sessionId, String executionId, String userId) {
		super(sessionId, executionId, userId);
	}

	public VariableValuesMetadata(String sessionId, String executionId, String userId, List<VariableValue> variableValueList) {
		super(sessionId, executionId, userId);
		this.variableValueList = variableValueList;
	}

	public List<VariableValue> getVariableValueList() {
		return variableValueList;
	}

	public void setVariableValueList(List<VariableValue> variableValueList) {
		this.variableValueList = variableValueList;
	}

	@Override
	public String toString() {
		return Arrays.toString((variableValueList.toArray(new VariableValue[variableValueList
				.size()])));
	}
}
