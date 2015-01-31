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

package com.sap.dirigible.repository.ext.db.dialect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sap.dirigible.repository.ext.db.DBSupportedTypesMap;


public class SybaseDBSpecifier implements IDialectSpecifier {

	private static final String SYBASE_TIMESTAMP = "DATETIME"; //$NON-NLS-1$
	private static final String SYBASE_FLOAT = "REAL"; //$NON-NLS-1$
	private static final String SYBASE_BLOB = "IMAGE"; //$NON-NLS-1$
	private static final String SYBASE_CURRENT_TIMESTAMP = "GETDATE()"; //$NON-NLS-1$

	@Override
	public String specify(String sql) {
		sql = sql.replace(DIALECT_CURRENT_TIMESTAMP, SYBASE_CURRENT_TIMESTAMP);
		sql = sql.replace(DIALECT_TIMESTAMP, SYBASE_TIMESTAMP);
		sql = sql.replace(DIALECT_BLOB, SYBASE_BLOB);
		return sql;
	}

	@Override
	public String getSpecificType(String commonType) {
		if (DBSupportedTypesMap.TIMESTAMP.equals(commonType)) {
			return SYBASE_TIMESTAMP;
		}
		if (DBSupportedTypesMap.FLOAT.equals(commonType)) {
			return SYBASE_FLOAT;
		}
		if (DBSupportedTypesMap.BLOB.equals(commonType)) {
			return SYBASE_BLOB;
		}

		return commonType;
	}

	@Override
	public String createLimitAndOffset(int limit, int offset) {
		return "";  //$NON-NLS-1$
	}
	
	@Override
	public String createTopAndStart(int limit, int offset) {
		return String.format("TOP %d ROWS START AT %d", limit, offset);
	}

	@Override
	public boolean isSchemaFilterSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSchemaFilterScript() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlterAddOpen() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getAlterAddClose() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public InputStream getBinaryStream(ResultSet resultSet, String columnName) throws SQLException {
		return new ByteArrayInputStream(resultSet.getBytes(columnName));
	}

}
