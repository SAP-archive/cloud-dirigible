package com.sap.dirigible.runtime.java;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptingServlet;

public class JavaServlet extends AbstractScriptingServlet {

	private static final long serialVersionUID = -2029496922201773270L;

	private static final Logger logger = Logger.getLogger(JavaServlet.class);

	@Override
	protected void doExecution(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String module = request.getPathInfo();

		JavaExecutor executor = createExecutor(request);
		try {
			executor.executeServiceModule(request, response, module);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		}
	}

	protected JavaExecutor createExecutor(HttpServletRequest request) throws IOException {
		JavaExecutor executor = new JavaExecutor(getRepository(request),
				getScriptingRegistryPath(request), REGISTRY_SCRIPTING_DEPLOY_PATH);
		return executor;
	}
}
