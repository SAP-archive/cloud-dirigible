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

package com.sap.dirigible.ide.jgit.property.tester;

import java.io.IOException;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;

import com.sap.dirigible.ide.jgit.utils.GitProjectProperties;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.impl.Workspace;
import com.sap.dirigible.repository.api.IRepository;

public class GitProjectPropertyTest extends PropertyTester {
	private static final Logger logger = Logger.getLogger(GitProjectProperties.class);

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		boolean result = false;
		boolean inverseForm = false;
		if (expectedValue instanceof Boolean) {
			inverseForm = !(Boolean) expectedValue;
		}
		if (receiver instanceof IProject) {
			IProject project = (IProject) receiver;
			Workspace workspace = (Workspace) project.getWorkspace();
			IRepository repository = workspace.getRepository();

			String user = RemoteResourcesPlugin.getUserName();
			String projectName = project.getName();
			String gitFilePath = String.format(GitProjectProperties.GIT_PROPERTY_FILE_LOCATION,
					user, projectName);
			try {
				result = repository.hasResource(gitFilePath);
			} catch (IOException e) {
				result = false;
				logger.error(e.getMessage(), e);
			}
		}
		if (inverseForm) {
			result = !result;
		}
		return result;
	}

}
