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

package test.com.sap.dirigible.repository.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.db.DBRepository;

public class DBCacheTest {

	private static IRepository repository;
	
	private static boolean disabled = false;

	@Before
	public void setUp() {
		DataSource dataSource = DBRepositoryTest.createLocal();
		try {
			repository = new DBRepository(dataSource, "guest", false, !disabled); //$NON-NLS-1$
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testCacheText() {
		IResource resource = null;
		try {
			resource = repository.createResource(
					"/testCollection/toBeRemoved1Cached.txt", "cached file".getBytes()); //$NON-NLS-1$
			assertNotNull(resource);
			assertTrue(resource.exists());
			assertFalse(resource.isBinary());

			// TODO

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		} finally {
			try {
				if (resource != null && resource.exists()) {
					repository.removeResource("/testCollection/toBeRemoved1Cached.txt"); //$NON-NLS-1$
					resource = repository
							.getResource("/testCollection/toBeRemoved1Cached.txt"); //$NON-NLS-1$
					assertNotNull(resource);
					assertFalse(resource.exists());
				}
			} catch (IOException e) {
				assertTrue(e.getMessage(), false);
				e.printStackTrace();
			}
		}
	}
	
}
