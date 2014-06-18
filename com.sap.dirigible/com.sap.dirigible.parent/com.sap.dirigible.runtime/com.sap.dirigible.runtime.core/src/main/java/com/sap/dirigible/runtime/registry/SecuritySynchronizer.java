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

package com.sap.dirigible.runtime.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.security.SecurityManager;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public class SecuritySynchronizer implements Runnable {

	private static final String REFRESHING_OF_SECURED_LOCATIONS_FAILED = Messages
			.getString("SecuritySynchronizer.REFRESHING_OF_SECURED_LOCATIONS_FAILED"); //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(SecuritySynchronizer.class);

	private static List<String> securedLocations = Collections
			.synchronizedList(new ArrayList<String>());

	@Override
	public void run() {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$

		try {
			refreshSecuredLocations();
			logger.info("Refresh of secured locations successful for: " //$NON-NLS-1$
					+ securedLocations.size());
		} catch (Exception e) {
			logger.error(REFRESHING_OF_SECURED_LOCATIONS_FAILED, e);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$
	}

	public static List<String> getSecuredLocations() {
		return securedLocations;
	}

	private void refreshSecuredLocations() throws ServletException {

		SecurityManager securityManager = null;
		try {
			DataSource dataSource = RepositoryFacade.getInstance().getDataSource();
			IRepository repository = RepositoryFacade.getInstance().getRepository(null);
			// TODO
			securityManager = SecurityManager.getInstance(repository, dataSource);
			securedLocations = securityManager.getSecuredLocations();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
