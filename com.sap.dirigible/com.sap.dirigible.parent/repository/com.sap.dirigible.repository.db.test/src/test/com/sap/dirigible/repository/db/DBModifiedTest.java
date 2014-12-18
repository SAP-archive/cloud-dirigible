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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBRepository;

public class DBModifiedTest {

	private static DataSource dataSource;

	@Before
	public void setUp() {
		dataSource = DBRepositoryTest.createLocal();
	}

	@Test
	public void testModified() {
		try {
			DBRepository repository = new DBRepository(dataSource, "guest1", false); //$NON-NLS-1$
			IResource resource = repository.createResource(
					"/testCollection/toBeModified.txt", //$NON-NLS-1$
					"Some content".getBytes()); //$NON-NLS-1$
			assertNotNull(resource);
			assertTrue(resource.exists());
			assertFalse(resource.isBinary());
			
			assertEquals("guest1", resource.getInformation().getModifiedBy());
			
			Date firstModified = resource.getInformation().getModifiedAt();
			
			repository = new DBRepository(dataSource, "guest2", false); //$NON-NLS-1$
			resource = repository.getResource(
					"/testCollection/toBeModified.txt"); //$NON-NLS-1$
			assertNotNull(resource);
			assertTrue(resource.exists());
			
			resource.setContent("Some modified content".getBytes());
			
			resource = repository.getResource(
					"/testCollection/toBeModified.txt"); //$NON-NLS-1$
			
			assertEquals("guest2", ((DBRepository) resource.getRepository()).getUser());
			assertEquals("guest2", resource.getInformation().getModifiedBy());			
			assertTrue(resource.getInformation().getModifiedAt().after(firstModified));
			
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		} finally {
			try {
				DBRepository repository = new DBRepository(dataSource, "guest3", false); //$NON-NLS-1$
				repository.removeResource(
						"/testCollection/toBeModified.txt"); //$NON-NLS-1$
			} catch (DBBaseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
