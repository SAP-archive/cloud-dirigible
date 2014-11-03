package com.sap.dirigible.runtime.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TwoProjectsJavaExecutorTest extends AbstractJavaExecutorTest {

	// TODO Add more tests! Like execute 2 services from one project. Use Utils
	// class from another project and so on. Project with errors in the sandbox
	// does not affects the other projects

	@Test
	public void testExecuteTwoServicesFromTwoProjectsOutputs() throws Exception {
		createResource(HELLO_WORLD_RESOURCE_PATH, HELLO_WORLD_SOURCЕ);
		createResource(UTILS_RESOURCE_PATH, UTILS_SOURCЕ);
		createResource(CALCULATOR_RESOURCE_PATH, CALCULATOR_SOURCЕ);
		
		execute(HELLO_WORLD_MODULE);
		assertEquals("Hello World!",  getOutput());

		execute(CALCULATOR_MODULE);
		assertEquals("Sum of 3 + 5 = 8", getOutput());
	}
	
	@Test
	public void testExecuteTwoServicesFromOneProjectOutputs() throws Exception {
		createResource(ENDPOINT1_RESOURCE_PATH, ENDPOINT1_SOURCЕ);
		createResource(ENDPOINT2_RESOURCE_PATH, ENDPOINT2_SOURCЕ);
		
		execute(ENDPOINT1_MODULE);
		assertEquals("Hello from Endpoint 1!", getOutput());
		
		execute(ENDPOINT2_MODULE);
		assertEquals("Hello from Endpoint 2!", getOutput());
	}
}
