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
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.ext.conf.ConfigurationStore;
import com.sap.dirigible.repository.ext.conf.IConfigurationStore;
import com.sap.dirigible.repository.ext.utils.FileUtils;

public class ConfigurationStoreTest {

	private static IRepository repository;
	
	@Before
	public void setUp() {
		DBRepositoryTest.setUp();
		repository = DBRepositoryTest.getRepository();
	}

	@Test
	public void testcopyCollectionToDirectory() {
		try {
			IConfigurationStore configurationStorage = 
					new ConfigurationStore(repository);
			
			Properties properties = new Properties();
			properties.put("property1", "value1");
			properties.put("property2", "value2");
			configurationStorage.setGlobalSettings("/myConfig", "special_settings", properties);

			String path = IRepositoryPaths.CONF_REGISTRY + "/myConfig/special_settings.properties";
			assertTrue(repository.hasResource(path));
			IResource resource = repository.getResource(path);
			
			Properties retrieved = new Properties();
			retrieved.load(new ByteArrayInputStream(resource.getContent()));
			
			String var1 = retrieved.getProperty("property1");
			String var2 = retrieved.getProperty("property2");
			
			assertEquals("value1", var1);
			assertEquals("value2", var2);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
		
}
