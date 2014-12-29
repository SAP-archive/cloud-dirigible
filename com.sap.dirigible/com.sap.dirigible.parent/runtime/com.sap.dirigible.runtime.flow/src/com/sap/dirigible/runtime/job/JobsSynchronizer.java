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

package com.sap.dirigible.runtime.job;

import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public class JobsSynchronizer implements Runnable {

	private static final Logger logger = Logger.getLogger(JobsSynchronizer.class);

	@Override
	public void run() {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$

		try {
			refreshJobs();
			logger.debug("Refresh of jobs locations successful for: " //$NON-NLS-1$
					+ JobsUpdater.activeJobs.size());
		} catch (Exception e) {
			logger.error("Refreshing Jobs failed.", e);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "run"); //$NON-NLS-1$
	}

	public static List<String> getActiveJobs() {
		return JobsUpdater.activeJobs;
	}

	private void refreshJobs() throws ServletException {

		try {
			DataSource dataSource = RepositoryFacade.getInstance().getDataSource();
			IRepository repository = RepositoryFacade.getInstance().getRepository(null);
			// TODO
			JobsUpdater jobsUpdater = new JobsUpdater(repository, dataSource,
					JobsUpdater.REGISTRY_INTEGRATION_DEFAULT);
			jobsUpdater.applyUpdates();
			jobsUpdater.cleanDeletedJobs();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
