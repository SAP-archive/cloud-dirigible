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

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.security.SecurityManager;

public class SecurityManagerTest {

	private static DataSource dataSource;
	
	private static IRepository repository;
	
	private static SecurityManager securityManager;
	

	@Before
	public void setUp() {
		DBRepositoryTest.setUp();
		dataSource = DBRepositoryTest.getDataSource();
		repository = DBRepositoryTest.getRepository();
		securityManager = SecurityManager.getInstance(repository, dataSource);
	}

	@Test
	public void testSecureLocation() {
		try {
			securityManager.secureLocation("/location1", null);
			assertTrue(securityManager.isSecuredLocation("/location1"));
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUnsecureLocation() {
		try {
			securityManager.secureLocation("/location1", null);
			assertTrue(securityManager.isSecuredLocation("/location1"));
			securityManager.unsecureLocation("/location1");
			assertFalse(securityManager.isSecuredLocation("/location1"));
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	

}
