package com.sap.dirigible.repository.ext.conf;

import java.io.IOException;
import java.util.Properties;

public interface IConfigurationStore {
	
	public Properties getGlobalSettings(String path, String name) throws IOException;
	
	public byte[] getGlobalSettingsAsBytes(String path, String name) throws IOException;
	
	public void setGlobalSettings(String path, String name, Properties properties) throws IOException;
	
	public void setGlobalSettingsAsBytes(String path, String name, byte[] bytes) throws IOException;
	
	public Properties getUserSettings(String path, String name, String userName) throws IOException;
	
	public byte[] getUserSettingsAsBytes(String path, String name, String userName) throws IOException;
	
	public void setUserSettings(String path, String name, Properties properties, String userName) throws IOException;
	
	public void setUserSettingsAsBytes(String path, String name, byte[] bytes, String userName) throws IOException;

}
