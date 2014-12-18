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

package com.sap.dirigible.runtime.esb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.runtime.agent.RuntimeBridgeUtils;
import com.sap.dirigible.runtime.logger.Logger;

public class MessagingServlet extends ControlServlet {

	private static final String RELOAD = Messages.getString("MessagingServlet.RELOAD"); //$NON-NLS-1$

	private static final String FORCE_RELOAD = Messages.getString("MessagingServlet.FORCE_RELOAD"); //$NON-NLS-1$

	private static final String DONE = Messages.getString("MessagingServlet.DONE"); //$NON-NLS-1$

	private static final String OK = Messages.getString("MessagingServlet.OK"); //$NON-NLS-1$

	private static final String FAILED = Messages.getString("MessagingServlet.FAILED"); //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(MessagingServlet.class.getCanonicalName());

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_RELOAD = "reload"; //$NON-NLS-1$
	private static String PARAMETER_PING = "ping"; //$NON-NLS-1$
	private static String PARAMETER_ROUTES = "routes"; //$NON-NLS-1$
	private static String PARAMETER_WS = "ws"; //$NON-NLS-1$

	/**
	 * @see ControlServlet#ControlServlet()
	 */
	public MessagingServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "doGet"); //$NON-NLS-1$

		beforeGet(request, response);

		PrintWriter writer = response.getWriter();
		response.setContentType("text/html"); //$NON-NLS-1$

		String reload = request.getParameter(PARAMETER_RELOAD);
		String ping = request.getParameter(PARAMETER_PING);
		String routes = request.getParameter(PARAMETER_ROUTES);
		String ws = request.getParameter(PARAMETER_WS);

		logger.info("Parameters: " + " reload: " + reload + " ping: " + ping //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ " routes: " + routes + " ws: " + ws); //$NON-NLS-1$ //$NON-NLS-2$

		processRequest(request, writer, reload, routes, ws);

		writer.flush();
		writer.close();

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "doGet"); //$NON-NLS-1$

	}

	private void processRequest(HttpServletRequest request, PrintWriter writer,
			String reload, String routes, String ws) throws IOException {
		
		String message = null;
		if (reload != null && Boolean.TRUE.toString().equalsIgnoreCase(reload)) { //$NON-NLS-1$
			message = forceReload(request);
		} else if (routes != null) {
			message  = pushRoutes(request, routes);
		} else if (ws != null) {
			message = pushWs(request, ws);
		} else {
			StringBuilder buff = new StringBuilder(); 
			buff.append("<table>") //$NON-NLS-1$
				.append("<tr>") //$NON-NLS-1$
				.append("<td>" //$NON-NLS-1$
					+ FORCE_RELOAD
					+ "</td><td><a href=\"/dirigible-esb/messaging?reload=true\">" //$NON-NLS-1$
					+ RELOAD + "</a></td>") //$NON-NLS-1$
					.append("</tr>") //$NON-NLS-1$
					.append("</table>"); //$NON-NLS-1$
			message = buff.toString();
		}
		writer.println(message);
		writer.flush();
	}

	private String pushWs(HttpServletRequest request, String ws) {
		try {
			String messageContent = RuntimeBridgeUtils.createApplyWsMessage(ws);
			RuntimeBridgeUtils.pushMessage(messageContent, request);
			return OK;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return FAILED + e.getMessage();
		}
	}

	private String pushRoutes(HttpServletRequest request, String routes) {
		try {
			String messageContent = RuntimeBridgeUtils.createApplyRoutesMessage(routes);
			RuntimeBridgeUtils.pushMessage(messageContent, request);
			return OK;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return FAILED + e.getMessage();
		}
		
	}

	private String forceReload(HttpServletRequest request)
			throws IOException {
		StringBuilder buff = new StringBuilder(); 
		buff.append(FORCE_RELOAD + "<br/>") //$NON-NLS-1$
			.append(getConfigurationAgent().forceLoadEndpoints(request))
			.append(DONE);
		return buff.toString();
	}

}
