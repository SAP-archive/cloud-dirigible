package com.sap.dirigible.runtime.java.dynamic.compilation;

import java.security.SecureClassLoader;

import javax.servlet.Servlet;
import javax.tools.JavaFileObject;

public class DynamicJavaClassLoader extends SecureClassLoader {

	public DynamicJavaClassLoader() {
		super(ClassLoader.getSystemClassLoader());
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz = DynamicJavaCacheManager.getClassCacheEntry(name);
		if (clazz == null) {
			clazz = defineClass(name, DynamicJavaCacheManager.getJavaFileObjectCacheEntry(name));
			DynamicJavaCacheManager.putClassCacheEntry(name, clazz);
		}
		return clazz;
	}

	private Class<?> defineClass(String name, JavaFileObject sourceFile)
			throws ClassNotFoundException {
		Class<?> clazz = null;
		if (sourceFile != null && sourceFile instanceof JavaClassObject) {
			byte[] bytes = ((JavaClassObject) sourceFile).getBytes();
			clazz = super.defineClass(name, bytes, 0, bytes.length);
		} else {
			clazz = Servlet.class.getClassLoader().loadClass(name);
		}
		resolveClass(clazz);
		return clazz;
	}

}