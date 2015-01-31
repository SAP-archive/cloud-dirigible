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

import java.util.Date;

import com.sap.dirigible.repository.api.RepositoryPath;
import com.sap.dirigible.repository.api.IEntityInformation;
import com.sap.dirigible.repository.db.dao.DBObject;

/**
 * The DB implementation of {@link IEntityInformation}
 * 
 */
public class DBEntityInformation implements IEntityInformation {

	private RepositoryPath wrapperPath;

	private DBObject master;

	private long size;

	public DBEntityInformation(RepositoryPath wrapperPath, DBObject master) {
		this.wrapperPath = wrapperPath;
		this.master = master;
	}

	@Override
	public String getName() {
		return this.wrapperPath.getLastSegment();
	}

	@Override
	public String getPath() {
		return this.wrapperPath.toString();
	}

	@Override
	public int getPermissions() {
		return this.master.getPermissions();
	}

	@Override
	public Long getSize() {
		return this.size;
	}

	@Override
	public String getCreatedBy() {
		return this.master.getCreatedBy();
	}

	@Override
	public Date getCreatedAt() {
		return this.master.getCreatedAt();
	}

	@Override
	public String getModifiedBy() {
		return this.master.getModifiedBy();
	}

	@Override
	public Date getModifiedAt() {
		return this.master.getModifiedAt();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DBEntityInformation)) {
			return false;
		}
		final DBEntityInformation other = (DBEntityInformation) obj;
		return getPath().equals(other.getPath());
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

}
