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

package com.sap.dirigible.ide.workspace.wizard.project.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.impl.Workspace;
import com.sap.dirigible.repository.api.IRepository;

public class RepositoryDataStore {

	public static final String PROJECT_NAME_SEPARATOR = "$"; //$NON-NLS-1$

	public static byte[] getByteArrayData(String fileName) throws IOException {
		IRepository repository = RepositoryFacade.getInstance().getRepository();
		Workspace workspace = (Workspace) RemoteResourcesPlugin.getWorkspace();
		String root = workspace
				.getRepositoryPathForWorkspace(RemoteResourcesPlugin
						.getUserName());
		fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		List<String> projectNames = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(fileName,
				PROJECT_NAME_SEPARATOR);
		while (tokenizer.hasMoreElements()) {
			String token = (String) tokenizer.nextElement();
			projectNames.add(root + "/" + token); //$NON-NLS-1$
		}

		byte[] result = repository.exportZip(projectNames);
		return result;
	}

}
