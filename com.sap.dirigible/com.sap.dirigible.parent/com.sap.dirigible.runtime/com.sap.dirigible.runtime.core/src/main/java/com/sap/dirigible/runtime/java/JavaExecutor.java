package com.sap.dirigible.runtime.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
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
import javax.tools.ToolProvider;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.java.dynamic.compilation.CharSequenceJavaFileObject;
import com.sap.dirigible.runtime.java.dynamic.compilation.ClassFileManager;
import com.sap.dirigible.runtime.java.dynamic.compilation.DynamicJavaCompilationDiagnosticListener;
import com.sap.dirigible.runtime.java.dynamic.compilation.DynamicJavaCompilationException;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;
import com.sap.dirigible.runtime.scripting.Module;

public class JavaExecutor extends AbstractScriptExecutor {

	private static final String JAVA_EXTENSION = ".java";

	private static final String DOT = ".";

	private static final String SLASH = "/";

	private static final String CLASSPATH = "-classpath";

	private static final String PATH_SEPARATOR = "path.separator";

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
			Object input, String module) throws DynamicJavaCompilationException {

		registerDefaultVariables(request, response, input, null, repository, null);

		try {

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			DynamicJavaCompilationDiagnosticListener diagnosticListener = new DynamicJavaCompilationDiagnosticListener();
			JavaFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(
					diagnosticListener, null, null));

			CompilationTask compilationTask = compiler
					.getTask(null, fileManager, diagnosticListener,
							Arrays.asList(CLASSPATH, getJars()), null, getSourceFiles());

			Boolean compilationTaskResult = compilationTask.call();

			if (compilationTaskResult == null || !compilationTaskResult.booleanValue()) {
				throw new DynamicJavaCompilationException(diagnosticListener);
			}

			String fqn = getFQN(module);
			Class<?> loadedClass = fileManager.getClassLoader(null).loadClass(fqn);

			Class<?>[] inputParameters = new Class<?>[] { HttpServletRequest.class,
					HttpServletResponse.class, Map.class };

			Method serviceMethod = loadedClass.getMethod("service", inputParameters);
			return serviceMethod.invoke(loadedClass.newInstance(), request, response,
					defaultVariables);
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			throw new DynamicJavaCompilationException(baos.toString());
		}
	}

	private String getJars() throws URISyntaxException, IOException {
		URL url = JavaExecutor.class.getProtectionDomain().getCodeSource().getLocation();
		File libDirectory = new File(url.toURI()).getParentFile();

		String separator = System.getProperty(PATH_SEPARATOR);
		StringBuilder jars = new StringBuilder();

		for (File jar : libDirectory.listFiles()) {
			jars.append(jar.getCanonicalPath() + separator);
		}
		return jars.toString();
	}

	private String getFQN(String module) {
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

	private Iterable<? extends JavaFileObject> getSourceFiles() throws IOException {
		List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		List<Module> modules = retrieveModulesByExtension(repository, JAVA_EXTENSION, rootPaths);
		for (Module module : modules) {
			javaFiles.add(new CharSequenceJavaFileObject(getFQN(module.getPath()), new String(
					module.getContent())));
		}
		return javaFiles;
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name, Object value) {
		defaultVariables.put(name, value);
	}
}
