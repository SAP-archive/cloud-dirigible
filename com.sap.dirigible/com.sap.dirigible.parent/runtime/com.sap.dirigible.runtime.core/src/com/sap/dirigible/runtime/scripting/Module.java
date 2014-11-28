package com.sap.dirigible.runtime.scripting;

import com.sap.dirigible.repository.api.IEntityInformation;

public class Module {
	private String name;
	private byte[] content;
	private IEntityInformation entityInformation;

	public Module(String name, byte[] content) {
		this(name, content, null);
	}

	public Module(String name, byte[] content, IEntityInformation entityInformation) {
		this.name = name;
		this.content = content;
		this.entityInformation = entityInformation;
	}

	public String getPath() {
		return name;
	}

	public byte[] getContent() {
		return content;
	}

	public IEntityInformation getEntityInformation() {
		return entityInformation;
	}
}
