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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.logging.Logger;

public class DebugModelFacade {
	
	private static final Logger logger = Logger.getLogger(DebugModelFacade.class); 
	
	private static final String ACTIVE_DEBUG_SESSION = "debug.session.active";
	
	private static DebugModelFacade debugModelFacade;
	
	private static Map<String, DebugModel> debugModels = Collections.synchronizedMap(new HashMap<String, DebugModel>());
	
	public static DebugModelFacade getInstance() {
		if (debugModelFacade == null) {
				debugModelFacade = new DebugModelFacade();
		}
		return debugModelFacade;
	}
	
	public DebugModel createDebugModel(String sessionId, String executionId, String userId, IDebugController debugController) {
		logger.debug("entering DebugModelFacade.createDebugModel() with sessionId:" + sessionId + ", executionId:" + executionId + ", userId:" + userId); 
		DebugModel debugModel = debugModels.get(executionId);
		if (debugModel == null) {
			debugModel = new DebugModel(debugController);
			debugModel.setSessionId(sessionId);
			debugModel.setExecutionId(executionId);
			debugModel.setUserId(userId);
			debugModels.put(executionId, debugModel);
			logger.debug("DebugModel created with sessionId: " + sessionId + ", executionId: " + executionId + ", userId: " + userId);
		}
		
		logger.debug("exiting DebugModelFacade.createDebugModel()");
		return debugModel;
	}
	
	public DebugModel getDebugModel(String executionId) {
		DebugModel debugModel = debugModels.get(executionId);
		if (debugModel == null) {
			logger.warn("Getting DebugModel with executionId: " + executionId + " failed - no such model exists");
		}
		return debugModel;
	}
	
	public void removeDebugModel(String executionId) {
		debugModels.remove(executionId);
		logger.debug("DebugModel with executionId: " + executionId + " removed");
	}

	public static Map<String, DebugModel> getDebugModels() {
		return debugModels;
	}
	
	public static DebugModel getActiveDebugModel() {
		DebugModel debugModel = (DebugModel) CommonParameters.getObject(ACTIVE_DEBUG_SESSION);
		if (debugModel == null) {
			logger.debug("Getting active DebugModel from session failed");
		}
		return debugModel;
	}
	
	public static void setActiveDebugModel(DebugModel debugModel) {
		CommonParameters.setObject(ACTIVE_DEBUG_SESSION, debugModel);
		logger.debug("Setting DebugModel in Session with sessionId: " + debugModel.getSessionId() + ", executionId: " + debugModel.getExecutionId() + ", userId: " + debugModel.getUserId());
	}
	
	public void clearDebugModels() {
		debugModels.clear();
		logger.debug("DebugModels cleared");
	}

}
