package com.sap.dirigible.runtime.java.dynamic.compilation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaFileObject;

public class DynamicJavaCacheManager {

	private static final Map<String, JavaFileObject> cachedJavaFileObjects = Collections.synchronizedMap(new HashMap<String, JavaFileObject>());
	private static final Map<String, Class<?>> cachedClasses = Collections.synchronizedMap(new HashMap<String, Class<?>>());
	
	public static void clearCache() {
		cachedJavaFileObjects.clear();
		cachedClasses.clear();
	}
	
	public static JavaFileObject getJavaFileObjectCacheEntry(String name) {
		return cachedJavaFileObjects.get(name);
	}
	
	public static JavaFileObject putJavaFileObjectCacheEntry(String name, JavaFileObject javaFileObject) {
		return cachedJavaFileObjects.put(name, javaFileObject);
	}
	
	public static Class<?> getClassCacheEntry(String name) {
		return cachedClasses.get(name);
	}
	
	public static Class<?> putClassCacheEntry(String name, Class<?> clazz) {
		return cachedClasses.put(name, clazz);
	}
	
	private DynamicJavaCacheManager() {
	}
}
