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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.extensions.ExtensionDefinition;
import com.sap.dirigible.repository.ext.extensions.ExtensionManager;
import com.sap.dirigible.repository.ext.extensions.ExtensionPointDefinition;

public class ExtensionManagerTest {

	private static DataSource dataSource;
	
	private static IRepository repository;
	
	private static ExtensionManager extensionManager;
	

	@Before
	public void setUp() {
		DBRepositoryTest.setUp();
		dataSource = DBRepositoryTest.getDataSource();
		repository = DBRepositoryTest.getRepository();
		extensionManager = ExtensionManager.getInstance(repository, dataSource);
	}

	@Test
	public void testCreateExtensionPoint() {
		try {
			extensionManager.removeExtensionPoint("extensionPoint1");
			extensionManager.createExtensionPoint("extensionPoint1", "test extension point", null);
			String[] extensionPoints = extensionManager.getExtensionPoints();
			assertTrue(contains(extensionPoints,"extensionPoint1"));
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	private boolean contains(String[] array, String string) {
		for (int i = 0; i < array.length; i++) {
			if (array[0] != null
					&& array[0].equals(string)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void testCreateExtension() {
		try {
			extensionManager.removeExtensionPoint("extensionPoint1");
			extensionManager.createExtensionPoint("extensionPoint1", "test extension point", null);
			extensionManager.createExtension("extension1", "extensionPoint1", "test extension", null);
			String[] extensions = extensionManager.getExtensions("extensionPoint1");
			assertTrue(contains(extensions,"extension1"));
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRemoveExtension() {
		try {
			extensionManager.removeExtensionPoint("extensionPoint1");
			extensionManager.createExtensionPoint("extensionPoint1", "test extension point", null);
			extensionManager.createExtension("extension1", "extensionPoint1", "test extension", null);
			String[] extensions = extensionManager.getExtensions("extensionPoint1");
			assertTrue(contains(extensions,"extension1"));
			
			extensionManager.removeExtension("extension1", "extensionPoint1");
			extensions = extensionManager.getExtensions("extensionPoint1");
			assertFalse(contains(extensions,"extension1"));
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}


	@Test
	public void testGetExtensionPoint() {
		try {
			extensionManager.removeExtensionPoint("extensionPoint1");
			extensionManager.createExtensionPoint("extensionPoint1", "test extension point", null);
			ExtensionPointDefinition extensionPointDefinition = extensionManager.getExtensionPoint("extensionPoint1");
			assertNotNull(extensionPointDefinition);
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetExtension() {
		try {
			extensionManager.removeExtensionPoint("extensionPoint1");
			extensionManager.createExtensionPoint("extensionPoint1", "test extension point", null);
			extensionManager.createExtension("extension1", "extensionPoint1", "test extension", null);
			ExtensionDefinition extensionDefinition = extensionManager.getExtension("extension1", "extensionPoint1");
			assertNotNull(extensionDefinition);
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateExtensionPoint() {
		try {
			extensionManager.removeExtensionPoint("extensionPoint1");
			extensionManager.createExtensionPoint("extensionPoint1", "test extension point", null);
			ExtensionPointDefinition extensionPointDefinition = extensionManager.getExtensionPoint("extensionPoint1");
			assertNotNull(extensionPointDefinition);
			assertEquals("test extension point", extensionPointDefinition.getDescription());
			extensionManager.updateExtensionPoint("extensionPoint1", "test extension point updated", null);
			extensionPointDefinition = extensionManager.getExtensionPoint("extensionPoint1");
			assertEquals("test extension point updated", extensionPointDefinition.getDescription());
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateExtension() {
		try {
			extensionManager.removeExtensionPoint("extensionPoint1");
			extensionManager.createExtensionPoint("extensionPoint1", "test extension point", null);
			extensionManager.createExtension("extension1", "extensionPoint1", "test extension", null);
			ExtensionDefinition extensionDefinition = extensionManager.getExtension("extension1", "extensionPoint1");
			assertNotNull(extensionDefinition);
			assertEquals("test extension", extensionDefinition.getDescription());
			extensionManager.updateExtension("extension1", "extensionPoint1", "test extension updated", null);
			extensionDefinition = extensionManager.getExtension("extension1", "extensionPoint1");
			assertEquals("test extension updated", extensionDefinition.getDescription());
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
}
