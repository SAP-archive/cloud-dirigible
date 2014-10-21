package com.sap.dirigible.runtime.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;


public class JavaExecutorTest {
	
	private static final String LIB_DIRECTORY = "src/test/resources/lib";
	
	private static final String REPOSITORY_DEPLOY_PATH = "/db/dirigible/registry/public/" + ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	private static final String USER = "guest";
	
	private static final File HELLO_WORLD_SOURCЕ = new File("src/test/resources/src/HelloWorld.java");
	private static final File HELLO_WORLD_UPDATED_SOURCE = new File("src/test/resources/src/HelloWorld_Updated.java");
	private static final String HELLO_WORLD_MODULE = "/project/HelloWorld.java";
	private static final String HELLO_WORLD_RESOURCE_PATH = REPOSITORY_DEPLOY_PATH + HELLO_WORLD_MODULE;
	
	private static final File CALCULATOR_SOURCЕ = new File("src/test/resources/src/Calculator.java");
	private static final File CALCULATOR_UPDATED_SOURCE = new File("src/test/resources/src/Calculator_Updated.java");
	private static final String CALCULATOR_MODULE = "/project/Calculator.java";
	private static final String CALCULATOR_RESOURCE_PATH = REPOSITORY_DEPLOY_PATH + CALCULATOR_MODULE;

	private static final File UTILS_SOURCЕ = new File("src/test/resources/src/Utils.java");
	private static final String UTILS_MODULE = "/project/Utils.java";
	private static final String UTILS_RESOURCE_PATH = REPOSITORY_DEPLOY_PATH + UTILS_MODULE;
	
	private IRepository repository;
	private AbstractScriptExecutor executor;
	
	private PrintStream backupOut;
	private PrintStream backupErr;

	private ByteArrayOutputStream baos;
	private PrintStream printStream;

	@Before
	public void setUp() throws Exception {
		repository = new DBRepository(createLocal(), USER, true);
		executor = new JavaExecutorStub(getRepository(), new File(LIB_DIRECTORY), REPOSITORY_DEPLOY_PATH, null);
		
		backupOut = System.out;
		backupErr = System.err;
		
		baos = new ByteArrayOutputStream();
		printStream = new PrintStream(baos);
		
		System.setOut(printStream);
		System.setErr(printStream);
	}

	public static DataSource createLocal() {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("derby"); //$NON-NLS-1$
		dataSource.setCreateDatabase("create"); //$NON-NLS-1$
		return dataSource;
	}
	
	@After
	public void tearDown() throws Exception {
		if (baos != null) {
			baos.close();
		}
		if(printStream != null){
			printStream.close();
		}
		System.setOut(backupOut);
		System.setErr(backupErr);
	}

	private IRepository getRepository() {
		return repository;
	}

	private String getOutput() throws IOException {
		String output = null;
		if(baos != null){
			output = baos.toString();
			baos.reset();
		}
		return output;
	}

	private byte[] readSource(File source) throws IOException{
		return FileUtils.readFileToString(source).getBytes();
	}
	
	private long getExecutionTime(String module) throws IOException {
		long startTime = System.currentTimeMillis();
		execute(module);
		return System.currentTimeMillis() - startTime;
	}
	
	private Object execute(String module) throws IOException {
		return executor.executeServiceModule(null, null, module);
	}
	
	private void createResource(String path, File source) throws IOException {
		repository.createResource(path, readSource(source));
	}
	
	private void assertCacheExecutionTime(long firstExecutionTime, long secondExecutionTime) {
		String format = "Cache is not working. Second execution time \"%d ms\" (execution with cache) should be less than first execution time \"%d ms\""; 
		String message = String.format(format, secondExecutionTime, firstExecutionTime);
		assertTrue(message, firstExecutionTime > secondExecutionTime);
	}
	
	@Test
	public void testHelloWorldClassWithCacheExecutionTime() throws Exception {
		createResource(HELLO_WORLD_RESOURCE_PATH, HELLO_WORLD_SOURCЕ);
		
		long firstExecutionTime = getExecutionTime(HELLO_WORLD_MODULE);
		long secondExecutionTime = getExecutionTime(HELLO_WORLD_MODULE);
		
		assertCacheExecutionTime(firstExecutionTime, secondExecutionTime);
	}

	@Test
	public void testHelloWorldClassWithCacheOutput() throws Exception {
		createResource(HELLO_WORLD_RESOURCE_PATH, HELLO_WORLD_SOURCЕ);
		execute(HELLO_WORLD_MODULE);
		assertEquals("Hello World!", getOutput());
		
		createResource(HELLO_WORLD_RESOURCE_PATH, HELLO_WORLD_UPDATED_SOURCE);
		execute(HELLO_WORLD_MODULE);
		assertEquals("Hello World Updated!", getOutput());
	}

	@Test
	public void testCalculatorAndUtilsClassesWithCacheExecutionTime() throws Exception {
		createResource(UTILS_RESOURCE_PATH, UTILS_SOURCЕ);
		createResource(CALCULATOR_RESOURCE_PATH, CALCULATOR_SOURCЕ);
		long firstExecutionTime = getExecutionTime(CALCULATOR_MODULE);
		
		createResource(CALCULATOR_RESOURCE_PATH, CALCULATOR_UPDATED_SOURCE);
		long secondExecutionTime = getExecutionTime(CALCULATOR_MODULE);
		
		assertCacheExecutionTime(firstExecutionTime, secondExecutionTime);
	}
	
	@Test
	public void testCalculatorAndUtilsClassesWithCacheOutput() throws Exception {
		createResource(UTILS_RESOURCE_PATH, UTILS_SOURCЕ);
		createResource(CALCULATOR_RESOURCE_PATH, CALCULATOR_SOURCЕ);
		execute(CALCULATOR_MODULE);
		assertEquals("Sum of 3 + 5 = 8", getOutput());
		
		createResource(CALCULATOR_RESOURCE_PATH, CALCULATOR_UPDATED_SOURCE);
		execute(CALCULATOR_MODULE);
		assertEquals("Cache was not updated", "Sum of 5 + 5 = 10", getOutput());
	}
}
