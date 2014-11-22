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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.zip.ZipInputStream;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.db.DBRepository;

public class ImportZipTest {

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
	public void testImportZip() {
		ZipInputStream zipInputStream = new ZipInputStream(
				ImportZipTest.class.getResourceAsStream("/testImport.zip")); //$NON-NLS-1$
		try {

			ICollection collection = repository.getCollection("/root1/import"); //$NON-NLS-1$
			if (collection.exists()) {
				collection.delete();
			}

			repository.importZip(zipInputStream, "/root1/import"); //$NON-NLS-1$

			IResource resource = repository
					.getResource("/root1/import/folder1/text1.txt"); //$NON-NLS-1$
			String read = new String(resource.getContent());
			assertEquals("text1", read); //$NON-NLS-1$

			resource = repository
					.getResource("/root1/import/folder1/folder2/image1.png"); //$NON-NLS-1$
			assertTrue(resource.isBinary());
			assertEquals("image/png", resource.getContentType()); //$NON-NLS-1$

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

}
