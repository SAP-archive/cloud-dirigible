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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBRepository;

public class DBFolderDAO extends DBObjectDAO {

	private static Logger logger = LoggerFactory.getLogger(DBFolderDAO.class
			.getCanonicalName());

	DBFolderDAO(DBRepositoryDAO dbRepositoryDAO) {
		super(dbRepositoryDAO);
	}

	/**
	 * Return the database folder object
	 * 
	 * @param path
	 * @return
	 * @throws DBBaseException
	 */
	DBFolder getFolderByPath(String path) throws DBBaseException {
		logger.debug(this.getClass().getCanonicalName(),
				"entering getFolderByPath"); //$NON-NLS-1$

		checkInitialized();

		DBFolder dbFolder = null;
		DBObject dbObject = getObjectByPath(path);
		if (dbObject instanceof DBFolder) {
			dbFolder = (DBFolder) dbObject;
		}
		logger.debug(this.getClass().getCanonicalName(),
				"exiting getFolderByPath"); //$NON-NLS-1$
		return dbFolder;
	}

	/**
	 * Create the database folder object in the target database schema, located
	 * at the given path - cascading
	 * 
	 * @param path
	 * @return
	 * @throws DBBaseException
	 */
	DBFolder createFolder(String path) throws DBBaseException {
		logger.debug(this.getClass().getCanonicalName(),
				"entering createFolder"); //$NON-NLS-1$

		checkInitialized();

		if (path == null || "".equals(path.trim())) { //$NON-NLS-1$
			return null;
		}

		StringTokenizer tokenizer = new StringTokenizer(path,
				DBRepository.PATH_DELIMITER);
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(DBRepository.PATH_DELIMITER);
		DBFolder parent = null;
		DBFolder current = null;
		while (tokenizer.hasMoreTokens()) {
			pathBuffer.append(tokenizer.nextToken());

			current = getFolderByPath(pathBuffer.toString());
			if (current == null) {
				current = createSingleFolder(pathBuffer.toString(), parent);
			}
			parent = current;

			pathBuffer.append(DBRepository.PATH_DELIMITER);
		}
		logger.debug(this.getClass().getCanonicalName(), "exiting createFolder"); //$NON-NLS-1$
		return current;
	}

	/**
	 * Create the database folder object in the target database schema, located
	 * at the given path - single entity only
	 * 
	 * @param path
	 * @return
	 * @throws DBBaseException
	 */
	DBFolder createSingleFolder(String path, DBFolder parent)
			throws DBBaseException {
		logger.debug(this.getClass().getCanonicalName(),
				" entering createSingleFolder"); //$NON-NLS-1$

		checkInitialized();

		String name = path.substring(path
				.lastIndexOf(DBRepository.PATH_DELIMITER) + 1);
		String createdBy = getRepository().getUser();
		String modifiedBy = getRepository().getUser();

		getDbRepositoryDAO().getDbFileDAO().insertFile(name, path, "", //$NON-NLS-1$
				createdBy, modifiedBy, DBMapper.OBJECT_TYPE_FOLDER);

		DBFolder dbFolder = getFolderByPath(path);
		logger.debug(this.getClass().getCanonicalName(),
				"exiting createSingleFolder"); //$NON-NLS-1$
		return dbFolder;
	}

	/**
	 * Delete the database folder object based on the given path
	 * 
	 * @param path
	 * @throws DBBaseException
	 */
	void removeFolderByPath(String path) throws DBBaseException {

		checkInitialized();

		if (!isFolderEmpty(path)) {
			removeFolderCascade(path);
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script = getRepository().getDbUtils().readScript(connection,
					DBScriptsMap.SCRIPT_REMOVE_FOLDER_BY_PATH, this.getClass());
			preparedStatement = getRepository().getDbUtils()
					.getPreparedStatement(connection, script);
			preparedStatement.setString(1, path);
			preparedStatement.setString(2, path + DBRepository.PATH_DELIMITER
					+ "%"); //$NON-NLS-1$
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
		}
		
		getRepository().getCacheManager().clear(path);
	}

	/**
	 * Cascading - delete folder and its content
	 * 
	 * @param path
	 * @throws DBBaseException
	 */
	private void removeFolderCascade(String path) throws DBBaseException {

		checkInitialized();

		getDbRepositoryDAO().getDbFileDAO().removeDocsCascade(path);

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script = getRepository().getDbUtils().readScript(connection,
					DBScriptsMap.SCRIPT_REMOVE_FOLDER_CASCADE, this.getClass());
			preparedStatement = getRepository().getDbUtils()
					.getPreparedStatement(connection, script);
			preparedStatement.setString(1, path + DBRepository.PATH_DELIMITER
					+ "%"); //$NON-NLS-1$
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
		}
		
		getRepository().getCacheManager().clear();
	}

	/**
	 * Check whether the folder is empty
	 * 
	 * @param path
	 * @return
	 * @throws DBBaseException
	 */
	private boolean isFolderEmpty(String path) throws DBBaseException {

		checkInitialized();

		boolean empty = true;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script = getRepository().getDbUtils().readScript(connection,
					DBScriptsMap.SCRIPT_IS_FOLDER_EMPTY, this.getClass());
			preparedStatement = getRepository().getDbUtils()
					.getPreparedStatement(connection, script);
			preparedStatement.setString(1, path + DBRepository.PATH_DELIMITER
					+ "%"); //$NON-NLS-1$
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				empty = (resultSet.getInt(1) <= 1);
			}
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
		}

		return empty;
	}

	/**
	 * Iterate the sub-folders and files of a given folder
	 * 
	 * @param path
	 * @return
	 * @throws DBBaseException
	 */
	public List<DBObject> getChildrenByFolder(String path)
			throws DBBaseException {

		checkInitialized();

		if (path == null || "".equals(path.trim())) { //$NON-NLS-1$
			return null;
		}

		List<DBObject> dbObjects = new ArrayList<DBObject>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script = getRepository().getDbUtils().readScript(connection,
					DBScriptsMap.SCRIPT_GET_FILES_BY_PATH, this.getClass());
			preparedStatement = getRepository().getDbUtils()
					.getPreparedStatement(connection, script);
			preparedStatement.setString(1, (IRepository.SEPARATOR.equals(path) ? "" : path) //$NON-NLS-1$
					+ DBRepository.PATH_DELIMITER + "%"); //$NON-NLS-1$
			preparedStatement.setString(2, (IRepository.SEPARATOR.equals(path) ? "" : path) //$NON-NLS-1$
					+ DBRepository.PATH_DELIMITER + "%" //$NON-NLS-1$
					+ DBRepository.PATH_DELIMITER + "%"); //$NON-NLS-1$
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				DBObject dbObject = DBMapper.dbToObject(getRepository(),
						resultSet);
				if (IRepository.SEPARATOR.equals(path) && IRepository.SEPARATOR.equals(dbObject.getPath())) {
					continue;
				}
				dbObjects.add(dbObject);
			}
			return dbObjects;
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
		}
	}
	
	
	public void renameFolderByPath(String path, String newPath)
			throws DBBaseException {
		getDbRepositoryDAO().getDbFileDAO().renameFileByPath(path, newPath);		
	}

}
