package com.sap.dirigible.runtime.scripting;

public class Module {
	private String name;
	private byte[] content;

	public Module(String name, byte[] content) {
		this.name = name;
		this.content = content;
	}

	public String getPath() {
		return name;
	}

	public byte[] getContent() {
		return content;
	}
	
	// TODO DELETE ME !
	@Override
	public String toString() {
		return name + " | " + new String(content);
	}
}
