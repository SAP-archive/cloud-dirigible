package com.sap.dirigible.runtime.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.Platform;

import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.java.dynamic.compilation.ClassFileManager;
import com.sap.dirigible.runtime.scripting.AbstractScriptingServlet;

public class JavaServlet extends AbstractScriptingServlet {

	private static final long serialVersionUID = -2029496922201773270L;

	private static final Logger logger = Logger.getLogger(JavaServlet.class);
	
	private File libDirectory;
	
	private String classpath;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		String eclipseLauncherProperty = System.getProperty("osgi.syspath");
		logger.debug("osgi.syspath: " + eclipseLauncherProperty);
		if (eclipseLauncherProperty == null) {
			eclipseLauncherProperty = System.getProperty("user.dir");
			logger.debug("user.dir: " + eclipseLauncherProperty);
		}
		eclipseLauncherProperty = eclipseLauncherProperty.replace("/./", "/");
		libDirectory = new File(eclipseLauncherProperty);
		if (libDirectory.exists()
				&& libDirectory.getParentFile() != null
				&& libDirectory.getParentFile().exists()) {
			libDirectory = libDirectory.getParentFile();
		}
		try {
			getClasspath();
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doExecution(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String module = request.getPathInfo();

		JavaExecutor executor = createExecutor(request);
		Map<Object, Object> executionContext = new HashMap<Object, Object>();
		try {
			executor.executeServiceModule(request, response, module, executionContext);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	private String getClasspath() throws IOException {
		if (this.classpath == null) {
			if (this.libDirectory != null) {
				this.classpath = ClassFileManager.getJars(this.libDirectory);
			} else {
				this.classpath = Platform.getInstallLocation().getURL().toString();
			}
		}
		return this.classpath;
	}

	public JavaExecutor createExecutor(HttpServletRequest request) throws IOException {
		JavaExecutor executor = new JavaExecutor(getRepository(request), getClasspath(),
				getScriptingRegistryPath(request), REGISTRY_SCRIPTING_DEPLOY_PATH);
		return executor;
	}
}
