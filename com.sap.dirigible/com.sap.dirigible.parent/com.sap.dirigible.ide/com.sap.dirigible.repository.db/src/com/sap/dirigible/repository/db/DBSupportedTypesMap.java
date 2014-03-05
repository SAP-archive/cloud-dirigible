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

public class DBSupportedTypesMap {

	public static final String VARCHAR = "VARCHAR"; //$NON-NLS-1$
	public static final String TINYINT = "TINYINT"; //$NON-NLS-1$
	public static final String TIMESTAMP = "TIMESTAMP"; //$NON-NLS-1$
	public static final String TIME = "TIME"; //$NON-NLS-1$
	public static final String SMALLINT = "SMALLINT"; //$NON-NLS-1$
	public static final String NVARCHAR = "NVARCHAR"; //$NON-NLS-1$
	public static final String NUMERIC = "NUMERIC"; //$NON-NLS-1$
	public static final String INTEGER = "INTEGER"; //$NON-NLS-1$
	public static final String REAL = "REAL"; //$NON-NLS-1$
	public static final String FLOAT = "FLOAT"; //$NON-NLS-1$
	public static final String DOUBLE = "DOUBLE"; //$NON-NLS-1$
	public static final String DECIMAL = "DECIMAL"; //$NON-NLS-1$
	public static final String DATE = "DATE"; //$NON-NLS-1$
	public static final String CLOB = "CLOB"; //$NON-NLS-1$
	public static final String CHAR = "CHAR"; //$NON-NLS-1$
	public static final String BOOLEAN = "BOOLEAN"; //$NON-NLS-1$
	public static final String BLOB = "BLOB"; //$NON-NLS-1$
	public static final String BIT = "BIT"; //$NON-NLS-1$
	public static final String BINARY = "BINARY"; //$NON-NLS-1$
	public static final String BIGINT = "BIGINT"; //$NON-NLS-1$
	public static final String UNSUPPORTED_TYPE = Messages.getString("DBSupportedTypesMap.UNSUPPORTED_TYPE"); //$NON-NLS-1$

	/**
	 * Gives the meaningful subset of all the existing types in JDBC
	 * specification which are supported in this framework
	 * 
	 * @return
	 */
	public static String[] getSupportedTypes() {
		return new String[] { VARCHAR, CHAR, INTEGER, BIGINT, SMALLINT, REAL,
				DOUBLE, DATE, TIME, TIMESTAMP, BLOB };
	}

	/**
	 * Retrieve the type name by given JDBC type as integer
	 * 
	 * @param type
	 * @return
	 */
	public static String getTypeName(int type) {
		String typeName = null;
		switch (type) {
		case -5:
			typeName = BIGINT;
			break;
		case -2:
			typeName = BINARY;
			break;
		case -7:
			typeName = BIT;
			break;
		case 2004:
			typeName = BLOB;
			break;
		case 16:
			typeName = BOOLEAN;
			break;
		case 1:
			typeName = CHAR;
			break;
		case 2005:
			typeName = CLOB;
			break;
		case 91:
			typeName = DATE;
			break;
		case 3:
			typeName = DECIMAL;
			break;
		case 8:
			typeName = DOUBLE;
			break;
		case 6:
			typeName = FLOAT;
			break;
		case 4:
			typeName = INTEGER;
			break;
		case 2:
			typeName = NUMERIC;
			break;
		case -9:
			typeName = NVARCHAR;
			break;
		case 5:
			typeName = SMALLINT;
			break;
		case 92:
			typeName = TIME;
			break;
		case 93:
			typeName = TIMESTAMP;
			break;
		case -6:
			typeName = TINYINT;
			break;
		case 7:
			typeName = REAL;
			break;
		case 12:
			typeName = VARCHAR;
			break;
		default:
			typeName = UNSUPPORTED_TYPE + type;
			break;
		}

		return typeName;
	}

}
