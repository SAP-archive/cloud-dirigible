package com.sap.dirigible.repository.ext.conf;

import java.io.IOException;
import java.util.Properties;

public interface IConfigurationStorage {
	
	public Properties getGeneralSettings(String path, String name) throws IOException;
	
	public void setGeneralSettings(String path, String name, Properties properties) throws IOException;
	
	public Properties getUserSettings(String path, String name, String userName) throws IOException;
	
	public void setUserSettings(String path, String name, Properties properties, String userName) throws IOException;

}
