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

import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBRepository;

/**
 * Internal representation of a File/Resource kind of object
 * 
 */
public class DBFileVersion extends DBFile {

	private int version;

	private byte[] bytes;

	public DBFileVersion(DBRepository repository, boolean isBinary,
			String contentType, int version, byte[] bytes) {
		super(repository, isBinary, contentType);
		this.version = version;
		this.bytes = bytes;
	}

	@Override
	public byte[] getData() throws DBBaseException {
		byte[] data = this.bytes;
		return data;
	}

	public int getVersion() {
		return version;
	}

}
