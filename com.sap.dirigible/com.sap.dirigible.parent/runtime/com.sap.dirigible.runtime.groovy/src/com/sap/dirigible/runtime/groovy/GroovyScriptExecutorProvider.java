package com.sap.dirigible.runtime.groovy;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class GroovyScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.GROOVY;
	}
	
	@Override
	public String getAlias() {
		return ICommonConstants.ENGINE_ALIAS.GROOVY;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		GroovyServlet groovyServlet = new GroovyServlet();
		GroovyExecutor groovyExecutor = groovyServlet.createExecutor(request);
		return groovyExecutor;		
	}

}
