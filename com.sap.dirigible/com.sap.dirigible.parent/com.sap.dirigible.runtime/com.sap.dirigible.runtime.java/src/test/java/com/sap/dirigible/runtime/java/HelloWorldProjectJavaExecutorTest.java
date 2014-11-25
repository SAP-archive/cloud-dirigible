package com.sap.dirigible.runtime.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class HelloWorldProjectJavaExecutorTest extends AbstractJavaExecutorTest {
	
	@Test
	public void testHelloWorldProject() throws Exception {
		createResource(RESOURCE_PATH_HELLO_WORLD, SOURCЕ_HELLO_WORLD);
		execute(MODULE_HELLO_WORLD);
		assertEquals("Hello World!", getOutput());
		
		createResource(RESOURCE_PATH_HELLO_WORLD, SOURCE_HELLO_WORLD_UPDATED);
		execute(MODULE_HELLO_WORLD);
		assertEquals("Hello World Updated!", getOutput());
	}
}
