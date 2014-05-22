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

package com.sap.dirigible.runtime.filter;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.registry.PathUtils;
import com.sap.dirigible.runtime.registry.SecuritySynchronizer;

public abstract class AbstractRegistrySecureFilter implements Filter {

	private static final String HTTP_METHOD_CANNOT_BE_REDIRECTED_AUTOMATICALLY = "AbstractRegistrySecureFilter.2"; //$NON-NLS-1$

	private static final String JSON = "json"; //$NON-NLS-1$
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		
    	// SAML standard redirect
		String location = PathUtils.extractPath(request);
		if (isLocationSecured(location)) {
			// SAML do not redirect in case of explicitly requested application/json without logged-in user
			String acceptHeader = request.getHeader("Accept"); //$NON-NLS-1$              	
	    	if (acceptHeader != null
	    			&& acceptHeader.contains(JSON)) {
	    		if (request.getUserPrincipal() == null) {
		    		((HttpServletResponse) res).sendError(HttpURLConnection.HTTP_FORBIDDEN, 
							String.format(Messages.getString(HTTP_METHOD_CANNOT_BE_REDIRECTED_AUTOMATICALLY),
									request.getMethod(), req.getServletContext().getContextPath() + getSecuredMapping()));
		    		return;
	    		}
	    	}
			if (HttpMethod.GET.equalsIgnoreCase(request.getMethod())) {
				((HttpServletResponse) res).sendRedirect(req.getServletContext()
						.getContextPath() + getSecuredMapping() + location + 
						(request.getQueryString() != null ? "?" + request.getQueryString() : "")); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				((HttpServletResponse) res).sendError(HttpURLConnection.HTTP_FORBIDDEN, 
						String.format(Messages.getString(HTTP_METHOD_CANNOT_BE_REDIRECTED_AUTOMATICALLY),
								request.getMethod(), req.getServletContext().getContextPath() + getSecuredMapping()));
			}
			return;
		}
		chain.doFilter(req, res);
	}

	protected abstract String getSecuredMapping();

	protected boolean isLocationSecured(String location)
			throws ServletException {
		for (String securedLocation : SecuritySynchronizer
				.getSecuredLocations()) {
			if (location.startsWith(securedLocation)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
