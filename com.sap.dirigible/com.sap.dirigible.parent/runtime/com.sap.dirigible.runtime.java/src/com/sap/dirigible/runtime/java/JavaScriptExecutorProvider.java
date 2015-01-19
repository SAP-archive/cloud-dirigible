package com.sap.dirigible.runtime.java;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class JavaScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.JAVA;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		JavaServlet javaServlet = new JavaServlet();
		JavaExecutor javaExecutor = javaServlet.createExecutor(request);
		return javaExecutor;		
	}

}
