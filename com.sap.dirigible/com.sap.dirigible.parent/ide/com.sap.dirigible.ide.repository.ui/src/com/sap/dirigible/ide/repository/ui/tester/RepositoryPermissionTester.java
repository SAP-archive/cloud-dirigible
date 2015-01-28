/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.ide.repository.ui.tester;

import java.io.IOException;

import org.eclipse.core.expressions.PropertyTester;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepositoryPaths;

public class RepositoryPermissionTester extends PropertyTester {
	private static final Logger logger = Logger.getLogger(RepositoryPermissionTester.class);

	private static final String REPOSITORY_COMMAND_COPY = "copy";

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		String operation = (String) expectedValue;
		String currentUser = CommonParameters.getUserName();
		String createdBy = null;
		boolean allowed = false;

		String allowedCopyPath = IRepositoryPaths.DB_DIRIGIBLE_ROOT;
		String allowedWorkspacePath = CommonParameters.getWorkspace();
		String allowedSandboxPath = IRepositoryPaths.DB_DIRIGIBLE_SANDBOX + currentUser;
		
		boolean isOperator = CommonParameters.isUserInRole(CommonParameters.ROLE_OPERATOR);
		
		IEntity selectedEntity = (IEntity) receiver;
		String selectedEntityPath = selectedEntity.getPath();

		try {
			if (isOperator) {
				allowed = true;
			} else if (operation.equalsIgnoreCase(REPOSITORY_COMMAND_COPY)) {
				allowed = selectedEntityPath.startsWith(allowedCopyPath);
			} else {
				createdBy = selectedEntity.getInformation().getCreatedBy();
				allowed = (selectedEntityPath.startsWith(allowedWorkspacePath) || selectedEntityPath
						.startsWith(allowedSandboxPath)) && currentUser.equalsIgnoreCase(createdBy);
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			allowed = false;
		}
		return allowed;
	}
}
