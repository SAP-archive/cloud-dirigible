package com.sap.dirigible.runtime.java.dynamic.compilation;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class CharSequenceJavaFileObject extends SimpleJavaFileObject implements CommonConstants {

	private CharSequence content;

	public CharSequenceJavaFileObject(String className, CharSequence content) {
		super(URI.create(STRING_URI + className.replace(DOT, SLASH) + Kind.SOURCE.extension),
				Kind.SOURCE);
		this.content = content;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return content;
	}
}
