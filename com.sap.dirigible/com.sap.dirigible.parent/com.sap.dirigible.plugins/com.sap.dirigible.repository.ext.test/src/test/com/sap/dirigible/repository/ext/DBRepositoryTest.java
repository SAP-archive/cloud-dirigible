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

package test.com.sap.dirigible.repository.ext;

import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBRepository;

public class DBRepositoryTest {

	private static DataSource dataSource;
	
	private static IRepository repository;
	

	public static void setUp() {
		dataSource = createLocal();
		try {
			repository = new DBRepository(dataSource, "guest", false); //$NON-NLS-1$
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	private static DataSource createLocal() {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("derby"); //$NON-NLS-1$
		dataSource.setCreateDatabase("create"); //$NON-NLS-1$
		return dataSource;
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	public static IRepository getRepository() {
		return repository;
	}
}
