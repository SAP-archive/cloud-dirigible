package com.sap.dirigible.repository.ext.conf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.api.IResource;

public class ConfigurationStorage implements IConfigurationStorage {
	
	private static final String PROPERTIES_EXT = ".properties"; 
	
	private IRepository repository;
	
	public ConfigurationStorage(IRepository repository) {
		this.repository = repository;
	}

	@Override
	public Properties getGeneralSettings(String path, String name) throws IOException {
		return getSettingsByRoot(IRepositoryPaths.CONF_REGISTRY, path, name);
	}

	@Override
	public void setGeneralSettings(String path, String name,
			Properties properties) throws IOException {
		setSettingsByRoot(IRepositoryPaths.CONF_REGISTRY, path, name, properties);
	}

	@Override
	public Properties getUserSettings(String path, String name, String userName) throws IOException {
		String root = getUserPath(userName);
		return getSettingsByRoot(root, path, name);
	}
	
	@Override
	public void setUserSettings(String path, String name, Properties properties, String userName) throws IOException {
		String root = getUserPath(userName);
		setSettingsByRoot(root, path, name, properties);		
	}

	private String getUserPath(String userName) {
		return IRepositoryPaths.DB_DIRIGIBLE_USERS + userName + IRepositoryPaths.CONF_FOLDER_NAME + IRepository.SEPARATOR;
	}

	private Properties getSettingsByRoot(String root, String path, String name) throws IOException {
		String resourcePath = root + path + IRepository.SEPARATOR + name + PROPERTIES_EXT;
		if (repository != null 
				&& repository.hasResource(resourcePath)) {
			IResource resource = repository.getResource(resourcePath);
			Properties properties = new Properties(); 
			properties.load(new ByteArrayInputStream(resource.getContent()));
			return properties;
		}
		return null;
	}
	
	private void setSettingsByRoot(String root, String path, String name, Properties properties) throws IOException {
		String resourcePath = root + path + IRepository.SEPARATOR + name + PROPERTIES_EXT;
		IResource resource = null;
		if (repository != null 
				&& repository.hasResource(resourcePath)) {
			resource = repository.getResource(resourcePath);
		} else {
			resource = repository.createResource(resourcePath);
		}
		 
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		properties.store(baos, resource.getPath());
		
		resource.setContent(baos.toByteArray());
	}
}
