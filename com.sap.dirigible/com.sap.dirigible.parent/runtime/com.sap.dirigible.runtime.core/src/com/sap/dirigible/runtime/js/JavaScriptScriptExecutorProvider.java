package com.sap.dirigible.runtime.js;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class JavaScriptScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.JAVASCRIPT;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		JavaScriptServlet javaScriptServlet = new JavaScriptServlet();
		JavaScriptExecutor javaScriptExecutor = javaScriptServlet.createExecutor(request);
		return javaScriptExecutor;		
	}

}
