package com.sap.dirigible.runtime.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaclulatorProjectJavaExecutorTest extends AbstractJavaExecutorTest {
	
	@Test
	public void testCalculatorAndUtilsProjects() throws Exception {
		createResource(RESOURCE_PATH_UTILS, SOURCЕ_UTILS);
		createResource(PATH_CALCULATOR_RESOURCE, SOURCЕ_CALCULATOR);
		execute(MODULE_CALCULATOR);
		assertEquals("Sum of 3 + 5 = 8", getOutput());
		
		createResource(PATH_CALCULATOR_RESOURCE, SOURCE_CALCULATOR_UPDATED);
		execute(MODULE_CALCULATOR);
		assertEquals("Cache was not updated", "Sum of 5 + 5 = 10", getOutput());
	}
}
