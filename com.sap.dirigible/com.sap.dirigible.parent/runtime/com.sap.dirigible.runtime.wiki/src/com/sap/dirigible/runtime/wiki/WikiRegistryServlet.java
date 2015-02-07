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

package com.sap.dirigible.runtime.wiki;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.filter.SandboxFilter;
import com.sap.dirigible.runtime.registry.PathUtils;
import com.sap.dirigible.runtime.repository.RepositoryFacade;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.web.WebRegistryServlet;

public class WikiRegistryServlet extends WebRegistryServlet {
	
	private static final Logger logger = Logger.getLogger(WikiRegistryServlet.class);

	

	private static final String WIKI_CONTENT = "/WikiContent"; //$NON-NLS-1$

	private static final long serialVersionUID = -1484072696377972535L;

	
	
	
	
	
	

	protected String extractRepositoryPath(HttpServletRequest request)
			throws IllegalArgumentException {
		String requestPath = PathUtils.extractPath(request);
		if (request.getAttribute(SandboxFilter.SANDBOX_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.SANDBOX_CONTEXT)) {
			
			return IRepositoryPaths.SANDBOX_DEPLOY_PATH + ICommonConstants.SEPARATOR
					+ RepositoryFacade.getUser(request) + WIKI_CONTENT + requestPath;
		}
		return IRepositoryPaths.REGISTRY_DEPLOY_PATH + WIKI_CONTENT + requestPath;
	}
	
	@Override
	protected boolean checkExtensions(IEntity entity) {
		return entity.getName().endsWith(WikiProcessor.DEFAULT_WIKI_EXTENSION)
				|| entity.getName().endsWith(WikiProcessor.CONFLUENCE_EXTENSION)
				|| entity.getName().endsWith(WikiProcessor.MARKDOWN_EXTENSION)
				|| entity.getName().endsWith(WikiProcessor.MARKDOWN_EXTENSION2)
				|| entity.getName().endsWith(WikiProcessor.MARKDOWN_EXTENSION3)
				|| entity.getName().endsWith(WikiProcessor.MARKDOWN_EXTENSION4)
				|| entity.getName().endsWith(WikiProcessor.MARKDOWN_EXTENSION5)
				|| entity.getName().endsWith(WikiProcessor.MARKDOWN_EXTENSION6)
				|| entity.getName().endsWith(WikiProcessor.TEXTILE_EXTENSION)
				|| entity.getName().endsWith(WikiProcessor.TRACWIKI_EXTENSION)
				|| entity.getName().endsWith(WikiProcessor.TWIKI_EXTENSION)
				|| entity.getName().endsWith(WikiProcessor.BATCH_EXTENSION)
				;
	}
	
	@Override
	protected byte[] preprocessContent(byte[] rawContent, IEntity entity) throws IOException {
		return WikiProcessor.processContent(rawContent, entity);
//		if (entity.getName().endsWith(CONFLUENCE_EXTENSION)
//				|| entity.getName().endsWith(DEFAULT_WIKI_EXTENSION)) {
//			return wikiToHtml(rawContent, entity, WikiUtils.WIKI_FORMAT_CONFLUENCE);
//		} else if (entity.getName().endsWith(MARKDOWN_EXTENSION)
//				|| entity.getName().endsWith(MARKDOWN_EXTENSION2)
//				|| entity.getName().endsWith(MARKDOWN_EXTENSION3)
//				|| entity.getName().endsWith(MARKDOWN_EXTENSION4)
//				|| entity.getName().endsWith(MARKDOWN_EXTENSION5)
//				|| entity.getName().endsWith(MARKDOWN_EXTENSION6)) {
//			return wikiToHtml(rawContent, entity, WikiUtils.WIKI_FORMAT_MARKDOWN);
////		} else if (entity.getName().endsWith(MEDIAWIKI_EXTENSION)) {
////			return wikiToHtml(rawContent, entity, WikiUtils.WIKI_FORMAT_MEDIAWIKI);
//		} else if (entity.getName().endsWith(TEXTILE_EXTENSION)) {
//			return wikiToHtml(rawContent, entity, WikiUtils.WIKI_FORMAT_TEXTILE);
//		} else if (entity.getName().endsWith(TRACWIKI_EXTENSION)) {
//			return wikiToHtml(rawContent, entity, WikiUtils.WIKI_FORMAT_TRACWIKI);
//		} else if (entity.getName().endsWith(TWIKI_EXTENSION)) {
//			return wikiToHtml(rawContent, entity, WikiUtils.WIKI_FORMAT_TWIKI);
//		} else if (entity.getName().endsWith(BATCH_EXTENSION)) {
//			return batchToHtml(rawContent, entity);
		}
	
	protected String getContentFolder() {
		return WIKI_CONTENT;
	}

	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		WikiExecutor executor = new WikiExecutor(getRepository(request),
				getWebRegistryPath(request), IRepositoryPaths.REGISTRY_DEPLOY_PATH + WIKI_CONTENT);
		return executor;
	}
	
}
