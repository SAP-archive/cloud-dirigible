package com.sap.dirigible.runtime.java.dynamic.compilation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.SecureClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
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
	private static final Map<String, JavaFileObject> lastKnownSourceFiles = Collections.synchronizedMap(new HashMap<String, JavaFileObject>());
	
	public synchronized static ClassFileManager getInstance(StandardJavaFileManager standardManager) {
		if (instance == null) {
			instance = new ClassFileManager(standardManager);
		}
		return new ClassFileManager(standardManager);
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
		return lastKnownSourceFiles.get(className);
	}
	
	public static List<JavaFileObject> getSourceFiles(List<Module> modules) throws IOException {
		for (Module module : modules) {
			String fqn = getFQN(module.getPath());
			long lastModified = getLastModified(module);

			JavaClassObject lastKnownSoruceFile = (JavaClassObject) getSourceFile(fqn);
			if(lastKnownSoruceFile == null || lastKnownSoruceFile.getLastModified() < lastModified) {
				String content = new String(module.getContent());
				lastKnownSourceFiles.put(fqn, new JavaClassObject(fqn, Kind.SOURCE, content, lastModified));
			}
		}
		JavaFileObject[] array = lastKnownSourceFiles.values().toArray(new JavaFileObject[]{});
		return Arrays.asList(array);
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

	private ClassFileManager(StandardJavaFileManager standardManager) {
		super(standardManager);
	}

	@Override
	public ClassLoader getClassLoader(final Location location) {
		return new SecureClassLoader(ClassLoader.getSystemClassLoader()) {
			
			@Override
			protected Class<?> findClass(String name) throws ClassNotFoundException {
				Class<?> clazz = null;
				JavaClassObject javaClassObject = (JavaClassObject) lastKnownSourceFiles.get(name);
				if (javaClassObject != null) {
					byte[] bytes = javaClassObject.getBytes();
					clazz = super.defineClass(name, bytes, 0, bytes.length);
				} else {
					clazz = Servlet.class.getClassLoader().loadClass(name);
				}
				return clazz;
			}
		};
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
		return lastKnownSourceFiles.get(className);
	}
}
