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
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.model.RouteDefinition;

/**
 * Servlet implementation class StatusServlet
 */
public class StatusServlet extends ControlServlet {
	private static final String CAMEL_ROUTES = "Camel Routes:"; //$NON-NLS-1$
	private static final String CONSUMER_NAME = "Consumer Name:"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;

	/**
	 * @see ControlServlet#ControlServlet()
	 */
	public StatusServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		beforeGet(request, response);

		PrintWriter writer = response.getWriter();
		response.setContentType("text/html"); //$NON-NLS-1$

		writer.println("<table border='1'>"); //$NON-NLS-1$
		writer.println("<tr><td><label>" //$NON-NLS-1$
				+ CONSUMER_NAME
				+ "</label></td><td><input type='text' readonly size='100' value='" //$NON-NLS-1$
				+ getConfigurationAgent().getConsumerName()
				+ "'/><br/></td></tr>"); //$NON-NLS-1$

		writer.println("<tr><td><label>" + CAMEL_ROUTES + "</label></td><td>"); //$NON-NLS-1$ //$NON-NLS-2$
		List<RouteDefinition> routeDefinitions = getConfigurationAgent()
				.getCamelContext().getRouteDefinitions();
		for (Iterator<RouteDefinition> iterator = routeDefinitions.iterator(); iterator
				.hasNext();) {
			RouteDefinition routeDefinition = iterator.next();
			writer.println("<input type='text' readonly size='100' value='" //$NON-NLS-1$
					+ routeDefinition.getId() + "'/><br/>"); //$NON-NLS-1$
		}
		writer.println("</td></tr>"); //$NON-NLS-1$

		// writer.println("<tr><td><label>Local File Location:</label></td><td><input type='text' readonly size='150' value='"
		// + getConfigurationAgent().getLocalConfigurationFile() +
		// "'/></td></tr>");
		// writer.println("<tr><td colspan='2'><label>Local File Content:</label></td></tr>");
		// writer.println("<tr><td colspan='2' bgcolor='white'>");
		// writer.println("<pre>");
		// InputStream configIn =
		// getServletContext().getResourceAsStream(ConfigurationCommands.LOCAL_CONFIGURATION_FILE);
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(configIn));
		// String s = null;
		// while ((s = reader.readLine()) != null) {
		// writer.println(StringEscapeUtils.escapeXml("		" + s));
		// }
		// writer.println("</pre>");
		// writer.println("</td></tr>");

		writer.println("</table>"); //$NON-NLS-1$

		writer.flush();
		writer.close();
	}

}
