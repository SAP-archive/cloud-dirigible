package com.sap.dirigible.runtime.java.dynamic.compilation;

import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

public class ClassFileManager extends ForwardingJavaFileManager {

	private final Map<String, JavaClassObject> javaClassObjects;

	@SuppressWarnings("unchecked")
	public ClassFileManager(StandardJavaFileManager standardManager) {
		super(standardManager);
		javaClassObjects = new HashMap<String, JavaClassObject>();
	}

	@Override
	public ClassLoader getClassLoader(final Location location) {
		return new SecureClassLoader(ClassLoader.getSystemClassLoader()) {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				System.out.println("SecureClassLoader#loadClass(String name) name-> " + name);
				Class<?> result = null;
				JavaClassObject javaClassObject = (JavaClassObject) javaClassObjects.get(name);
				if (javaClassObject != null) {
					byte[] bytes = javaClassObject.getBytes();
					result = super.defineClass(name, bytes, 0, bytes.length);
				} else {
					result = Servlet.class.getClassLoader().loadClass(name);
				}

				return result;
			}
		};
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind,
			FileObject sibling) throws IOException {
		JavaClassObject javaClassObject = new JavaClassObject(className, kind);
		javaClassObjects.put(className, javaClassObject);
		return javaClassObject;
	}
}
