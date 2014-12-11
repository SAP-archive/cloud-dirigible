package com.sap.dirigible.runtime.groovy;

import com.sap.dirigible.repository.api.IRepository;

public class RepositoryURLConnectionParams {
	
	private IRepository repository;
	private String rootPath;
	private String secondaryRootPath;
	
	
	
	public RepositoryURLConnectionParams(IRepository repository,
			String rootPath, String secondaryRootPath) {
		super();
		this.repository = repository;
		this.rootPath = rootPath;
		this.secondaryRootPath = secondaryRootPath;
	}
	public IRepository getRepository() {
		return repository;
	}
	public void setRepository(IRepository repository) {
		this.repository = repository;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getSecondaryRootPath() {
		return secondaryRootPath;
	}
	public void setSecondaryRootPath(String secondaryRootPath) {
		this.secondaryRootPath = secondaryRootPath;
	}
	
	
}
