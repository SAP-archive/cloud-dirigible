package com.sap.dirigible.runtime.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaclulatorProjectJavaExecutorTest extends AbstractJavaExecutorTest {

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
