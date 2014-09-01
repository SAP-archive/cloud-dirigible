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

package com.sap.dirigible.runtime.scripting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.repository.ext.extensions.ExtensionManager;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.mail.MailSender;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public abstract class AbstractScriptExecutor {

	private static final String THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH = Messages
			.getString("ScriptLoader.THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH"); //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(AbstractScriptExecutor.class);

	public Object executeServiceModule(HttpServletRequest request, HttpServletResponse response,
			String module) throws IOException {
		return executeServiceModule(request, response, null, module);
	}

	protected abstract Object executeServiceModule(HttpServletRequest request,
			HttpServletResponse response, Object input, String module) throws IOException;

	protected abstract void registerDefaultVariable(Object scope, String name, Object value);

	protected void registerDefaultVariables(HttpServletRequest request,
			HttpServletResponse response, Object input, Map<Object, Object> executionContext,
			IRepository repository, Object scope) {
		// put the execution context
		registerDefaultVariable(scope, "context", executionContext); //$NON-NLS-1$
		// put the system out
		registerDefaultVariable(scope, "out", System.out); //$NON-NLS-1$
		// put the default data source
		DataSource dataSource = null;
		if (repository instanceof DBRepository) {
			dataSource = ((DBRepository) repository).getDataSource();
		} else {
			dataSource = RepositoryFacade.getInstance().getDataSource();
		}
		registerDefaultVariable(scope, "datasource", dataSource); //$NON-NLS-1$
		// put request
		registerDefaultVariable(scope, "request", request); //$NON-NLS-1$
		// put response
		registerDefaultVariable(scope, "response", response); //$NON-NLS-1$
		// put repository
		registerDefaultVariable(scope, "repository", repository); //$NON-NLS-1$
		// put mail sender
		MailSender mailSender = new MailSender();
		registerDefaultVariable(scope, "mail", mailSender); //$NON-NLS-1$
		// put Apache Commons IOUtils
		IOUtils ioUtils = new IOUtils();
		registerDefaultVariable(scope, "io", ioUtils); //$NON-NLS-1$
		// put Apache Commons HttpClient and related classes wrapped with a
		// factory like HttpUtils
		HttpUtils httpUtils = new HttpUtils();
		registerDefaultVariable(scope, "http", httpUtils); //$NON-NLS-1$
		// put Apache Commons Codecs
		Base64 base64Codec = new Base64();
		registerDefaultVariable(scope, "base64", base64Codec); //$NON-NLS-1$
		Hex hexCodec = new Hex();
		registerDefaultVariable(scope, "hex", hexCodec); //$NON-NLS-1$
		DigestUtils digestUtils = new DigestUtils();
		registerDefaultVariable(scope, "digest", digestUtils); //$NON-NLS-1$
		// standard URLEncoder and URLDecoder functionality
		URLUtils urlUtils = new URLUtils();
		registerDefaultVariable(scope, "url", urlUtils); //$NON-NLS-1$
		// user name
		registerDefaultVariable(scope, "user", RepositoryFacade.getUser(request)); //$NON-NLS-1$
		// file upload
		ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
		registerDefaultVariable(scope, "upload", fileUpload); //$NON-NLS-1$
		// UUID
		UUID uuid = new UUID(0, 0);
		registerDefaultVariable(scope, "uuid", uuid); //$NON-NLS-1$
		// the input from the execution chain if any
		registerDefaultVariable(scope, "input", input); //$NON-NLS-1$
		// DbUtils
		DbUtils dbUtils = new DbUtils(dataSource);
		registerDefaultVariable(scope, "db", dbUtils); //$NON-NLS-1$
		// EscapeUtils
		StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
		registerDefaultVariable(scope, "xss", stringEscapeUtils); //$NON-NLS-1$
		// Extension Manager
		ExtensionManager extensionManager = new ExtensionManager(repository, dataSource);
		registerDefaultVariable(scope, "extensionManager", extensionManager); //$NON-NLS-1$
		// Apache Lucene Indexer
		IndexerUtils indexerUtils = new IndexerUtils();
		registerDefaultVariable(scope, "indexer", indexerUtils); //$NON-NLS-1$
		// Mylyn Confluence Format
		WikiUtils wikiUtils = new WikiUtils();
		registerDefaultVariable(scope, "wiki", wikiUtils); //$NON-NLS-1$
		// Simple binary storage
		StorageUtils storageUtils = new StorageUtils(dataSource);
		registerDefaultVariable(scope, "storage", storageUtils); //$NON-NLS-1$
		// Simple file storage
		FileStorageUtils fileStorageUtils = new FileStorageUtils(dataSource);
		registerDefaultVariable(scope, "fileStorage", fileStorageUtils); //$NON-NLS-1$
		// Simple binary storage
		ConfigStorageUtils configStorageUtils = new ConfigStorageUtils(dataSource);
		registerDefaultVariable(scope, "config", configStorageUtils); //$NON-NLS-1$
		// XML to JSON and vice-versa
		XMLUtils xmlUtils = new XMLUtils();
		registerDefaultVariable(scope, "xml", xmlUtils); //$NON-NLS-1$

	}

	public byte[] readResourceData(IRepository repository, String repositoryPath)
			throws IOException {
		final IResource resource = repository.getResource(repositoryPath);
		if (!resource.exists()) {
			logger.error(String.format(THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH, resource.getName(), repositoryPath));
			throw new IOException(String.format(THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH,
					resource.getName(), repositoryPath));
		}
		return resource.getContent();
	}
	
	public byte[] retrieveModule(IRepository repository, String module, String extension, String ... rootPaths)
			throws IOException {
		
		for (String rootPath : rootPaths) {
			String resourcePath = createResourcePath(rootPath, module, extension);
			byte[] result;
			final IResource resource = repository.getResource(resourcePath);
			if (resource.exists()) {
				result = readResourceData(repository, resourcePath);
				return result;
			}
		}
		
		logger.error(String.format(THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH, (module + extension), Arrays.toString(rootPaths)));
			throw new FileNotFoundException(
					THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH
					+ module + extension);
	}
	
	private String createResourcePath(String root, String module,
			String extension) {
		String resourcePath = new StringBuilder().append(root).append('/')
				.append(module).append(extension).toString();
		return resourcePath;
	}
	
}
