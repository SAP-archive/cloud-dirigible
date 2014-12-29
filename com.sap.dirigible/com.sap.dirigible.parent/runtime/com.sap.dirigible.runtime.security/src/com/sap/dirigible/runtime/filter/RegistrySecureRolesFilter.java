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
import java.security.Principal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.api.RepositoryException;
import com.sap.dirigible.repository.ext.security.SecurityManager;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.registry.Messages;
import com.sap.dirigible.runtime.registry.PathUtils;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public class RegistrySecureRolesFilter extends AbstractRegistrySecureFilter {

	private static final String YOU_DO_NOT_HAVE_REQUIRED_ROLE_S_TO_ACCESS_THIS_LOCATION = Messages
			.getString("RegistrySecureRolesFilter.YOU_DO_NOT_HAVE_REQUIRED_ROLE_S_TO_ACCESS_THIS_LOCATION"); //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(RegistrySecureRolesFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		String location = PathUtils.extractPath(request);
		if (isLocationSecured(location)) {
			if (!isUserInRole(req, location)) {
				((HttpServletResponse) res).sendError(HttpServletResponse.SC_FORBIDDEN,
						YOU_DO_NOT_HAVE_REQUIRED_ROLE_S_TO_ACCESS_THIS_LOCATION);
			}
		}
		chain.doFilter(req, res);

	}

	private boolean isUserInRole(ServletRequest req, String location) {
		try {
			if (req instanceof HttpServletRequest) {
				HttpServletRequest request = (HttpServletRequest) req;
				Principal principal = request.getUserPrincipal();
				if (principal != null) {
					SecurityManager securityManager = SecurityManager.getInstance(RepositoryFacade
							.getInstance().getRepository(request), RepositoryFacade.getInstance()
							.getDataSource());
					List<String> roles = securityManager.getRolesForLocation(location);
					for (Iterator<String> iterator = roles.iterator(); iterator.hasNext();) {
						String role = iterator.next();
						if (request.isUserInRole(role)) {
							return true;
						}
					}
				}
			}
		} catch (RepositoryException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	protected String getSecuredMapping() {
		return null;
	}

}
