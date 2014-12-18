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

import java.util.List;

import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBRepository;

/**
 * Internal representation of a Folder/Collection kind of object
 * 
 */
public class DBFolder extends DBObject {

	public DBFolder(DBRepository repository) {
		super(repository);
	}

	public void deleteTree() throws DBBaseException {
		getRepository().getRepositoryDAO().removeFolderByPath(getPath());
	}

	public List<DBObject> getChildren() throws DBBaseException {
		List<DBObject> result = getRepository().getRepositoryDAO()
				.getChildrenByFolder(getPath());
		return result;
	}

	public void createFolder(String name) throws DBBaseException {
		getRepository().getRepositoryDAO().createFolder(
				getRepository().getDbUtils().normalizePath(getPath(), name));
	}

	public void createFile(String name, byte[] content, boolean isBinary,
			String contentType) throws DBBaseException {
		getRepository().getRepositoryDAO().createFile(
				getRepository().getDbUtils().normalizePath(getPath(), name),
				content, isBinary, contentType);
	}
	
	public void renameFolder(String newPath) throws DBBaseException {
		getRepository().getRepositoryDAO().renameFolder(getPath(), newPath);
	}

}
