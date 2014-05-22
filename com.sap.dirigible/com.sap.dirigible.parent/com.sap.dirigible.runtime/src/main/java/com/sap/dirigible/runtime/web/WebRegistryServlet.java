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

package com.sap.dirigible.runtime.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.runtime.common.ICommonConstants;
import com.sap.dirigible.runtime.filter.SandboxFilter;
import com.sap.dirigible.runtime.registry.PathUtils;
import com.sap.dirigible.runtime.registry.RegistryServlet;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public class WebRegistryServlet extends RegistryServlet {

	private static final String WEB_CONTENT = "/WebContent"; //$NON-NLS-1$
	private static final String PARAMETER_NO_HEADER_AND_FOOTER = "nohf"; //$NON-NLS-1$
	private static final String PARAMETER_LIST = "list"; //$NON-NLS-1$

	private static final long serialVersionUID = -1484072696377972535L;

	protected static final String HEADER_REF = "header.ref"; //$NON-NLS-1$
	protected static final String HEADER_HTML = "header.html"; //$NON-NLS-1$
	protected static final String FOOTER_HTML = "footer.html"; //$NON-NLS-1$
	private static final String FOOTER_REF = "footer.ref"; //$NON-NLS-1$
	protected static final String HTML_EXTENSION = ".html"; //$NON-NLS-1$
	protected static final String INDEX_HTML = "index.html"; //$NON-NLS-1$

	protected String extractRepositoryPath(HttpServletRequest request)
			throws IllegalArgumentException {
		String requestPath = PathUtils.extractPath(request);
		if (request.getAttribute(SandboxFilter.SANDBOX_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.SANDBOX_CONTEXT)) {
			return SANDBOX_DEPLOY_PATH + ICommonConstants.SEPARATOR
					+ RepositoryFacade.getUser(request) + WEB_CONTENT + requestPath;
		}
		return REGISTRY_DEPLOY_PATH + WEB_CONTENT + requestPath;
	}

	@Override
	protected byte[] buildResourceData(IEntity entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		byte[] rawContent = super.buildResourceData(entity, request, response);
		boolean nohf = (request.getParameter(PARAMETER_NO_HEADER_AND_FOOTER) != null);
		boolean list = (request.getParameter(PARAMETER_LIST) != null);
		
		if (list) {
			// list parameter is present - return JSON formatted content
			return super.buildResourceData(entity, request, response);
		}
		
		if (checkExtensions(entity)
				&& !INDEX_HTML.equals(entity.getName().toLowerCase())) {
			// it is *.html and it is NOT index.html
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			// lookup for header.html
			IResource header = entity.getParent().getResource(HEADER_HTML);
			IResource headerRef = entity.getParent().getResource(HEADER_REF);
			if (!nohf && headerRef.exists()) {
				String headerPath = new String(headerRef.getContent()).trim();
				IResource headerContent = entity.getRepository().getResource(REGISTRY_DEPLOY_PATH + WEB_CONTENT + headerPath);
				// start with header
				if(headerContent.exists()){
					outputStream.write(headerContent.getContent());
				}
			} else if (!nohf && header.exists()) {
				// start with header
				outputStream.write(header.getContent());
			}

			// put the content
			outputStream.write(preprocessContent(rawContent, entity));
			
			// lookup for footer.html
			IResource footer = entity.getParent().getResource(FOOTER_HTML);
			IResource footerRef = entity.getParent().getResource(FOOTER_REF);
			if(!nohf && footerRef.exists()) {
				String footerPath = new String(footerRef.getContent()).trim();
				IResource footerContent = entity.getRepository().getResource(REGISTRY_DEPLOY_PATH + WEB_CONTENT + footerPath);
				// end with footer
				if(footerContent.exists()){
					outputStream.write(footerContent.getContent());
				}
			} else if (!nohf && footer.exists()) {
				// end with footer
				outputStream.write(footer.getContent());
			}
			return outputStream.toByteArray();
		}
		return rawContent;
	}

	protected boolean checkExtensions(IEntity entity) {
		return entity.getName().endsWith(HTML_EXTENSION);
	}

	protected byte[] preprocessContent(byte[] rawContent, IEntity entity) throws IOException {
		return rawContent;
	}

	@Override
	protected byte[] buildCollectionData(boolean deep, IEntity entity,
			String collectionPath) throws IOException {
		byte[] data;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(baos);
		// lookup for index.html
		IResource index = entity.getParent().getResource(INDEX_HTML);
		if (index.exists()) {
			// start with index
			writer.print(index.getContent());
		} else {
			return super.buildCollectionData(deep, entity, collectionPath);
		}
		writer.flush();
		data = baos.toByteArray();
		return preprocessContent(data, entity);
	}
}
