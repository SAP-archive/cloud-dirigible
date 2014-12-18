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

package com.sap.dirigible.repository.db;

import java.io.IOException;
import java.util.Date;

import com.sap.dirigible.repository.api.IResourceVersion;
import com.sap.dirigible.repository.db.dao.DBFileVersion;
import com.sap.dirigible.repository.db.dao.DBObject;

public class DBResourceVersion implements IResourceVersion {

	private final DBRepository repository;

	private final DBRepositoryPath path;

	private int version;

	private DBFileVersion fileVersion;

	public DBResourceVersion(DBRepository repository, DBRepositoryPath path,
			int version) {
		super();
		this.repository = repository;
		this.path = path;
		this.version = version;
		this.fileVersion = getRepository().getRepositoryDAO()
				.getFileVersionByPath(getPath(), version);
	}

	public DBRepository getRepository() {
		return repository;
	}

	@Override
	public String getPath() {
		return this.path.toString();
	}

	protected DBObject getDBObject() throws IOException {
		return this.fileVersion;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public byte[] getContent() throws IOException {
		return this.fileVersion.getData();
	}

	@Override
	public boolean isBinary() {
		return this.fileVersion.isBinary();
	}

	@Override
	public String getContentType() {
		return this.fileVersion.getContentType();
	}

	@Override
	public String getCreatedBy() {
		return this.fileVersion.getCreatedBy();
	}

	@Override
	public Date getCreatedAt() {
		return this.fileVersion.getCreatedAt();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DBResourceVersion)) {
			return false;
		}
		final DBResourceVersion other = (DBResourceVersion) obj;
		return getPath().equals(other.getPath());
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

}
