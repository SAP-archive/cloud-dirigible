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

package com.sap.dirigible.repository.db.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.api.RepositoryPath;
import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBCollection;
import com.sap.dirigible.repository.db.DBResource;
import com.sap.dirigible.repository.db.init.DBScriptsMap;
import com.sap.dirigible.repository.logging.Logger;

public class DBSearchDAO extends DBObjectDAO {

	private static Logger logger = Logger.getLogger(DBSearchDAO.class);

	DBSearchDAO(DBRepositoryDAO dbRepositoryDAO) {
		super(dbRepositoryDAO);
	}

	/**
	 * Search for files and folders containing the parameter in their name
	 * 
	 * @param parameter
	 * @return
	 */
	List<IEntity> searchName(String parameter, boolean caseInsensitive) throws DBBaseException {
		List<String> parameters = new ArrayList<String>();
		parameters.add("%" + getParameter(parameter, caseInsensitive) + "%"); //$NON-NLS-1$
		return search(parameters, getSearchNameSQLFile(caseInsensitive));
	}

	/**
	 * Search for files and folders containing the parameter (means *parameter)
	 * in their name under specified root folder (means *root)
	 * 
	 * @param root
	 * @param parameter
	 * @return
	 */
	List<IEntity> searchName(String root, String parameter, boolean caseInsensitive)
			throws DBBaseException {
		List<String> parameters = new ArrayList<String>();
		parameters.add(root + "%"); //$NON-NLS-1$
		parameters.add("%" + getParameter(parameter, caseInsensitive)); //$NON-NLS-1$
		return search(parameters, getSearchNameUnderRootSQLFile(caseInsensitive));
	}

	/**
	 * Search for files and folders containing the parameter in their name
	 * 
	 * @param parameter
	 * @return
	 */
	List<IEntity> searchPath(String parameter, boolean caseInsensitive) throws DBBaseException {
		List<String> parameters = new ArrayList<String>();
		parameters.add("%" + getParameter(parameter, caseInsensitive) + "%"); //$NON-NLS-1$
		return search(parameters, getSearchNameSQLFile(caseInsensitive));
	}

	private String getParameter(String parameter, boolean caseInsensitive) {
		return caseInsensitive ? parameter.toUpperCase() : parameter;
	}

	private String getSearchNameSQLFile(boolean caseInsensitive) {
		return caseInsensitive ? DBScriptsMap.SCRIPT_SEARCH_NAME
				: DBScriptsMap.SCRIPT_SEARCH_NAME_SENSE;
	}

	private String getSearchNameUnderRootSQLFile(boolean caseInsensitive) {
		return caseInsensitive ? DBScriptsMap.SCRIPT_SEARCH_NAME_UNDER_ROOT
				: DBScriptsMap.SCRIPT_SEARCH_NAME_UNDER_ROOT_SENSE;
	}

	private List<IEntity> search(List<String> parameters, String sqlFile) throws DBBaseException {
		logger.debug("entering searchInPath"); //$NON-NLS-1$

		checkInitialized();

		if (parameters == null || parameters.isEmpty()) {
			return null;
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script = getRepository().getDbUtils().readScript(connection, sqlFile,
					this.getClass());

			preparedStatement = getRepository().getDbUtils().getPreparedStatement(connection,
					script);
			for (int i = 0; i < parameters.size(); i++) {
				preparedStatement.setString(i + 1, parameters.get(i));
			}
			return getEntityList(preparedStatement);
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} catch (IOException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
			logger.debug("exiting searchInPath"); //$NON-NLS-1$
		}
	}

	private List<IEntity> getEntityList(PreparedStatement preparedStatement) throws SQLException {
		List<IEntity> result = new ArrayList<IEntity>();
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			DBObject dbObject = DBMapper.dbToObject(getRepository(), resultSet);
			if (dbObject instanceof DBFolder) {
				ICollection collection = new DBCollection(getRepository(), new RepositoryPath(
						dbObject.getPath()));
				result.add(collection);
			} else {
				IResource resource = new DBResource(getRepository(), new RepositoryPath(
						dbObject.getPath()));
				result.add(resource);
			}
		}
		return result;
	}

	public List<IEntity> searchInPathAndText(String parameter, boolean caseInsensitive)
			throws DBBaseException {
		logger.debug("entering searchInPathAndText"); //$NON-NLS-1$

		checkInitialized();

		if (parameter == null) {
			return null;
		}

		parameter = "%" + parameter + "%"; //$NON-NLS-1$ //$NON-NLS-2$

		List<IEntity> result = new ArrayList<IEntity>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script;
			if (caseInsensitive) {
				parameter = parameter.toUpperCase();
				script = getRepository().getDbUtils().readScript(connection,
						DBScriptsMap.SCRIPT_SEARCH_TEXT, this.getClass());
			} else {
				script = getRepository().getDbUtils().readScript(connection,
						DBScriptsMap.SCRIPT_SEARCH_TEXT_SENSE, this.getClass());
			}
			preparedStatement = getRepository().getDbUtils().getPreparedStatement(connection,
					script);
			preparedStatement.setString(1, parameter);
			preparedStatement.setString(2, parameter);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String path = resultSet.getString(1);
				IResource resource = getRepository().getResource(path);
				result.add(resource);
			}
			return result;
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} catch (IOException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
			logger.debug("exiting searchInPathAndText"); //$NON-NLS-1$
		}
	}
}
