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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.runtime.common.ICommonConstants;
import com.sap.dirigible.runtime.filter.SandboxFilter;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.registry.PathUtils;
import com.sap.dirigible.runtime.repository.RepositoryFacade;
import com.sap.dirigible.runtime.scripting.WikiUtils;
import com.sap.dirigible.runtime.web.WebRegistryServlet;

public class WikiRegistryServlet extends WebRegistryServlet {
	
	private static final Logger logger = Logger.getLogger(WikiRegistryServlet.class);

	private static final String ERROR_READING_BATCH_WITH_WIKI_PAGES = Messages.getString("WikiRegistryServlet.ERROR_READING_BATCH_WITH_WIKI_PAGES"); //$NON-NLS-1$

	private static final String WIKI_CONTENT = "/WikiContent"; //$NON-NLS-1$

	private static final long serialVersionUID = -1484072696377972535L;

	private static final String CONFLUENCE_EXTENSION = ".wiki"; //$NON-NLS-1$
	private static final String BATCH_EXTENSION = ".wikis"; //$NON-NLS-1$
	private static final int WIKI_CACHE_LIMIT = 10000; // ~ 100MB for cache of the wikis 
	
	// caches
	private static final Map<String, Date> resourceToModification = Collections.synchronizedMap(new HashMap<String, Date>());
	private static final Map<String, String> resourceToWiki = Collections.synchronizedMap(new HashMap<String, String>());

	protected String extractRepositoryPath(HttpServletRequest request)
			throws IllegalArgumentException {
		String requestPath = PathUtils.extractPath(request);
		if (request.getAttribute(SandboxFilter.SANDBOX_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.SANDBOX_CONTEXT)) {
			
			return SANDBOX_DEPLOY_PATH + ICommonConstants.SEPARATOR
					+ RepositoryFacade.getUser(request) + WIKI_CONTENT + requestPath;
		}
		return REGISTRY_DEPLOY_PATH + WIKI_CONTENT + requestPath;
	}
	
	@Override
	protected boolean checkExtensions(IEntity entity) {
		return entity.getName().endsWith(CONFLUENCE_EXTENSION)
				|| entity.getName().endsWith(BATCH_EXTENSION);
	}
	
	@Override
	protected byte[] preprocessContent(byte[] rawContent, IEntity entity) throws IOException {
		if (entity.getName().endsWith(CONFLUENCE_EXTENSION)) {
			return wikiToHtml(rawContent, entity);
		} else if (entity.getName().endsWith(BATCH_EXTENSION)) {
			return batchToHtml(rawContent, entity);
		}
		return null;
	}

	private byte[] wikiToHtml(byte[] rawContent, IEntity entity) throws IOException {
		
		
		byte[] result = null;

		Date lastModification = resourceToModification.get(entity.getPath());
		String existingWiki = resourceToWiki.get(entity.getPath());
		if (lastModification == null
				|| existingWiki == null
				|| lastModification.before(entity.getInformation().getModifiedAt())) {
//			StringWriter writer = new StringWriter();
//			HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
//			builder.setEmitAsDocument(false);
//			MarkupParser markupParser = new MarkupParser();
//			markupParser.setBuilder(builder);		
//			markupParser.setMarkupLanguage(new ConfluenceLanguage());
//			markupParser.parse(new String(rawContent));
			
			WikiUtils wikiUtils = new WikiUtils();
			String htmlContent = wikiUtils.toHtml(new String(rawContent));
			result = htmlContent.getBytes();
			
			resourceToModification.put(entity.getPath(), entity.getInformation().getModifiedAt());
			resourceToWiki.put(entity.getPath(), htmlContent);
			
			if (resourceToModification.size() > WIKI_CACHE_LIMIT) {
				// to many cached wiki pages, clean and collect the most used again
				resourceToModification.clear();
				resourceToWiki.clear();
				logger.info("Wiki cache reaches its limit of 10000 pages. Clean-up done."); //$NON-NLS-1$
			}
		} else {
			result = existingWiki.getBytes();
		}
		return result;
	}

	private byte[] batchToHtml(byte[] rawContent, IEntity entity) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader((new ByteArrayInputStream(rawContent))));
		String line = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			while((line = reader.readLine()) != null) {
				IResource wikiResource = entity.getRepository().getResource(entity.getParent().getPath() + IRepository.SEPARATOR + line);
				if (wikiResource.exists()) {
					outputStream.write(wikiToHtml(wikiResource.getContent(), wikiResource));
				} else {
					logger.error(String.format("Error while render batch of wiki pages. Resource %s does not exist", wikiResource.getPath())); //$NON-NLS-1$
				}
			}
		} catch (IOException e) {
			throw new IOException(ERROR_READING_BATCH_WITH_WIKI_PAGES, e);
		}
		return outputStream.toByteArray();
	}

	
}
