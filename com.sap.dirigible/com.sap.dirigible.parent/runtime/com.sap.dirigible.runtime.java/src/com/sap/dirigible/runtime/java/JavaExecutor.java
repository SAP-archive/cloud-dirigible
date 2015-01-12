package com.sap.dirigible.runtime.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.java.dynamic.compilation.ClassFileManager;
import com.sap.dirigible.runtime.java.dynamic.compilation.InMemoryCompilationException;
import com.sap.dirigible.runtime.java.dynamic.compilation.InMemoryDiagnosticListener;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;

public class JavaExecutor extends AbstractScriptExecutor {
	
	private static final Logger logger = Logger.getLogger(JavaExecutor.class);

	private static final String JAVA_EXTENSION = ".java"; //$NON-NLS-1$
	private static final String CLASSPATH = "-classpath"; //$NON-NLS-1$
	
	public static final String JAVA_TOOLS_COMPILER = "JAVA_TOOLS_COMPILER"; //$NON-NLS-1$

	private IRepository repository;
	private String[] rootPaths;
	private Map<String, Object> defaultVariables;
	private File libDirectory;
	
	private String classpath;

	public JavaExecutor(IRepository repository, File libDirectory, String classpath, String... rootPaths) {
		this.repository = repository;
		this.rootPaths = rootPaths;
		this.defaultVariables = new HashMap<String, Object>();
		this.libDirectory = libDirectory;
		this.classpath = classpath;
	}

	@Override
	public Object executeServiceModule(HttpServletRequest request, HttpServletResponse response,
			Object input, String module, Map<Object, Object> executionContext) throws InMemoryCompilationException {
		try {
			registerDefaultVariables(request, response, input, null, repository, null);
			ClassFileManager fileManager = compile(request);
			return execute(request, response, module, fileManager);
		} catch (Throwable t) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			t.printStackTrace(new PrintStream(baos));
			logger.error(t.getMessage(), t);
			throw new InMemoryCompilationException(baos.toString());
		}
	}

	private ClassFileManager compile(HttpServletRequest request) throws IOException, ClassNotFoundException,
			URISyntaxException {
		List<JavaFileObject> sourceFiles = ClassFileManager.getSourceFiles(retrieveModulesByExtension(repository, JAVA_EXTENSION, rootPaths));

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		if(compiler == null) {
			throw new InMemoryCompilationException("Use JDK instead of JRE");
		}
		InMemoryDiagnosticListener diagnosticListener = new InMemoryDiagnosticListener();
		ClassFileManager fileManager = ClassFileManager.getInstance(compiler.getStandardFileManager(diagnosticListener, null, null));
		CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnosticListener, Arrays.asList(CLASSPATH, getClasspath()), null, sourceFiles);

		Boolean compilationTaskResult = compilationTask.call();

		if (compilationTaskResult == null || !compilationTaskResult.booleanValue()) {
			throw new InMemoryCompilationException(diagnosticListener);
		}
		return fileManager;
	}
	
	public String getClasspath() {
		return classpath;
	}

	private Object execute(HttpServletRequest request, HttpServletResponse response, String module,
			ClassFileManager fileManager) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?> loadedClass = fileManager.getClassLoader(null).loadClass(ClassFileManager.getFQN(module));

		Class<?>[] inputParameters = new Class<?>[] { HttpServletRequest.class, HttpServletResponse.class, Map.class };

		Method serviceMethod = loadedClass.getMethod("service", inputParameters);
		return serviceMethod.invoke(loadedClass.newInstance(), request, response, defaultVariables);
	}
	
	@Override
	protected void registerDefaultVariable(Object scope, String name, Object value) {
		defaultVariables.put(name, value);
	}
}
