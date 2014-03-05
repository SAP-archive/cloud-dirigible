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

package com.sap.dirigible.ide.db.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServiceHandler;
import org.eclipse.rap.rwt.service.ServiceManager;

import com.sap.dirigible.ide.logging.Logger;

public class DataExportServiceHandler implements ServiceHandler {

	private static final String DSV_EXTENSION = ".dsv";
	private static final String DataExportServiceHandler_ERROR_WHILE_EXPORTING_DSV = Messages.DataExportServiceHandler_ERROR_WHILE_EXPORTING_DSV;
	static final String DataExportServiceHandler_SERVICE_HANDLER_ID = "com.sap.dirigible.ide.db.data.DataExportServiceHandler";

	private static final Logger logger = Logger
			.getLogger(DataExportServiceHandler.class);

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String fileName = request.getParameter("filename");//$NON-NLS-1$
		String tableName = fileName.substring(0, fileName.lastIndexOf(DSV_EXTENSION)).toUpperCase();
		byte[] download;
		
		try {
			DataFinder dataFinder = new DataFinder();
			dataFinder.setTableName(tableName);
			download = dataFinder.getTableData().getBytes();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			download = (DataExportServiceHandler_ERROR_WHILE_EXPORTING_DSV + tableName).getBytes();
			tableName = "error.txt"; //$NON-NLS-1$
		}

		String contentDisposition = "attachment; filename=\"" + fileName+"\""; //$NON-NLS-1$ //$NON-NLS-2$
		response.setContentType("text/plain");//$NON-NLS-1$
		response.setHeader("Content-Disposition", contentDisposition); //$NON-NLS-1$
		response.getOutputStream().write(download);
	}

	public static String getUrl(String tableName) {

		ServiceManager manager = RWT.getServiceManager();
		String rootURL = manager
				.getServiceHandlerUrl(DataExportServiceHandler_SERVICE_HANDLER_ID);
		StringBuffer url = new StringBuffer();
		url.append(rootURL);
		url.append("&"); //$NON-NLS-1$
		url.append("filename").append("=").append(tableName.toLowerCase()).append(DSV_EXTENSION); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		int relativeIndex = url.lastIndexOf("/"); //$NON-NLS-1$

		if (relativeIndex > -1) {
			url.delete(0, relativeIndex + 1);
		}

		return RWT.getResponse().encodeURL(url.toString());
	}
}
