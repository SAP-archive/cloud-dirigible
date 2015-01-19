package com.sap.dirigible.runtime.scripting;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IScriptExecutor {

	public abstract Object executeServiceModule(HttpServletRequest request,
			HttpServletResponse response, String module,
			Map<Object, Object> executionContext) throws IOException;

}