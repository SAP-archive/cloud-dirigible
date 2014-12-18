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

import static java.text.MessageFormat.format;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IEntityInformation;
import com.sap.dirigible.repository.db.dao.DBObject;

/**
 * The DB implementation of {@link IEntity}
 * 
 */
public abstract class DBEntity implements IEntity {

	private static final String THERE_IS_NO_ENTITY_AT_PATH_0 = Messages.getString("DBEntity.THERE_IS_NO_ENTITY_AT_PATH_0"); //$NON-NLS-1$

	private static final Logger logger = LoggerFactory
			.getLogger(DBEntity.class);

	private final DBRepository repository;

	private final DBRepositoryPath path;

	public DBEntity(DBRepository repository, DBRepositoryPath path) {
		super();
		this.repository = repository;
		this.path = path;
	}

	@Override
	public DBRepository getRepository() {
		return this.repository;
	}

	/**
	 * Returns the path of this {@link Entity} represented by an instance of
	 * {@link RepositoryPath}.
	 */
	protected DBRepositoryPath getRepositoryPath() {
		return this.path;
	}

	@Override
	public String getName() {
		return this.path.getLastSegment();
	}

	@Override
	public String getPath() {
		return this.path.toString();
	}

	@Override
	public ICollection getParent() {
		final DBRepositoryPath parentPath = this.path.getParentPath();
		if (parentPath == null) {
			return null;
		}
		return new DBCollection(repository, parentPath);
	}

	@Override
	public IEntityInformation getInformation() throws IOException {
		return new DBEntityInformation(this.path, getDBObjectSafe());
	}

	/**
	 * Returns the {@link DBObject} that matches this entity's path. If there is
	 * no such object in the real repository, then <code>null</code> is
	 * returned.
	 */
	protected DBObject getDBObject() throws IOException {
		try {
			return this.repository.getRepositoryDAO()
					.getObjectByPath(getPath());
		} catch (DBBaseException ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * Returns the {@link DBObject} that matches this entity's path. If there is
	 * no such object in the real repository, then an {@link IOException} is
	 * thrown.
	 */
	protected DBObject getDBObjectSafe() throws IOException {
		final DBObject result = getDBObject();
		if (result == null) {
			throw new IOException(format(THERE_IS_NO_ENTITY_AT_PATH_0,
					this.path.toString()));
		}
		return result;
	}

	/**
	 * Creates all ancestors of the given {@link CMISEntity} inside the
	 * repository if they don't already exist.
	 */
	protected void createAncestorsIfMissing() throws IOException {
		final ICollection parent = getParent();
		if ((parent != null) && (!parent.exists())) {
			parent.create();
		}
	}

	/**
	 * Creates all ancestors of the given {@link CMISEntity} and itself too if
	 * they don't already exist.
	 */
	protected void createAncestorsAndSelfIfMissing() throws IOException {
		createAncestorsIfMissing();
		if (!exists()) {
			create();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DBEntity)) {
			return false;
		}
		final DBEntity other = (DBEntity) obj;
		return getPath().equals(other.getPath());
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

}
