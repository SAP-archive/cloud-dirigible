package com.sap.dirigible.runtime.scripts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.runtime.common.ICommonConstants;
import com.sap.dirigible.runtime.ruby.RubyExecutor;

public class RubyScriptExecutorTest {
	private static final String DUMMY_MODULE_RB = "/dummy_module.rb";
	private static final String MAIN_MODULE_RB = "/main_module.rb";
	private static final String REPOSITORY_DEPLOY_PATH = "/db/dirigible/registry/public/"
			+ ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	private static IRepository repository;
	private RubyExecutor rubyExecutor;

	public static DataSource createLocal() {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("derby");
		dataSource.setCreateDatabase("create");
		return dataSource;
	}

	@Before
	public void setUp() throws Exception {
		DataSource dataSource = createLocal();

		try {
			repository = new DBRepository(dataSource, "guest", false);

			repository.createResource(REPOSITORY_DEPLOY_PATH + DUMMY_MODULE_RB,
					("module DummyModule\n" + "def self.give_me_five\n" + "5\n" + "end\n" + "end\n"
							+ "DummyModule.give_me_five()").getBytes());

			repository
					.createResource(
							REPOSITORY_DEPLOY_PATH + MAIN_MODULE_RB,
							("require \"" + DUMMY_MODULE_RB
									+ "\"\nputs 'Hello world from ruby module!'\n" + "\nDummyModule.give_me_five()")
									.getBytes());

			rubyExecutor = new RubyExecutor(repository, REPOSITORY_DEPLOY_PATH, null);

			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testSimpleModule() {
		try {
			long expectedValue = 5;
			long actualValue = (Long) rubyExecutor.executeServiceModule(null, null, null,
					DUMMY_MODULE_RB);
			assertNotNull(actualValue);
			assertEquals(expectedValue, actualValue);

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
		
	}

	@Test
	public void testRequireModule() {
		try {
			long expectedValue = 5;
			long actualValue = (Long) rubyExecutor.executeServiceModule(null, null, null,
					MAIN_MODULE_RB);
			assertNotNull(actualValue);
			assertEquals(expectedValue, actualValue);

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
}
