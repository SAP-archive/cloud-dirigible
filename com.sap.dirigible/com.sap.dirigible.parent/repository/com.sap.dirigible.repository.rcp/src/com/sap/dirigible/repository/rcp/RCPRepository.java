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

package com.sap.dirigible.repository.rcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.api.IResourceVersion;
import com.sap.dirigible.repository.api.RepositoryPath;
import com.sap.dirigible.repository.logging.Logger;

/**
 * The DB implementation of {@link IRepository}
 * 
 */
public class RCPRepository implements IRepository {

	private static final String PROVIDED_ZIP_DATA_CANNOT_BE_NULL = Messages.getString("DBRepository.PROVIDED_ZIP_DATA_CANNOT_BE_NULL"); //$NON-NLS-1$

	private static final String PROVIDED_ZIP_INPUT_STREAM_CANNOT_BE_NULL = Messages.getString("DBRepository.PROVIDED_ZIP_INPUT_STREAM_CANNOT_BE_NULL"); //$NON-NLS-1$

	private static Logger logger = Logger.getLogger(RCPRepository.class);

	public static final String PATH_DELIMITER = IRepository.SEPARATOR;

	private static final String WORKSPACE_PATH = IRepository.SEPARATOR;
	
	private RCPRepositoryDAO repositoryDAO;

	public RCPRepository() throws RCPBaseException {
		this.repositoryDAO = new RCPRepositoryDAO(this); 
	}
	
	@Override
	public ICollection getRoot() {
		logger.debug("entering getRoot"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(
				WORKSPACE_PATH);
		RCPCollection dbCollection = new RCPCollection(this, wrapperPath);
		logger.debug("exiting getRoot"); //$NON-NLS-1$
		return dbCollection;
	}

	@Override
	public ICollection createCollection(String path) throws IOException {
		logger.debug("entering createCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final RCPCollection collection = new RCPCollection(this, wrapperPath);
		collection.create();
		logger.debug("exiting createCollection"); //$NON-NLS-1$
		return collection;
	}

	@Override
	public ICollection getCollection(String path) {
		logger.debug("entering getCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		RCPCollection dbCollection = new RCPCollection(this, wrapperPath);
		logger.debug("exiting getCollection"); //$NON-NLS-1$
		return dbCollection;
	}

	@Override
	public void removeCollection(String path) throws IOException {
		logger.debug("entering removeCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final ICollection collection = new RCPCollection(this, wrapperPath);
		collection.delete();
		logger.debug("exiting removeCollection"); //$NON-NLS-1$
	}

	@Override
	public boolean hasCollection(String path) throws IOException {
		logger.debug("entering hasCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final ICollection collection = new RCPCollection(this, wrapperPath);
		boolean result = collection.exists();
		logger.debug("exiting hasCollection"); //$NON-NLS-1$
		return result;
	}

	@Override
	public IResource createResource(String path) throws IOException {
		logger.debug("entering createResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new RCPResource(this, wrapperPath);
		resource.create();
		logger.debug("exiting createResource"); //$NON-NLS-1$
		return resource;
	}

	@Override
	public IResource createResource(String path, byte[] content)
			throws IOException {
		logger.debug("entering createResource with Content"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new RCPResource(this, wrapperPath);
		resource.setContent(content);
		logger.debug("exiting createResource with Content"); //$NON-NLS-1$
		return resource;
	}

	@Override
	public IResource createResource(String path, byte[] content,
			boolean isBinary, String contentType) throws IOException {
		logger.debug("entering createResource with Content"); //$NON-NLS-1$
		try {
			getRepositoryDAO().createFile(path, content, isBinary, contentType);
		} catch (RCPBaseException e) {
			throw new IOException(e);
		}
		final IResource resource = getResource(path);
		logger.debug("exiting createResource with Content"); //$NON-NLS-1$
		return resource;
	}

	@Override
	public IResource getResource(String path) {
		logger.debug("entering getResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		RCPResource resource = new RCPResource(this, wrapperPath);
		logger.debug("exiting getResource"); //$NON-NLS-1$
		return resource;
	}

	@Override
	public void removeResource(String path) throws IOException {
		logger.debug("entering removeResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new RCPResource(this, wrapperPath);
		resource.delete();
		logger.debug("exiting removeResource"); //$NON-NLS-1$
	}

	@Override
	public boolean hasResource(String path) throws IOException {
		logger.debug("entering hasResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new RCPResource(this, wrapperPath);
		boolean result = resource.exists();
		logger.debug("exiting hasResource"); //$NON-NLS-1$
		return result;
	}

	@Override
	public void dispose() {
//		repositoryDAO.dispose();
	}

	public RCPRepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}

	@Override
	public void importZip(ZipInputStream zipInputStream, String path)
			throws IOException {
		if (zipInputStream == null) {
			logger.error(PROVIDED_ZIP_INPUT_STREAM_CANNOT_BE_NULL);
			throw new IOException(PROVIDED_ZIP_INPUT_STREAM_CANNOT_BE_NULL);
		}
		ZipImporter.unzip(path,zipInputStream);
	}

	@Override
	public void importZip(byte[] data, String path) throws IOException {
		if (data == null) {
			logger.error(PROVIDED_ZIP_DATA_CANNOT_BE_NULL);
			throw new IOException(PROVIDED_ZIP_DATA_CANNOT_BE_NULL);
		}
		ZipImporter.unzip(path, new ZipInputStream(
				new ByteArrayInputStream(data)));
	}

	@Override
	public byte[] exportZip(List<String> relativeRoots) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
		ZipExporter.zip(relativeRoots, zipOutputStream);
		return baos.toByteArray();
	}

	@Override
	public byte[] exportZip(String path, boolean inclusive)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
		ZipExporter.zip(path, zipOutputStream);
		return baos.toByteArray();
	}

	@Override
	public List<IEntity> searchName(String parameter, boolean caseInsensitive)
			throws IOException {
//		return repositoryDAO.searchName(parameter, caseInsensitive);
		return null;
	}

	@Override
	public List<IEntity> searchName(String root, String parameter, boolean caseInsensitive)
			throws IOException {
//		return repositoryDAO.searchName(root, parameter, caseInsensitive);
//		return null;
		
		String workspacePath = RCPWorkspaceMapper.getMappedName(root);
		
		List<IEntity> entities = new ArrayList<IEntity>();
		

		if (parameter == null
				|| "".equals(parameter)) {
			return entities;
		}
		
		if (parameter.startsWith("%")) {
			parameter = parameter.substring(1);
		}
		
		File dir = new File(workspacePath);
		findInDirectory(dir, parameter, entities);

    	return entities;
	}

	private void findInDirectory(File dir, String parameter,
			List<IEntity> entities) throws IOException {
		
		
		final String search = parameter;
    	File[] found = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(search);
			}
		});
    	
    	for (File f : found) {
    		String repositoryName = RCPWorkspaceMapper.getReverseMappedName(f.getCanonicalPath());
    		RepositoryPath repositoryPath = new RepositoryPath(repositoryName);
    		entities.add(new RCPResource(this, repositoryPath));
    	}
    	
    	File[] all = dir.listFiles();
		for (File f : all) {
			if (f.isDirectory()) {
				findInDirectory(f, parameter, entities);
			}
		}
	}
	
	@Override
	public List<IEntity> searchPath(String parameter, boolean caseInsensitive)
			throws IOException {
//		return repositoryDAO.searchPath(parameter, caseInsensitive);
		return null;
	}

	@Override
	public List<IEntity> searchText(String parameter, boolean caseInsensitive)
			throws IOException {
//		return repositoryDAO.searchText(parameter, caseInsensitive);
		return null;
	}

	@Override
	public List<IResourceVersion> getResourceVersions(String path)
			throws IOException {
//		return repositoryDAO.getResourceVersionsByPath(path);
		return null;
	}

	@Override
	public IResourceVersion getResourceVersion(String path, int version)
			throws IOException {
//		return new DBResourceVersion(this, new RepositoryPath(path), version);
		return null;
	}
	
	@Override
	public void cleanupOldVersions() throws IOException {
//		repositoryDAO.cleanupOldVersions();
	}

	public String getUser() {
		return "local";
	}

}
