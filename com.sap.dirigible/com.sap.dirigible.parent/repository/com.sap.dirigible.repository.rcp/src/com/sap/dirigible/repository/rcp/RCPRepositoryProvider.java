package com.sap.dirigible.repository.rcp;

import java.util.Map;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryProvider;

public class RCPRepositoryProvider implements IRepositoryProvider {

	@Override
	public IRepository createRepository(Map<String, Object> parameters) {
		return RCPRepository.getInstance();
	}

}
