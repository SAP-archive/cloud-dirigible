package com.sap.dirigible.ide.workspace.wizard.project.sample.property.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.sap.dirigible.ide.common.CommonParameters;

public class SamplesProjectPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		boolean testResult = false;
		try {
			testResult = CommonParameters.isUserInRole(CommonParameters.ROLE_DEVELOPER);
		} catch (UnsupportedOperationException e) {
			testResult = false;
		}
		return testResult;
	}

}
