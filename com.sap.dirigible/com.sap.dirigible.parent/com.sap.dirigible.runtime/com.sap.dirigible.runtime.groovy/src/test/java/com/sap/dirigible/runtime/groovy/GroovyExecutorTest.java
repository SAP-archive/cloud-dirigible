package com.sap.dirigible.runtime.groovy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import groovy.lang.Binding;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.runtime.groovy.GroovyExecutor;

public class GroovyExecutorTest {

	private static final String REPOSITORY_JS_DEPLOY_PATH = "/db/dirigible/registry/public/" //$NON-NLS-1$
			+ ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;

	private static IRepository repository;

	@Before
	public void setUp() throws Exception {
		DataSource dataSource = createLocal();

		try {
			repository = new DBRepository(dataSource, "guest", false);

			repository
					.createResource(
							REPOSITORY_JS_DEPLOY_PATH + "/hello1.groovy", ("class Hello1 { void hello() {\n" //$NON-NLS-1$ //$NON-NLS-2$
									+ "println(\"hello world\")}}\n "
									+ "def obj = new Hello1();\n"
									+ "obj.hello();\n" + "result = 5;") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									.getBytes());

			repository.createResource(
					REPOSITORY_JS_DEPLOY_PATH + "/calee.groovy", ("class Calee { void hello() {\n" //$NON-NLS-1$ //$NON-NLS-2$
							+ "println(\"hello world from module\");}}") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							.getBytes());

			// repository.createResource(
			//					REPOSITORY_JS_DEPLOY_PATH + "/Calculator.groovy", ("class Calculator { void addTwoNumbers(int a, int b) {" //$NON-NLS-1$ //$NON-NLS-2$
			//							+ " return a+b}}") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			// .getBytes());

			repository.createResource(
					REPOSITORY_JS_DEPLOY_PATH + "/main.groovy", ("import calee;\n" //$NON-NLS-1$ //$NON-NLS-2$
							// +"println(\"abc\")")
							+ "def obj = new Calee();\n" + "obj.hello();" + "result = 11;") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							.getBytes());

		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testHelloWorldModule() {
		GroovyExecutor groovyExecutor = new GroovyExecutor(repository, REPOSITORY_JS_DEPLOY_PATH, null);

		try {
			Binding binding = ((Binding) groovyExecutor.executeServiceModule(null, null,
					"/hello1.groovy")); //$NON-NLS-1$
			assertNotNull(binding);
			assertEquals(5, binding.getVariable("result"));

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testImportModule() {
		GroovyExecutor groovyExecutor = new GroovyExecutor(repository, REPOSITORY_JS_DEPLOY_PATH, null);

		try {
			Binding binding = ((Binding) groovyExecutor.executeServiceModule(null, null,
					"/main.groovy")); //$NON-NLS-1$
			assertNotNull(binding);
			assertEquals(11, binding.getVariable("result"));

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	public static DataSource createLocal() {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("derby"); //$NON-NLS-1$
		dataSource.setCreateDatabase("create"); //$NON-NLS-1$
		return dataSource;
	}
}
