package com.sap.dirigible.repository.api;

import java.util.Map;

public interface IRepositoryProvider {
	
	public IRepository createRepository(Map<String, Object> parameters);

}
