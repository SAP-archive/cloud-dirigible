package com.sap.dirigible.runtime.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class CommandScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.COMMAND;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		CommandServlet commandServlet = new CommandServlet();
		CommandExecutor commandExecutor = commandServlet.createExecutor(request);
		return commandExecutor;		
	}

}
