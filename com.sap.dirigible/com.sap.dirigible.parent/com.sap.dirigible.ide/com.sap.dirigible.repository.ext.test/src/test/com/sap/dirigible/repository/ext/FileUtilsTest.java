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

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.utils.FileUtils;

public class FileUtilsTest {

	private static IRepository repository;
	
	@Before
	public void setUp() {
		DBRepositoryTest.setUp();
		repository = DBRepositoryTest.getRepository();
	}

	@Test
	public void testcopyCollectionToDirectory() {
		try {
			File temp = FileUtils.createTempDirectory("test");
			System.out.println(temp.getCanonicalPath());
			repository.createResource("/db/dirigible/root/test", "test".getBytes());
			repository.createResource("/db/dirigible/root/test2", "test2".getBytes());
			repository.createResource("/db/dirigible/root/subfolder/test3", "test3".getBytes());
			ICollection collection = repository.getCollection("/db/dirigible/root/");
			FileUtils.copyCollectionToDirectory(collection, temp, new String[]{"/db/dirigible/root"});
			//assertTrue(contains(extensionPoints,"extensionPoint1"));
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
		
}
