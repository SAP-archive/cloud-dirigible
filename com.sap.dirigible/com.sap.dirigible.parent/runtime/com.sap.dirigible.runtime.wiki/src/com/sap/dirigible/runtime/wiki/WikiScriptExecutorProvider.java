package com.sap.dirigible.runtime.wiki;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class WikiScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.WIKI;
	}
	
	@Override
	public String getAlias() {
		return ICommonConstants.ENGINE_ALIAS.WIKI;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		WikiRegistryServlet wikiServlet = new WikiRegistryServlet();
		IScriptExecutor wikiExecutor = wikiServlet.createExecutor(request);
		return wikiExecutor;		
	}

}
