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

package com.sap.dirigible.runtime.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.MissingResourceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.IndexNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.lucene.MemoryIndexer;
import com.sap.dirigible.runtime.registry.AbstractRegistryServlet;
import com.sap.dirigible.runtime.registry.Messages;

/**
 * Servlet implementation class RegistryServlet
 */
public class SearchServlet extends AbstractRegistryServlet {

    private static final String REQUEST_PROCESSING_FAILED_S =
        Messages.getString("SearchServlet.REQUEST_PROCESSING_FAILED_S"); //$NON-NLS-1$
    private static final String SEARCH_TERM = "term"; //$NON-NLS-1$
    private static final String REINDEX = "reindex"; //$NON-NLS-1$
//    private static final String CASE_INSENSITIVE_TERM = "caseInsensitive"; //$NON-NLS-1$

    private static final long serialVersionUID = 7435479651482177443L;

    private static final Logger log = LoggerFactory
            .getLogger(SearchServlet.class.getCanonicalName());

    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {

        
//        final String caseInsensitiveTerm = request
//                .getParameter(CASE_INSENSITIVE_TERM);
//        boolean caseInsensitive = false;
//        if (caseInsensitiveTerm != null && !"".equals(caseInsensitiveTerm)) { //$NON-NLS-1$
//            caseInsensitive = Boolean.parseBoolean(caseInsensitiveTerm);
//        }
    	
    	final String searchTerm = request.getParameter(SEARCH_TERM);
        try {
            final IRepository repository = getRepository(request);
            
            final String reindex = request.getParameter(REINDEX);
            if (reindex != null & !"".equals(reindex)) { //$NON-NLS-1$
            	MemoryIndexer.clearIndex();
            	MemoryIndexer.indexRepository(repository);
                response.getWriter().println("0"); //$NON-NLS-1$
                return;
            }
            
            
            response.setContentType("application/json");
            if (searchTerm == null || "".equals(searchTerm)) { //$NON-NLS-1$
                response.getWriter().println("[]"); //$NON-NLS-1$
                return;
            }

//            final List<IEntity> entities = repository.searchText(searchTerm,
//                    caseInsensitive);
//            enumerateEntities(response, entities);
            
        	List<String> paths = null;
        	try {
        		paths = MemoryIndexer.search(searchTerm);
        	} catch (IndexNotFoundException e) {
        		MemoryIndexer.indexRepository(repository);
        	}
        	paths = MemoryIndexer.search(searchTerm);
            
            enumeratePaths(response, paths);
        } catch (final IllegalArgumentException ex) {
            log.error(String.format(REQUEST_PROCESSING_FAILED_S, searchTerm)
                    + ex.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    ex.getMessage());
        } catch (final MissingResourceException ex) {
            log.error(String.format(REQUEST_PROCESSING_FAILED_S, searchTerm)
                    + ex.getMessage());
            response.sendError(HttpServletResponse.SC_NO_CONTENT,
                    ex.getMessage());
        }
    }

//    private void enumerateEntities(final HttpServletResponse response,
//                                   final List<IEntity> entities) throws IOException {
//        final PrintWriter writer = response.getWriter();
//
//        final String collectionPath = "/dirigible/registry"; //$NON-NLS-1$
//
//        final JsonArray rootArray = new JsonArray();
//
//        for (final IEntity entity : entities) {
//            String entityName = entity.getPath();
//            if (entityName.startsWith(AbstractRegistryServlet.REGISTRY_DEPLOY_PATH)) {
//                entityName = entityName.substring(AbstractRegistryServlet.REGISTRY_DEPLOY_PATH.length());
//                final String path = collectionPath + entityName;
//                final JsonObject elementObject = new JsonObject();
//                elementObject.addProperty("name", entityName); //$NON-NLS-1$
//                elementObject.addProperty("path", path); //$NON-NLS-1$
//                rootArray.add(elementObject);
//            }
//        }
//
//        writer.println(new Gson().toJsonTree(rootArray));
//        writer.flush();
//        writer.close();
//    }

	private void enumeratePaths(final HttpServletResponse response,
			final List<String> paths) throws IOException {
		final PrintWriter writer = response.getWriter();

		final String collectionPath = "/dirigible/registry"; //$NON-NLS-1$

		final JsonArray rootArray = new JsonArray();

		for (final String entity : paths) {
			String entityName = entity;
			if (entityName.startsWith(AbstractRegistryServlet.REGISTRY_DEPLOY_PATH)) {
				entityName = entityName
						.substring(AbstractRegistryServlet.REGISTRY_DEPLOY_PATH
								.length());
				final String path = collectionPath + entityName;
				final JsonObject elementObject = new JsonObject();
				elementObject.addProperty("name", entityName); //$NON-NLS-1$
				elementObject.addProperty("path", path); //$NON-NLS-1$
				rootArray.add(elementObject);
			}
		}

		writer.println(new Gson().toJsonTree(rootArray));
		writer.flush();
		writer.close();
	}

}
