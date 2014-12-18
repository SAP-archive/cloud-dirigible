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

package com.sap.dirigible.runtime.memory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MemoryServlet
 */
public class MemoryServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5645919875259516138L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemoryServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter writer = response.getWriter();
		
		String paramLog = request.getParameter("log");
		
		try {
			if (paramLog != null) {
				response.setContentType("text/tab-separated-values");
				// full log for chart
				String[][] records = MemoryLogRecordDAO.getMemoryLogRecords();
				for (int i = 0; i < records.length; i++) {
					writer.print(records[i][0] + "\t");
					writer.print(records[i][1] + "\t");
					writer.print(records[i][2] + "\t");
					writer.print(records[i][3] + "\t");
					writer.println();
				}
			} else {
				// instant numbers
				String content = MemoryLogRecordDAO.generateMemoryInfo();
				writer.write(content);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.flush();
		writer.close();
	}

}
