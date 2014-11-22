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

package com.sap.dirigible.runtime.agent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.runtime.logger.Logger;

public class AbstractApplyHandler {

	private static final String CONFIGURATION_LOCATION_DOESN_T_EXIST = Messages.getString("AbstractApplyHandler.CONFIGURATION_LOCATION_DOESN_T_EXIST"); //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(AbstractApplyHandler.class.getCanonicalName());

	private ConfigurationAgent configurationAgent;
	private String remote;

	public AbstractApplyHandler(ConfigurationAgent configurationAgent,
			String remote) {
		super();
		this.configurationAgent = configurationAgent;
		this.remote = remote;
	}

	public ConfigurationAgent getConfigurationAgent() {
		return configurationAgent;
	}

	public String getRemote() {
		return remote;
	}

	protected InputStream getRemoteFile(String remoteFile,
			HttpServletRequest request) throws IOException {
		// load from repository
		IResource resource = getConfigurationAgent()
				.getRepository(request)
				.getResource(
						ConfigurationCommands.REPOSITORY_INTEGRATION_DEPLOY_PATH
								+ remoteFile);
		if (resource.exists()) {
			InputStream routeIn = new ByteArrayInputStream(
					resource.getContent());
			return routeIn;
		} else {
			String error = CONFIGURATION_LOCATION_DOESN_T_EXIST
					+ resource.getPath();
			logger.error(error);
			throw new IOException(error);
		}
	}

	protected void writeToFile(Reader input, Writer output) throws IOException {

		BufferedReader reader = new BufferedReader(input);
		BufferedWriter writer = new BufferedWriter(output);
		try {
			String s = null;
			while ((s = reader.readLine()) != null) {
				writer.write(s + '\n');
			}
		} finally {
			writer.flush();
			writer.close();
		}
	}

}