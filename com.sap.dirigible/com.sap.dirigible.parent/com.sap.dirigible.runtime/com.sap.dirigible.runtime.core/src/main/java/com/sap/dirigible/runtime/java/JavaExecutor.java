package com.sap.dirigible.runtime.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.java.dynamic.compilation.CharSequenceJavaFileObject;
import com.sap.dirigible.runtime.java.dynamic.compilation.ClassFileManager;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;

public class JavaExecutor extends AbstractScriptExecutor {

	private static final Logger logger = Logger.getLogger(JavaExecutor.class);

	private IRepository repository;
	private String[] rootPaths;
	private Map<String, Object> defaultVariables;

	public JavaExecutor(IRepository repository, String... rootPaths) {
		this.repository = repository;
		this.rootPaths = rootPaths;
		this.defaultVariables = new HashMap<String, Object>();
	}

	@Override
	protected Object executeServiceModule(HttpServletRequest request, HttpServletResponse response,
			Object input, String module) throws IOException {

		// TODO Delete me
		logger.error("JavaExecutor#executeServiceModule()");
		logger.error("JavaExecutor#executeServiceModule() -> module=" + module);

		registerDefaultVariables(request, response, input, null, repository, null);

		try {

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			// TODO implement Diagnostic Listener!
			StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null,
					null, null);
			JavaFileManager fileManager = new ClassFileManager(standardFileManager);

			URL url = JavaExecutor.class.getProtectionDomain().getCodeSource().getLocation();
			File libDirectory = new File(url.toURI()).getParentFile();

			String separator = System.getProperty("path.separator");
			StringBuilder jars = new StringBuilder();

			for (File jar : libDirectory.listFiles()) {
				jars.append(jar.getCanonicalPath() + separator);
			}

			CompilationTask task = compiler.getTask(null, fileManager, null,
					Arrays.asList("-classpath", jars.toString()), null, getSourceFiles(module));
			task.call();

			ClassLoader classLoader = fileManager.getClassLoader(null);

			// TODO Determine class name (FQN)
			Class<?> loadedClass = classLoader
					.loadClass("com.sap.dirigible.dynamic.test.Calculator");

			Object instance = loadedClass.newInstance();
			Method serviceMethod = loadedClass.getMethod("service",
					new Class<?>[] { HttpServletResponse.class });
			return serviceMethod.invoke(instance, response);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private Iterable<? extends JavaFileObject> getSourceFiles(String module) throws IOException {
		String sourceCode = new String(retrieveModule(repository, module, "", rootPaths));

		// TODO Delete me
		logger.error("JavaExecutor#getSourceFiles() rootPaths -> " + Arrays.toString(rootPaths));

		// TODO Determine class name (FQN)
		List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		javaFiles.add(new CharSequenceJavaFileObject("com.sap.dirigible.dynamic.test.Calculator",
				sourceCode));
		return javaFiles;
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name, Object value) {
		defaultVariables.put(name, value);
	}
}
