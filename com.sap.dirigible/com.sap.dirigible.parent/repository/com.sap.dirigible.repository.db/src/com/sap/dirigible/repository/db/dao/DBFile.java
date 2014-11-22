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

import java.util.Arrays;

import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBRepository;

/**
 * Internal representation of a File/Resource kind of object
 * 
 */
public class DBFile extends DBObject {

	private boolean binary = false;

	private String contentType;

	public DBFile(DBRepository repository, boolean isBinary, String contentType) {
		super(repository);
		this.binary = isBinary;
		this.contentType = contentType;
	}

	public void delete() throws DBBaseException {
		getRepository().getRepositoryDAO().removeFileByPath(getPath());
	}

	 public void rename(String newPath) throws DBBaseException {
		 getRepository().getRepositoryDAO().renameFile(getPath(), newPath);
	 }

	public byte[] getData() throws DBBaseException {
		if (isBinary()) {
			byte[] data = getRepository().getRepositoryDAO().getBinary(this);
			return data;
		} else {
			byte[] data = getRepository().getRepositoryDAO().getDocument(this);
			return data;
		}
	}

	public void setData(byte[] content) throws DBBaseException {
		byte[] old = getData();
		if (old != null) {
			if (Arrays.equals(old, content)) {
				return;
			}
		}
		if (isBinary()) {
			getRepository().getRepositoryDAO().setBinary(this, content,
					getContentType());
		} else {
			getRepository().getRepositoryDAO().setDocument(this, content);
		}
	}

	public boolean isBinary() {
		return binary;
	}

	public String getContentType() {
		return contentType;
	}
}
