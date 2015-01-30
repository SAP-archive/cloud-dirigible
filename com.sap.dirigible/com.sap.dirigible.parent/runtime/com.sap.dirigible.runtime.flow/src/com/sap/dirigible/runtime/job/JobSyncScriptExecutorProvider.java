package com.sap.dirigible.runtime.job;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.IScriptExecutorProvider;

public class JobSyncScriptExecutorProvider implements
		IScriptExecutorProvider {

	@Override
	public String getType() {
		return ICommonConstants.ENGINE_TYPE.JOB;
	}
	
	@Override
	public String getAlias() {
		return ICommonConstants.ENGINE_ALIAS.JOB;
	}

	@Override
	public IScriptExecutor createExecutor(HttpServletRequest request) throws IOException {
		JobSyncServlet jobServlet = new JobSyncServlet();
		JobSyncExecutor jobSyncExecutor = jobServlet.createExecutor(request);
		return jobSyncExecutor;
	}

}
