package com.sap.dirigible.runtime.flow;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class FlowScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.FLOW;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		FlowServlet flowServlet = new FlowServlet();
		FlowExecutor flowExecutor = flowServlet.createExecutor(request);
		return flowExecutor;		
	}

}
