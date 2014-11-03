package com.sap.dirigible.runtime.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class HelloWorldProjectJavaExecutorTest extends AbstractJavaExecutorTest {
	
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
}
