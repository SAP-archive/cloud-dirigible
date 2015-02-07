package com.sap.dirigible.runtime.scripting;

import com.sap.dirigible.repository.api.IEntityInformation;

public class Module {
	private String name;
	private String path;
	private byte[] content;
	private IEntityInformation entityInformation;

	public Module(String name, String path,  byte[] content) {
		this(name, path, content, null);
	}

	public Module(String name, String path, byte[] content, IEntityInformation entityInformation) {
		this.name = name;
		this.path = path;
		this.content = content;
		this.entityInformation = entityInformation;
	}

	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}

	public byte[] getContent() {
		return content;
	}

	public IEntityInformation getEntityInformation() {
		return entityInformation;
	}
}
