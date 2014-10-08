package com.sap.dirigible.runtime.java.dynamic.compilation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class JavaClassObject extends SimpleJavaFileObject implements CommonConstants {

	private String name;
	private String content;
	private long lastModified;

	 private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public JavaClassObject(String name, Kind kind, String content, long lastModified) {
		super(URI.create(STRING_URI + name.replace(DOT, SLASH) + kind.extension), kind);
		this.name = name;
		this.content = content.trim();
		this.lastModified = lastModified;
	}

	public byte[] getBytes() {
		return bos.toByteArray();
	}

	public String getContent() {
		return content;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getLastModified() {
		return lastModified;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return content != null ? content : super.getCharContent(ignoreEncodingErrors);
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return bos;
	}
}
