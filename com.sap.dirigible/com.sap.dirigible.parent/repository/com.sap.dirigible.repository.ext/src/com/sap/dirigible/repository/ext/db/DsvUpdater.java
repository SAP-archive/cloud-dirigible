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

package com.sap.dirigible.repository.ext.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.logging.Logger;

public class DsvUpdater extends AbstractDataUpdater {

	private static final String DELETE_FROM = "DELETE FROM ";
	private static final String TABLE_NAME = "tableName";

	private static final String EXTENSION_TABLE = ".table";
	private static final String EXTENSION_DSV = ".dsv";
	
	private static final Logger logger = Logger.getLogger(DsvUpdater.class);

	private IRepository repository;
	private DataSource dataSource;
	private String location;

	public DsvUpdater(IRepository repository, DataSource dataSource,
			String location) {
		this.repository = repository;
		this.dataSource = dataSource;
		this.location = location;
	}

	@Override
	public void enumerateKnownFiles(ICollection collection,
			List<String> dsDefinitions) throws IOException {
		if (collection.exists()) {
			List<IResource> resources = collection.getResources();
			for (Iterator<IResource> iterator = resources.iterator(); iterator
					.hasNext();) {
				IResource resource = iterator.next();
				if (resource != null && resource.getName() != null) {
					if (resource.getName().endsWith(EXTENSION_DSV)) {
//						# 177
//						String fullPath = collection.getPath().substring(
//								this.location.length())
//								+ IRepository.SEPARATOR + resource.getName();
						String fullPath = resource.getPath();
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
	public void executeUpdate(List<String> knownFiles, List<String> errors) throws Exception {
		if (knownFiles.size() == 0) {
			return;
		}

		try {
			Connection connection = dataSource.getConnection();

			try {
				for (String dsDefinition : knownFiles) {
					try {
						if (dsDefinition.endsWith(EXTENSION_DSV)) {
							executeDSVUpdate(connection, dsDefinition);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						errors.add(e.getMessage());
					}
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void executeUpdate(List<String> knownFiles, HttpServletRequest request, List<String> errors) throws Exception {
		executeUpdate(knownFiles, errors);
	}
	
	private void executeDSVUpdate(Connection connection, String dsDefinition)
			throws Exception {
		String dsDefinitionTable = dsDefinition.replace(EXTENSION_DSV, EXTENSION_TABLE);
		JsonObject dsDefinitionObject = parseTable(dsDefinitionTable);
		String tableName = dsDefinitionObject.get(TABLE_NAME).getAsString();
		tableName = tableName.toUpperCase();
		deleteAllDataFromTable(tableName);

		IRepository repository = this.repository;
//		# 177
//		IResource resource = repository.getResource(this.location + dsDefinition);
		IResource resource = repository.getResource(dsDefinition);
		byte[] content = resource.getContent();

		if (content.length != 0) {
			DBTableDataInserter tableDataInserter = new DBTableDataInserter(dataSource, 
					content, tableName + EXTENSION_TABLE);
			tableDataInserter.insert();
		}
	}

	private JsonObject parseTable(String dsDefinition) throws IOException {
		IRepository repository = this.repository;
//		# 177
//		IResource resource = repository.getResource(this.location + dsDefinition);
		IResource resource = repository.getResource(dsDefinition);
		String content = new String(resource.getContent());
		JsonParser parser = new JsonParser();
		JsonObject dsDefinitionObject = (JsonObject) parser.parse(content);
		return dsDefinitionObject;
	}

	private void deleteAllDataFromTable(String tableName) throws Exception {
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement deleteStatement = con.prepareStatement(DELETE_FROM + tableName);
			deleteStatement.execute();
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	private Connection getConnection() throws Exception {
		return dataSource.getConnection();
	}

	@Override
	public IRepository getRepository() {
		return this.repository;
	}
	
	@Override
	public String getLocation() {
		return location;
	}
}
