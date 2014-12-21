package com.sap.dirigible.runtime.flow;

import java.util.Properties;

public class FlowBase {
	
	private String name;
	
	private String description;
	
	private Properties properties;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
		}
		return properties;
	}

}
