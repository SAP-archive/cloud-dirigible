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

package com.sap.dirigible.ide.workspace.wizard.project.export;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServiceHandler;
import org.eclipse.rap.rwt.service.ServiceManager;

import com.sap.dirigible.ide.logging.Logger;

public class DownloadProjectServiceHandler implements ServiceHandler {

	private static final String ERROR_WHILE_GETTING_CONTENT_FROM_LOCATION = Messages.DownloadProjectServiceHandler_ERROR_WHILE_GETTING_CONTENT_FROM_LOCATION;

	static final String SERVICE_HANDLER_ID = "com.sap.dirigible.ide.workspace.wizard.project.export.DownloadProjectServiceHandler"; //$NON-NLS-1$

	private static final Logger logger = Logger
			.getLogger(DownloadProjectServiceHandler.class);

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// Which file to download?
		String fileName = request.getParameter("filename"); //$NON-NLS-1$
		// Get the file content
		byte[] download;
		try {
			download = RepositoryDataStore.getByteArrayData(fileName);
			// Send the file in the response
			response.setContentType("application/zip"); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			download = (ERROR_WHILE_GETTING_CONTENT_FROM_LOCATION + fileName)
					.getBytes();
			// Send the file in the response
			response.setContentType("text/plain"); //$NON-NLS-1$
			fileName = "error.txt"; //$NON-NLS-1$
		}
		response.setContentLength(download.length);
		String contentDisposition = "attachment; filename=\"" + fileName + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		response.setHeader("Content-Disposition", contentDisposition); //$NON-NLS-1$
		response.getOutputStream().write(download);

	}

	public static String getUrl(String token) {
		ServiceManager manager = RWT.getServiceManager();
		String rootURL = manager.getServiceHandlerUrl(SERVICE_HANDLER_ID);
		StringBuffer url = new StringBuffer();
		url.append(rootURL);
		url.append("&"); //$NON-NLS-1$
		url.append("filename").append("=").append(token).append(".zip"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		int relativeIndex = url.lastIndexOf("/"); //$NON-NLS-1$
		if (relativeIndex > -1) {
			url.delete(0, relativeIndex + 1);
		}
		return RWT.getResponse().encodeURL(url.toString());
	}
}
