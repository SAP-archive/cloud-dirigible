package com.sap.dirigible.runtime.java.dynamic.compilation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class JavaClassObject extends SimpleJavaFileObject implements CommonConstants {

	protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public JavaClassObject(String name, Kind kind) {
		super(URI.create(STRING_URI + name.replace(DOT, SLASH) + kind.extension), kind);
	}

	public byte[] getBytes() {
		return bos.toByteArray();
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return bos;
	}
}
