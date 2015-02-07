package com.sap.dirigible.runtime.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class WebScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.WEB;
	}
	
	@Override
	public String getAlias() {
		return ICommonConstants.ENGINE_ALIAS.WEB;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		WebRegistryServlet webServlet = new WebRegistryServlet();
		WebExecutor webExecutor = webServlet.createExecutor(request);
		return webExecutor;		
	}

}
