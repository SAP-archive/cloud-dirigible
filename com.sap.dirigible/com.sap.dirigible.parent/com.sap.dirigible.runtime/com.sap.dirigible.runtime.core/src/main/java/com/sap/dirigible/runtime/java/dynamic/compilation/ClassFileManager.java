package com.sap.dirigible.runtime.java.dynamic.compilation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

import com.sap.dirigible.repository.api.IEntityInformation;
import com.sap.dirigible.runtime.java.JavaExecutor;
import com.sap.dirigible.runtime.scripting.Module;

public class ClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	private static final String DOT = ".";
	private static final String SLASH = "/";
	private static final String PATH_SEPARATOR = "path.separator";
	
	private static ClassFileManager instance;
	private static List<JavaFileObject> lastKnownSourceFiles = Collections.synchronizedList(new ArrayList<JavaFileObject>());
	
	public synchronized static ClassFileManager getInstance(StandardJavaFileManager standardManager) {
		if (instance == null) {
			instance = new ClassFileManager(standardManager);
		}
		return instance;
	}
	
	public static String getFQN(String module) {
		StringBuilder fqn = new StringBuilder(module);
		if (fqn.charAt(0) == SLASH.charAt(0)) {
			fqn.delete(0, 1);
		}
		int indexOf = fqn.indexOf(DOT);
		if (indexOf > 0) {
			fqn.delete(indexOf, fqn.length());
		}

		return fqn.toString().replace(SLASH, DOT);
	}
	
	public static String getJars() throws URISyntaxException, IOException {
		URL url = JavaExecutor.class.getProtectionDomain().getCodeSource().getLocation();
		File libDirectory = new File(url.toURI()).getParentFile();
		return getJars(libDirectory);
	}

	public static String getJars(File libDirectory) throws IOException {
		String separator = System.getProperty(PATH_SEPARATOR);
		StringBuilder jars = new StringBuilder();

		for (File jar : libDirectory.listFiles()) {
			jars.append(jar.getCanonicalPath() + separator);
		}
		return jars.toString();
	}
	
	public static JavaFileObject getSourceFile(String className) {
		JavaFileObject javaFileObject = null;
		if (lastKnownSourceFiles != null) {
			for (JavaFileObject sourceFile : lastKnownSourceFiles) {
				if (sourceFile.getName().equals(className)) {
					javaFileObject = sourceFile;
					break;
				}
			}
		}
		return javaFileObject;
	}
	
	public static List<JavaFileObject> getSourceFiles(List<Module> modules) throws IOException {
		List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		for (Module module : modules) {
			String fqn = getFQN(module.getPath());
			String content = new String(module.getContent());
			long lastModified = getLastModified(module);

			javaFiles.add(new JavaClassObject(fqn, Kind.SOURCE, content, lastModified));
		}
		lastKnownSourceFiles.clear();
		lastKnownSourceFiles.addAll(javaFiles);
		return javaFiles;
	}
	
	private static long getLastModified(Module module) {
		Date lastModified = null;
		IEntityInformation entityInformation = module.getEntityInformation();
		if (entityInformation != null) {
			lastModified = entityInformation.getModifiedAt();
		} else {
			lastModified = new Date();
		}
		return lastModified.getTime();
	}

	public static void updateCache(List<JavaFileObject> sourceFiles) throws ClassNotFoundException {
		for(JavaFileObject sourceFile : sourceFiles){
			String name = sourceFile.getName();
			JavaFileObject cached = DynamicJavaCacheManager.getJavaFileObjectCacheEntry(name);
			if (cached != null) {
				if (cached.getLastModified() < sourceFile.getLastModified()) {
					updateCache(sourceFile, name);
				}
			} else {
				updateCache(sourceFile, name);
			}
		}
	}
	
	public static void clearCache() {
		DynamicJavaCacheManager.clearCache();
	}
	
	private static void updateCache(JavaFileObject sourceFile, String name) {
		DynamicJavaCacheManager.putJavaFileObjectCacheEntry(name, sourceFile);
		DynamicJavaCacheManager.putClassCacheEntry(name, null);
	}

	private ClassFileManager(StandardJavaFileManager standardManager) {
		super(standardManager);
	}

	@Override
	public ClassLoader getClassLoader(final Location location) {
		return new DynamicJavaClassLoader();
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
		return DynamicJavaCacheManager.getJavaFileObjectCacheEntry(className);
	}
}
