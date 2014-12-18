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
import java.util.Arrays;
import java.util.Random;

import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.db.DBRepository;

public class DBBigTextTest {

	private static IRepository repository;

	@Before
	public void setUp() {
		DataSource dataSource = DBRepositoryTest.createLocal();
		try {
			repository = new DBRepository(dataSource, "guest", false); //$NON-NLS-1$
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testBigText() {
		IResource resource = null;
		try {
			byte[] bytes = new byte[400000];
			for (int i = 0; i < bytes.length; i++) {
				int ch = 'A' + new Random().nextInt(20);
				bytes[i] = (byte) ch;
			}

			String base64 = DatatypeConverter.printBase64Binary(bytes);

			resource = repository.createResource(
					"/testCollection/toBeRemoved1.txt", bytes, false, //$NON-NLS-1$
					"text/plain"); //$NON-NLS-1$
			assertNotNull(resource);
			assertTrue(resource.exists());
			assertFalse(resource.isBinary());

			IResource resourceBack = repository
					.getResource("/testCollection/toBeRemoved1.txt"); //$NON-NLS-1$
			String base64back = DatatypeConverter
					.printBase64Binary(resourceBack.getContent());

			assertEquals(base64, base64back);

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		} finally {
			try {
				if (resource != null && resource.exists()) {
					repository.removeResource("/testCollection/toBeRemoved1.txt"); //$NON-NLS-1$
					resource = repository
							.getResource("/testCollection/toBeRemoved1.txt"); //$NON-NLS-1$
					assertNotNull(resource);
					assertFalse(resource.exists());
				}
			} catch (IOException e) {
				assertTrue(e.getMessage(), false);
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testSpacesText() {
		IResource resource = null;
		try {
			byte[] bytes = new byte[400000];
			for (int i = 0; i < bytes.length; i++) {
				int ch = ' ';
				bytes[i] = (byte) ch;
			}

			resource = repository.createResource(
					"/testCollection/toBeRemoved2.txt", bytes, false, //$NON-NLS-1$
					"text/plain"); //$NON-NLS-1$
			assertNotNull(resource);
			assertTrue(resource.exists());
			assertFalse(resource.isBinary());

			IResource resourceBack = repository
					.getResource("/testCollection/toBeRemoved2.txt"); //$NON-NLS-1$

			byte[] bytesBack = resourceBack.getContent();
			assertEquals(bytes.length, bytesBack.length);
			assertTrue(Arrays.equals(bytes, bytesBack));

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		} finally {
			try {
				if (resource != null && resource.exists()) {
					repository.removeResource("/testCollection/toBeRemoved2.txt"); //$NON-NLS-1$
					resource = repository
							.getResource("/testCollection/toBeRemoved2.txt"); //$NON-NLS-1$
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
