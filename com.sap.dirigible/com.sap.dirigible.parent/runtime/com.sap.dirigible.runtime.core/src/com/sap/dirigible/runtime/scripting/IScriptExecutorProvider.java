package com.sap.dirigible.runtime.scripting;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public interface IScriptExecutorProvider {
	
	public String getType();
	
	public String getAlias();
	
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException;

}
