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

package com.sap.dirigible.runtime.registry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.MissingResourceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.runtime.logger.Logger;

public abstract class AbstractRegistryServiceServlet extends AbstractRegistryServlet {

	private static final long serialVersionUID = -8255379751142002763L;

	private static final Logger logger = Logger.getLogger(AbstractRegistryServiceServlet.class);

	private static final String SERVICES_FOLDER = ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES + IRepository.SEPARATOR;

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		final String repositoryPath = IRepositoryPaths.DB_DIRIGIBLE_REGISTRY_PUBLIC + getServicesFolder();
		try {
			final ICollection collection = getRepository(request).getCollection(repositoryPath);
			buildList(
					new DefinitionEnumerator(repositoryPath, collection, getFileExtension())
							.toArrayList(),
					request, response);
		} catch (final IllegalArgumentException ex) {
			logger.error(String.format(getRequestProcessingFailedMessage(), repositoryPath)
					+ ex.getMessage());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		} catch (final MissingResourceException ex) {
			logger.error(String.format(getRequestProcessingFailedMessage(), repositoryPath)
					+ ex.getMessage());
			response.sendError(HttpServletResponse.SC_NO_CONTENT, ex.getMessage());
		}
	}

	protected String getServicesFolder() {
		return SERVICES_FOLDER;
	}

	private void buildList(final List<String> jsDefinitions, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		response.setContentType("application/json");
		final PrintWriter writer = response.getWriter();
		final String headingUrl = PathUtils.getHeadingUrl(request, getServletMapping());

		final JsonArray jsonRootArray = new JsonArray();

		for (final String jsDefinition : jsDefinitions) {

			final String path = headingUrl + jsDefinition;
			final JsonObject elementObject = new JsonObject();
			elementObject.addProperty("name", jsDefinition);
			elementObject.addProperty("path", path);

			jsonRootArray.add(elementObject);
		}
		writer.println(new Gson().toJsonTree(jsonRootArray));
		writer.flush();
		writer.close();
	}

	protected abstract String getServletMapping();

	protected abstract String getFileExtension();

	protected abstract String getRequestProcessingFailedMessage();
}
