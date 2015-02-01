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

package com.sap.dirigible.repository.rcp;

import java.util.List;

import com.sap.dirigible.repository.api.RepositoryPath;

/**
 * Internal representation of a Folder/Collection kind of object
 * 
 */
public class RCPFolder extends RCPObject {

	public RCPFolder(RCPRepository repository) {
		super(repository);
	}

	public void deleteTree() throws RCPBaseException {
		getRepository().getRepositoryDAO().removeFolderByPath(getPath());
	}

	public List<RCPObject> getChildren() throws RCPBaseException {
		List<RCPObject> result = getRepository().getRepositoryDAO()
				.getChildrenByFolder(getPath());
		return result;
	}

	public void createFolder(String name) throws RCPBaseException {
		getRepository().getRepositoryDAO().createFolder(
				RepositoryPath.normalizePath(getPath(), name));
	}

	public void createFile(String name, byte[] content, boolean isBinary,
			String contentType) throws RCPBaseException {
		getRepository().getRepositoryDAO().createFile(
				RepositoryPath.normalizePath(getPath(), name),
				content, isBinary, contentType);
	}
	
	public void renameFolder(String newPath) throws RCPBaseException {
		getRepository().getRepositoryDAO().renameFolder(getPath(), newPath);
	}

}
