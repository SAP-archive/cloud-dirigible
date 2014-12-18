package com.sap.dirigible.ide.generic.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.conf.ConfigurationStore;

public class GenericListManager {
	
	private static final String GENERIC_VIEWS = "genericViews";

	private static GenericListManager instance;

	private IRepository repository;
	
	private ConfigurationStore configurationStore;

	public static GenericListManager getInstance(IRepository repository) {
		if (instance == null) {
			instance = new GenericListManager(repository);
		}
		return instance;
	}

	public GenericListManager(IRepository repository) {
		this.repository = repository;
		configurationStore = new ConfigurationStore(repository);
	}

	public IRepository getRepository() {
		return this.repository;
	}

	public ConfigurationStore getConfigurationStore() {
		return configurationStore;
	}

	public void addLocation(String name, String location) throws IOException {
		if (name == null
				|| "".equals(name.trim())
				|| location == null
				|| "".equals(location.trim())) {
			throw new IllegalArgumentException("Name and Location - both have to be provided");
		}
		List<GenericLocationMetadata> locationMetadatas = getLocationsList();
		for (Iterator<GenericLocationMetadata> iterator = locationMetadatas.iterator(); iterator.hasNext();) {
			GenericLocationMetadata genericLocationMetadata = iterator.next();
			if (name.equals(genericLocationMetadata.getName())) {
				throw new IllegalArgumentException(String.format("Generic View with this name is already present - %s", name));
			}
		}
		Properties properties = new Properties();
		properties.setProperty("generic.view.name", name);
		properties.setProperty("generic.view.location", location);
		configurationStore.setGlobalSettings(CommonParameters.CONF_PATH_GENERIC_VIEWS, name, properties);
	}

	public void removeLocation(String name) throws IOException {
		if (name == null
				|| "".equals(name.trim())) {
			throw new IllegalArgumentException("Name has to be provided");
		}
		
		configurationStore.removeGlobalSettings(CommonParameters.CONF_PATH_GENERIC_VIEWS, name);
	}
	
	public boolean existsLocation(String name) throws IOException {
		if (name == null
				|| "".equals(name.trim())) {
			throw new IllegalArgumentException("Name has to be provided");
		}
		return configurationStore.existsGlobalSettings(CommonParameters.CONF_PATH_GENERIC_VIEWS, name);
	}

	public List<GenericLocationMetadata> getLocationsList() throws IOException {
		List<GenericLocationMetadata> result = new ArrayList<GenericLocationMetadata>();
		List<String> names = configurationStore.listGlobalSettingsNames(CommonParameters.CONF_PATH_GENERIC_VIEWS);
		for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
			String name = iterator.next();
			name = name.replaceAll(".properties", "");
			Properties properties = configurationStore.getGlobalSettings(CommonParameters.CONF_PATH_GENERIC_VIEWS, name);
			String genericViewName = properties.getProperty("generic.view.name");
			String genericViewLocation = properties.getProperty("generic.view.location");
			GenericLocationMetadata genericLocationMetadata = 
					new GenericLocationMetadata(genericViewName, genericViewLocation);
			result.add(genericLocationMetadata);
		}
		return result;
	}

}
