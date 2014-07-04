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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.MissingResourceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sap.dirigible.repository.api.ContentTypeHelper;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.runtime.logger.Logger;

/**
 * Servlet implementation class RegistryServlet
 */
public class RegistryServlet extends AbstractRegistryServlet {

	private static final String ACCEPT_HEADER = "Accept"; //$NON-NLS-1$

	private static final String INDEX_HTML = "index.html"; //$NON-NLS-1$

	private static final String LISTING_OF_FOLDERS_IS_FORBIDDEN = Messages
			.getString("RegistryServlet.LISTING_OF_FOLDERS_IS_FORBIDDEN"); //$NON-NLS-1$

	private static final String JSON = "json"; //$NON-NLS-1$

	private static final String JSON_FOLDER = "folder"; //$NON-NLS-1$

	private static final String JSON_ROOT = "root"; //$NON-NLS-1$

	private static final String JSON_PATH = "path"; //$NON-NLS-1$

	private static final String JSON_NAME = "name"; //$NON-NLS-1$

	private static final String JSON_FILES = "files"; //$NON-NLS-1$

	private static final String REQUEST_PROCESSING_FAILED_S = ""; //$NON-NLS-1$

	private static final long serialVersionUID = 7435479651482177443L;

	private static final Logger logger = Logger.getLogger(RegistryServlet.class);

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		String repositoryPath = null;
		final String requestPath = request.getPathInfo();
		boolean deep = false;
		if (requestPath == null) {
			deep = true;
		}
		final OutputStream out = response.getOutputStream();
		try {
			repositoryPath = extractRepositoryPath(request);
			final IEntity entity = getEntity(repositoryPath, request);
			byte[] data;
			if (entity != null) {
				if (entity instanceof IResource) {
					data = buildResourceData(entity, request, response);
				} else if (entity instanceof ICollection) {
					String collectionPath = request.getRequestURI().toString();
					String acceptHeader = request.getHeader(ACCEPT_HEADER);              	
					if (acceptHeader != null && acceptHeader.contains(JSON)) {
						if (!collectionPath.endsWith(IRepository.SEPARATOR)) {
							collectionPath += IRepository.SEPARATOR;
						}
						data = buildCollectionData(deep, entity, collectionPath);
					} else {
						// welcome file support
						IResource index = ((ICollection) entity).getResource(INDEX_HTML);
						if (index.exists() && (collectionPath.endsWith(IRepository.SEPARATOR))) {
							data = buildResourceData(index, request, response);
						} else {
							// listing of collections is forbidden
							exceptionHandler(response, repositoryPath,
									HttpServletResponse.SC_FORBIDDEN,
									LISTING_OF_FOLDERS_IS_FORBIDDEN);
							return;
						}
					}
				} else {
					exceptionHandler(response, repositoryPath, HttpServletResponse.SC_FORBIDDEN,
							LISTING_OF_FOLDERS_IS_FORBIDDEN);
					return;
				}
			} else {
				exceptionHandler(response, repositoryPath, HttpServletResponse.SC_NOT_FOUND,
						String.format("Resource at [%s] does not exist",requestPath));
				return;
			}

			if (entity instanceof IResource) {
				final IResource resource = (IResource) entity;
				String mimeType = null;
				String extension = ContentTypeHelper.getExtension(resource.getName());
				if ((mimeType = ContentTypeHelper.getContentType(extension)) != null) {
					response.setContentType(mimeType);
				} else {
					response.setContentType(resource.getContentType());
				}
			}
			sendData(out, data);
		} catch (final IllegalArgumentException ex) {
			exceptionHandler(response, repositoryPath, HttpServletResponse.SC_BAD_REQUEST,
					ex.getMessage());
		} catch (final MissingResourceException ex) {
			exceptionHandler(response, repositoryPath, HttpServletResponse.SC_NO_CONTENT,
					ex.getMessage());
		} catch (final RuntimeException ex) {
			exceptionHandler(response, repositoryPath,
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		} finally {
			out.flush();
			out.close();
		}
	}

	private void exceptionHandler(final HttpServletResponse response, final String repositoryPath,
			final int errorMessage, final String exceptionMessage) throws IOException {
		logger.error(String.format(REQUEST_PROCESSING_FAILED_S, repositoryPath) + exceptionMessage);
		response.sendError(errorMessage, exceptionMessage);
	}

	protected byte[] buildCollectionData(final boolean deep, final IEntity entity,
			final String collectionPath) throws IOException {
		byte[] data;
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintWriter writer = new PrintWriter(baos);

		final JsonObject rootObject = new JsonObject();
		rootObject.addProperty(JSON_NAME, JSON_ROOT);
		rootObject.addProperty(JSON_PATH, IRepository.SEPARATOR);
		rootObject.add(JSON_FILES,
				enumerateCollectionData(collectionPath, (ICollection) entity, deep));

		writer.println(new Gson().toJsonTree(rootObject));
		writer.flush();
		data = baos.toByteArray();
		return data;
	}

	protected byte[] buildResourceData(final IEntity entity, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		final byte[] data = readResourceData((IResource) entity);
		return data;
	}

	private JsonArray enumerateCollectionData(final String collectionPath,
			final ICollection collection, final boolean deep) throws IOException {
		final JsonArray arr = new JsonArray();
		childItterate(arr, collectionPath, collection, deep, collection.getCollectionsNames(), true);
		childItterate(arr, collectionPath, collection, deep, collection.getResourcesNames(), false);
		return arr;
	}

	private void childItterate(final JsonArray arr, final String collectionPath,
			final ICollection collection, final boolean deep, final List<String> collections,
			final Boolean isFolder) throws IOException {
		for (final String collectionName : collections) {

			final String path = collectionPath + collectionName
					+ (isFolder ? IRepository.SEPARATOR : ""); //$NON-NLS-1$
			final JsonObject elementObject = new JsonObject();
			elementObject.addProperty(JSON_NAME, collectionName);
			elementObject.addProperty(JSON_PATH, path);
			if (isFolder) {
				elementObject.addProperty(JSON_FOLDER, isFolder);
			}
			if (deep && isFolder) {
				final JsonArray children = enumerateCollectionData(path,
						collection.getCollection(collectionName), deep);
				// We don't care about empty folders
				if (children.size() != 0) {
					elementObject.add(JSON_FILES, children);
				}
			}
			arr.add(elementObject);
		}
	}

}
