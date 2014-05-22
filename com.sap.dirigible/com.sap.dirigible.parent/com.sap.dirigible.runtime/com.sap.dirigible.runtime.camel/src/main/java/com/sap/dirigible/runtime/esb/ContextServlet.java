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

/**
 * Servlet implementation class ContextServlet
 */
public class ContextServlet extends ControlServlet {
	private static final String BEAN_NAME = "Bean Name"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;

	/**
	 * @see ControlServlet#ControlServlet()
	 */
	public ContextServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		beforeGet(request, response);

		PrintWriter writer = response.getWriter();
		response.setContentType("text/html"); //$NON-NLS-1$

		writer.println("<table border='1'>"); //$NON-NLS-1$
		writer.println("<tr><th>" + BEAN_NAME + "</th><th>Value</th></tr>"); //$NON-NLS-1$ //$NON-NLS-2$

		String[] beanDefinitionNames = getConfigurationAgent()
				.getApplicationContext().getBeanDefinitionNames();
		for (String beanDefinitionName : beanDefinitionNames) {
			Object bean = getConfigurationAgent().getApplicationContext()
					.getBean(beanDefinitionName);
			if (bean != null) {
				writer.println("<tr><td>" + beanDefinitionName + "</td><td>" //$NON-NLS-1$ //$NON-NLS-2$
						+ bean.toString() + "</td></tr>"); //$NON-NLS-1$
			}
		}
		writer.println("</table>"); //$NON-NLS-1$

		writer.flush();
		writer.close();
	}

}
