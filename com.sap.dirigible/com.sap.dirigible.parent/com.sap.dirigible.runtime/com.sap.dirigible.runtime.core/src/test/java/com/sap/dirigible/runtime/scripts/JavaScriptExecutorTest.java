package com.sap.dirigible.runtime.scripts;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.runtime.common.ICommonConstants;
import com.sap.dirigible.runtime.js.JavaScriptExecutor;
import com.sap.dirigible.runtime.utils.DataSourceUtils;

public class JavaScriptExecutorTest {

	private static final String REPOSITORY_JS_DEPLOY_PATH = "/db/dirigible/registry/public/" //$NON-NLS-1$
			+ ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	
	private static final String REPOSITORY_SANDBOX_DEPLOY_PATH = "/db/dirigible/sandbox/GUEST/" //$NON-NLS-1$
			+ ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;

	private static IRepository repository;

	@Before
	public void setUp() {
		DataSource dataSource = DataSourceUtils.createLocal();
		try {
			repository = new DBRepository(dataSource, "guest", false); //$NON-NLS-1$
//			repository.createResource(
//					REPOSITORY_JS_DEPLOY_PATH + "/root.jslib", ("$ = {}; " //$NON-NLS-1$ //$NON-NLS-2$
//							+ "$.request = request; " //$NON-NLS-1$
//							+ "$.response = response; " //$NON-NLS-1$
//							+ "$.datasource = datasource; " + "$.mail = mail; " //$NON-NLS-1$ //$NON-NLS-2$
//							+ "$.io = io; " + "$.out = out; " //$NON-NLS-1$ //$NON-NLS-2$
//							+ "$.http = http; " + "$.base64 = base64; " //$NON-NLS-1$ //$NON-NLS-2$
//							+ "$.hex = hex; " + "$.digest = digest; " //$NON-NLS-1$ //$NON-NLS-2$
//							+ "$.url = url; " + "$.user = user; " //$NON-NLS-1$ //$NON-NLS-2$
//							+ "$.upload = upload; " + "$.uuid = uuid;") //$NON-NLS-1$ //$NON-NLS-2$
//							.getBytes());
//			repository.createResource(REPOSITORY_JS_DEPLOY_PATH
//					+ "/require.jslib", ("function require(module) {" //$NON-NLS-1$ //$NON-NLS-2$
//					+ "	return loader.load(module);" + "}; " //$NON-NLS-1$ //$NON-NLS-2$
//					+ "$.require = require;").getBytes()); //$NON-NLS-1$
			repository.createResource(
					REPOSITORY_JS_DEPLOY_PATH + "/testSum.js", ("var testSum = function(){ var a=2;" //$NON-NLS-1$ //$NON-NLS-2$
							+ "var b=2; " + "var c=a+b; " + "return c;}; testSum(); ") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							.getBytes());
			
			repository.createResource(
					REPOSITORY_SANDBOX_DEPLOY_PATH + "/testSumSandbox.js", ("var testSum = function(){ var a=2;" //$NON-NLS-1$ //$NON-NLS-2$
							+ "var b=2; " + "var c=a+b; " + "return c;}; testSum(); ") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							.getBytes());
			
			
			repository.createResource(REPOSITORY_JS_DEPLOY_PATH
					+ "/test/testLibrary.jslib", //$NON-NLS-1$
					("exports.add = function(a,b) {return a+b};").getBytes()); //$NON-NLS-1$
			repository
					.createResource(
							REPOSITORY_JS_DEPLOY_PATH + "/testExports.js", //$NON-NLS-1$
							("var testExports = function() {var add = require('test/testLibrary').add; return add(5, 6); }; testExports();") //$NON-NLS-1$
									.getBytes());
			repository
			.createResource(
					REPOSITORY_JS_DEPLOY_PATH + "/testDefaultObjects.js", //$NON-NLS-1$
					(" var testDefaultObjects = function() { return (datasource !== null) ? true : false}; testDefaultObjects();") //$NON-NLS-1$
							.getBytes());
			
			
			
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteFunctionModule() {
		JavaScriptExecutor executor = new JavaScriptExecutor(repository,
				REPOSITORY_JS_DEPLOY_PATH, null);
		// TODO mock request and response
		try {
			Object object = executor.executeServiceModule(null, null,
					"/testSum.js"); //$NON-NLS-1$
			assertNotNull(object);
			assertTrue(((Double) object) == 4);

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExecuteFunctionModuleSandbox() {
		JavaScriptExecutor executor = new JavaScriptExecutor(repository,
				REPOSITORY_SANDBOX_DEPLOY_PATH, REPOSITORY_JS_DEPLOY_PATH);
		// TODO mock request and response
		try {
			Object object = executor.executeServiceModule(null, null,
					"/testSumSandbox.js"); //$NON-NLS-1$
			assertNotNull(object);
			assertTrue(((Double) object) == 4);

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteExports() {
		JavaScriptExecutor executor = new JavaScriptExecutor(repository,
				REPOSITORY_JS_DEPLOY_PATH, null);
		// TODO mock request and response
		try {
			Object object = executor.executeServiceModule(null, null,
					"/testExports.js"); //$NON-NLS-1$
			assertNotNull(object);
			assertTrue(((Double) object) == 11);

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDefaultObjects() {
		JavaScriptExecutor executor = new JavaScriptExecutor(repository,
				REPOSITORY_JS_DEPLOY_PATH, null);
		// TODO mock request and response
		try {
			Object object = executor.executeServiceModule(null, null,
					"/testDefaultObjects.js"); //$NON-NLS-1$
			assertNotNull(object);
			assertTrue(((Boolean) object));

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

}
