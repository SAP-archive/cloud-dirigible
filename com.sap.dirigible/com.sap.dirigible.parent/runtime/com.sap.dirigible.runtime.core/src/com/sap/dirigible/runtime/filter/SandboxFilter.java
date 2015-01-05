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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.runtime.logger.Logger;

public class SandboxFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SandboxFilter.class);

	public static final String SANDBOX_CONTEXT = "sandbox"; //$NON-NLS-1$
	public static final String DEBUG_CONTEXT = "debug"; //$NON-NLS-1$

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		logger.trace("SandboxFilter doFilter req.getServletPath(): " + req.getServletPath());

		if (req.getServletPath() != null && (req.getServletPath().contains(SANDBOX_CONTEXT))) {
			req.setAttribute(SANDBOX_CONTEXT, true);
			logger.trace("setAttribute(SANDBOX_CONTEXT, true)");
		}
		if (req.getServletPath() != null && req.getServletPath().contains(DEBUG_CONTEXT)) {
			req.setAttribute(DEBUG_CONTEXT, true);
			logger.trace("setAttribute(DEBUG_CONTEXT, true)");
		}
		
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("cache-control", "private, max-age=0, no-cache");
		
		chain.doFilter(request, response);
	}

}
