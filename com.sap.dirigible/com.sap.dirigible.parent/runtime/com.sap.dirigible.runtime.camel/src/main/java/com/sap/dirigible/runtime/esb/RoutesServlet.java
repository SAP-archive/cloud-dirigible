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
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Endpoint;
import org.apache.camel.Route;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sap.dirigible.runtime.registry.PathUtils;

public class RoutesServlet extends ControlServlet {

	private static final long serialVersionUID = 1L;
	
    private static final String CAMEL_CONTEXT = "/camel/"; //$NON-NLS-1$
    private static final String SERVLET_PREFIX = "servlet:///";
    private static final String TIMER_PREFIX = "timer://";

    /**
     * @see ControlServlet#ControlServlet()
     */
    public RoutesServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {

        beforeGet(request, response);

        // explicitly force synchronization before rendering
        getConfigurationAgent().synchronize(request);

        final PrintWriter writer = response.getWriter();
        response.setContentType("application/json"); //$NON-NLS-1$

//        final Collection<Endpoint> endpoints = getConfigurationAgent().getCamelContext().getEndpoints();
//        final JsonArray rootArray = new JsonArray();
//        final String headingUrl = PathUtils.getHeadingUrl(request, getServletMapping());
//        
//        for (final Endpoint endpoint : endpoints) {
//            if (endpoint.getEndpointUri() != null
//            		&& endpoint.getEndpointUri().startsWith(SERVLET_PREFIX)) {
//            	
//            	final JsonObject elementObject = new JsonObject();
//                elementObject.addProperty("name", endpoint.getEndpointKey()); //$NON-NLS-1$
//                String path = headingUrl + endpoint.getEndpointUri().substring(SERVLET_PREFIX.length());
//            	elementObject.addProperty("path", path); //$NON-NLS-1$
//            	rootArray.add(elementObject); //$NON-NLS-1$
//            	
//            }
//        }
        
        
        final Collection<Route> routes = getConfigurationAgent().getCamelContext().getRoutes();
        final JsonArray rootArray = new JsonArray();
        final String headingUrl = PathUtils.getHeadingUrl(request, getServletMapping());
        
        for (final Route route : routes) {
            if (route.getId() != null) {
            	
            	final JsonObject elementObject = new JsonObject();
                elementObject.addProperty("name", route.getId()); //$NON-NLS-1$
                String path = null;
                if (route.getEndpoint() != null
                		&& route.getEndpoint().getEndpointUri() != null) {
                		if (route.getEndpoint().getEndpointUri().startsWith(SERVLET_PREFIX)) {
                			path = headingUrl + route.getEndpoint().getEndpointUri().substring(SERVLET_PREFIX.length());
                		} else if (route.getEndpoint().getEndpointUri().startsWith(TIMER_PREFIX)) {
                			path = headingUrl + route.getEndpoint().getEndpointUri().substring(TIMER_PREFIX.length());	            	
                		} else {
                			elementObject.addProperty("path", ""); //$NON-NLS-1$
                		}
                	elementObject.addProperty("path", path); //$NON-NLS-1$
                } else {
                	elementObject.addProperty("path", ""); //$NON-NLS-1$
                }
            	rootArray.add(elementObject); //$NON-NLS-1$
            }
        }
        
        writer.println(new Gson().toJsonTree(rootArray));
        writer.flush();
        writer.close();
    }
    
    
    
    protected String getServletMapping() {
        return CAMEL_CONTEXT;
    }

}
