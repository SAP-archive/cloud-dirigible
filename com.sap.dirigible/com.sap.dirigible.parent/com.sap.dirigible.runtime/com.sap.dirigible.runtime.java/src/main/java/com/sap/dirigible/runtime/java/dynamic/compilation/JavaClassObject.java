package com.sap.dirigible.runtime.java.dynamic.compilation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class JavaClassObject extends SimpleJavaFileObject implements CommonConstants {

	private final String name;
	private final String content;
	private final long lastModified;
	private ByteArrayOutputStream bos;

	public JavaClassObject(String name, Kind kind, String content, long lastModified) {
		super(URI.create(STRING_URI + name.replace(DOT, SLASH) + kind.extension), kind);
		this.name = name;
		this.content = content.trim();
		this.lastModified = lastModified;
		this.bos = new ByteArrayOutputStream();
	}

	public byte[] getBytes() {
		byte[] bytes = bos.toByteArray();
		return bytes;
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
		bos.reset();
		return bos;
	}
}
