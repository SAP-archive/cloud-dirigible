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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.google.gson.Gson;
import com.sap.dirigible.repository.ext.debug.DebugConstants;
import com.sap.dirigible.repository.ext.debug.DebugSessionMetadata;
import com.sap.dirigible.repository.ext.debug.DebugSessionsMetadata;
import com.sap.dirigible.repository.ext.debug.IDebugProtocol;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.task.IRunnableTask;
import com.sap.dirigible.runtime.task.TaskManagerShort;

public class DebugGlobalManager implements HttpSessionListener, PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(DebugGlobalManager.class);

	private static Map<String, DebuggerActionManager> debuggerActionManagers = Collections
			.synchronizedMap(new HashMap<String, DebuggerActionManager>());

	private IDebugProtocol debugProtocol;

	public DebugGlobalManager() {
		logger.debug("entering DebugHttpSessionListener.constructor");

		TaskManagerShort.getInstance().registerRunnableTask(new DebugBridgeRegister(this));

		logger.debug("assign the DebugHttpSessionListener as listener to the DebugBridge");

		logger.debug("exiting DebugHttpSessionListener.constructor");
	}

	class DebugBridgeRegister implements IRunnableTask {

		DebugGlobalManager debugGlobalManager;

		DebugBridgeRegister(DebugGlobalManager debugGlobalManager) {
			this.debugGlobalManager = debugGlobalManager;
		}

		@Override
		public String getName() {
			return "Debug Bridge Register";
		}

		@Override
		public void start() {
			if (debugProtocol == null) {
				debugProtocol = DebugProtocolUtils.lookupDebugProtocol();
				if (debugProtocol != null) {
					debugProtocol.addPropertyChangeListener(debugGlobalManager);
					TaskManagerShort.getInstance().unregisterRunnableTask(this);
					logger.info("DebugGlobalManager has been register to DebuggerBridge");
				}
			}
		}

	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.debug("entering DebugHttpSessionListener.sessionCreated() with sessionId: "
				+ se.getSession().getId());
		DebuggerActionManager debuggerActionManager = DebuggerActionManager.getInstance(se
				.getSession());
		debuggerActionManagers.put(se.getSession().getId(), debuggerActionManager);
		logger.debug("exiting DebugHttpSessionListener.sessionCreated()");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.debug("entering DebugHttpSessionListener.sessionDestroyed() with sessionId: "
				+ se.getSession().getId());
		debuggerActionManagers.remove(se.getSession().getId());
		logger.debug("exiting DebugHttpSessionListener.sessionDestroyed()");
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String commandId = event.getPropertyName();
		String clientId = (String) event.getOldValue();
		String commandBody = (String) event.getNewValue();
		logger.debug("DebugHttpSessionListener propertyChange() command: " + commandId
				+ ", clientId: " + clientId + ", body: " + commandBody);

		if (!"debug.global.manager".equals(clientId)) {
			return;
		}

		if (commandId.equals(DebugConstants.DEBUG_REFRESH)) {
			sendSessionsMetadata();
		}
	}

	private void sendSessionsMetadata() {
		DebugSessionsMetadata debugSessionsMetadata = new DebugSessionsMetadata();
		for (DebuggerActionManager debuggerActionManager : debuggerActionManagers.values()) {
			for (DebuggerActionCommander debuggerActionCommander : debuggerActionManager
					.getCommanders().values()) {
				DebugSessionMetadata debugSessionMetadata = new DebugSessionMetadata(
						debuggerActionCommander.getSessionId(),
						debuggerActionCommander.getExecutionId(),
						debuggerActionCommander.getUserId());
				debugSessionsMetadata.getDebugSessionsMetadata().add(debugSessionMetadata);
			}
		}
		Gson gson = new Gson();
		String sessionsJson = gson.toJson(debugSessionsMetadata);
		send(DebugConstants.VIEW_SESSIONS, sessionsJson);

	}

	public void send(String commandId, String commandBody) {
		logger.debug("JavaScriptDebugFrame send() command: " + commandId + ", body: " + commandBody);
		DebugProtocolUtils.send(debugProtocol, commandId, "debug.global.manager", commandBody);
	}

}
