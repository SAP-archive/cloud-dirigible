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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.ext.db.AbstractDataUpdater;

public class JobsUpdater extends AbstractDataUpdater {

	static final String NODE_NAME = "name";
	static final String NODE_DESCRIPTION = "description";
	static final String NODE_EXPRESSION = "expression";
	static final String NODE_TYPE = "type";
	static final String NODE_MODULE = "module";
	
	public static final String EXTENSION_JOB = ".job"; //$NON-NLS-1$
	
	public static final String REGISTRY_INTEGRATION_DEFAULT = ICommonConstants.INTEGRATION_REGISTRY_PUBLISH_LOCATION;
	
	private static final Logger logger = LoggerFactory.getLogger(JobsUpdater.class);

	private IRepository repository;
	private DataSource dataSource;
	private String location;
	private Scheduler scheduler;
	

	public JobsUpdater(IRepository repository, DataSource dataSource,
			String location) throws JobsException {
		this.repository = repository;
		this.dataSource = dataSource;
		this.location = location;
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			logger.debug("Creating quartz scheduler..."); //$NON-NLS-1$
			this.scheduler = schedulerFactory.getScheduler();
			logger.debug("Quartz scheduler created."); //$NON-NLS-1$
			logger.debug("Starting quartz scheduler..."); //$NON-NLS-1$
			this.scheduler.start();
			logger.debug("Quartz scheduler started."); //$NON-NLS-1$
		} catch (SchedulerException e) {
			throw new JobsException(e);
		}
	}

	@Override
	public void executeUpdate(List<String> knownFiles,
			HttpServletRequest request) throws Exception {
		if (knownFiles.size() == 0) {
			return;
		}

		try {
			Connection connection = dataSource.getConnection();

			try {
				for (Iterator<String> iterator = knownFiles.iterator(); iterator
						.hasNext();) {
					String jobDefinition = iterator.next();
					if (jobDefinition.endsWith(EXTENSION_JOB)) {
						executeJobUpdate(connection, jobDefinition, request);
					}
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
//			throw new Exception(e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
//			throw new Exception(e);
		}
	}

	private void executeJobUpdate(Connection connection,
			String jobDefinition, HttpServletRequest request)
			throws SQLException, IOException, JobsException {
		JsonObject jobDefinitionArray = parseJob(jobDefinition);
		
		String jobName = jobDefinitionArray.get(NODE_NAME).getAsString(); //$NON-NLS-1$
		String jobDescription = jobDefinitionArray.get(NODE_DESCRIPTION).getAsString(); //$NON-NLS-1$
		String jobExpression = jobDefinitionArray.get(NODE_EXPRESSION).getAsString(); //$NON-NLS-1$
		String jobType = jobDefinitionArray.get(NODE_TYPE).getAsString(); //$NON-NLS-1$
		String jobModule = jobDefinitionArray.get(NODE_MODULE).getAsString(); //$NON-NLS-1$
		
		logger.debug(String.format("Creating quartz job name: %s, description: %s, expression: %s, type: %s, module: %s ...", 
				jobName, jobDescription, jobExpression, jobType, jobModule)); //$NON-NLS-1$
		
		JobDetail jobDetail = new JobDetail(jobName, null, CronJob.class);
		jobDetail.getJobDataMap().put(NODE_NAME, jobDescription);
		jobDetail.getJobDataMap().put(NODE_TYPE, jobType);
		jobDetail.getJobDataMap().put(NODE_MODULE, jobModule);

		try {
			CronTrigger trigger = new CronTrigger(jobName, null, jobExpression);
			this.scheduler.scheduleJob(jobDetail, trigger);
		} catch (ParseException e) {
			throw new JobsException(e);
		} catch (SchedulerException e) {
			throw new JobsException(e);
		}
		
	}

	private JsonObject parseJob(String jobDefinition) throws IOException {
		// {
		// "name":"MyJob",
		// "description":"MyJob Description",
		// "expression":"0/20 * * * * ?",
		// "type":"javascript",
		// "module":"/${projectName}/service1.js"
		// }

		IRepository repository = this.repository;
		IResource resource = repository.getResource(this.location
				+ jobDefinition);
		String content = new String(resource.getContent());
		JsonParser parser = new JsonParser();
		JsonObject jobDefinitionObject = (JsonObject) parser.parse(content);

		// TODO validate the parsed content has the right structure

		return jobDefinitionObject;
	}

	public void enumerateKnownFiles(ICollection collection,
			List<String> dsDefinitions) throws IOException {
		if (collection.exists()) {
			List<IResource> resources = collection.getResources();
			for (Iterator<IResource> iterator = resources.iterator(); iterator
					.hasNext();) {
				IResource resource = iterator.next();
				if (resource != null && resource.getName() != null) {
					if (resource.getName().endsWith(EXTENSION_JOB)) {
						String fullPath = collection.getPath().substring(
								this.location.length())
								+ IRepository.SEPARATOR + resource.getName();
						dsDefinitions.add(fullPath);
					}
				}
			}

			List<ICollection> collections = collection.getCollections();
			for (Iterator<ICollection> iterator = collections.iterator(); iterator
					.hasNext();) {
				ICollection subCollection = iterator.next();
				enumerateKnownFiles(subCollection, dsDefinitions);
			}
		}
	}

	@Override
	public void applyUpdates() throws IOException, Exception {
		List<String> knownFiles = new ArrayList<String>();
		ICollection srcContainer = this.repository.getCollection(this.location);
		if (srcContainer.exists()) {
			enumerateKnownFiles(srcContainer, knownFiles);// fill knownFiles[]
															// with urls to
															// recognizable
															// repository files
			executeUpdate(knownFiles, null);// execute the real updates
		}
	}
	
	@Override
	public IRepository getRepository() {
		return repository;
	}
	
	@Override
	public String getLocation() {
		return location;
	}
	
	@Override
	public void executeUpdate(List<String> knownFiles) throws Exception {
		executeUpdate(knownFiles, null);
	}
	
}
