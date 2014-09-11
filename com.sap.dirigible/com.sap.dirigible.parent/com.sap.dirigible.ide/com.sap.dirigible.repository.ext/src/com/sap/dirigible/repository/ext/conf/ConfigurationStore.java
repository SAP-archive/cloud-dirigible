package com.sap.dirigible.repository.ext.conf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.api.IResource;

public class ConfigurationStore implements IConfigurationStore {
	
	private static final String PROPERTIES_EXT = ".properties"; 
	
	private IRepository repository;
	
	public ConfigurationStore(IRepository repository) {
		this.repository = repository;
	}

	@Override
	public Properties getGlobalSettings(String path, String name) throws IOException {
		return getSettingsByRoot(IRepositoryPaths.CONF_REGISTRY, path, name);
	}
	
	@Override
	public byte[] getGlobalSettingsAsBytes(String path, String name) throws IOException {
		return getSettingsByRootAsBytes(IRepositoryPaths.CONF_REGISTRY, path, name);
	}

	@Override
	public void setGlobalSettings(String path, String name,
			Properties properties) throws IOException {
		setSettingsByRoot(IRepositoryPaths.CONF_REGISTRY, path, name, properties);
	}
	
	@Override
	public void setGlobalSettingsAsBytes(String path, String name,
			byte[] bytes) throws IOException {
		setSettingsByRootAsBytes(IRepositoryPaths.CONF_REGISTRY, path, name, bytes);
	}

	@Override
	public Properties getUserSettings(String path, String name, String userName) throws IOException {
		String root = getUserPath(userName);
		return getSettingsByRoot(root, path, name);
	}
	
	@Override
	public byte[] getUserSettingsAsBytes(String path, String name, String userName) throws IOException {
		String root = getUserPath(userName);
		return getSettingsByRootAsBytes(root, path, name);
	}
	
	@Override
	public void setUserSettings(String path, String name, Properties properties, String userName) throws IOException {
		String root = getUserPath(userName);
		setSettingsByRoot(root, path, name, properties);		
	}
	
	@Override
	public void setUserSettingsAsBytes(String path, String name, byte[] bytes, String userName) throws IOException {
		String root = getUserPath(userName);
		setSettingsByRootAsBytes(root, path, name, bytes);		
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
		return new Properties();
	}
	
	private byte[] getSettingsByRootAsBytes(String root, String path, String name) throws IOException {
		String resourcePath = root + path + IRepository.SEPARATOR + name + PROPERTIES_EXT;
		if (repository != null 
				&& repository.hasResource(resourcePath)) {
			IResource resource = repository.getResource(resourcePath);
			return resource.getContent();
		}
		return new byte[]{};
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
	
	private void setSettingsByRootAsBytes(String root, String path, String name, byte[] bytes) throws IOException {
		String resourcePath = root + path + IRepository.SEPARATOR + name + PROPERTIES_EXT;
		IResource resource = null;
		if (repository != null 
				&& repository.hasResource(resourcePath)) {
			resource = repository.getResource(resourcePath);
		} else {
			resource = repository.createResource(resourcePath);
		}

		resource.setContent(bytes);
	}
}
