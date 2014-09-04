package com.sap.dirigible.runtime.java.dynamic.compilation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class DynaCompTest {
	public static void main(String[] args) throws Exception {

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		JavaFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null,
				null, null));

		compiler.getTask(null, fileManager, null, null, null, getSourceFiles()).call();

		ClassLoader classLoader = fileManager.getClassLoader(null);
		Class<?> loadedClass = classLoader.loadClass("com.sap.dirigible.dynamic.test.Calculator");

		Object instance = loadedClass.newInstance();
		Method serviceMethod = loadedClass.getMethod("doGet", new Class<?>[] { String.class,
				int.class });

		serviceMethod.invoke(instance, "Working ...", 3);
	}

	private static Iterable<? extends JavaFileObject> getSourceFiles() {
		List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		javaFiles.add(new CharSequenceJavaFileObject("com.sap.dirigible.dynamic.test.Calculator",
				getSourceCode("Calculator.jaas")));
		javaFiles.add(new CharSequenceJavaFileObject("com.sap.dirigible.dynamic.test.Utils",
				getSourceCode("Utils")));
		return javaFiles;
	}

	private static CharSequence getSourceCode(String fileName) {
		StringBuilder sourceCode = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					DynaCompTest.class.getResourceAsStream(fileName)));

			String line = null;
			for (line = reader.readLine(); line != null; line = reader.readLine()) {
				sourceCode.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sourceCode;
	}
}
