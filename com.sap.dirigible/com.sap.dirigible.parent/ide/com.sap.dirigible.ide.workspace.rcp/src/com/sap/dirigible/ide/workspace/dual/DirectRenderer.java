package com.sap.dirigible.ide.workspace.dual;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.sap.dirigible.runtime.scripting.IScriptExecutor;
import com.sap.dirigible.runtime.scripting.utils.EngineUtils;

public class DirectRenderer {
	
	public static final String SANDBOX_CONTEXT = "sandbox"; //$NON-NLS-1$
	public static final String DEBUG_CONTEXT = "debug"; //$NON-NLS-1$
	
	public static String renderContent(String location) {
		String message = null;
		try {
			String url = "http://local:0" + location;
			String module = location.substring(1);
			int moduleStart = module.indexOf('/');
			module = module.substring(moduleStart);
			String alias = location.substring(1, moduleStart + 1);
			int dash = alias.indexOf('-');
			if (dash > 0) {
				alias = alias.substring(0, dash);
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			LocalHttpServletRequest request = new LocalHttpServletRequest(new URL(url)); 
			LocalHttpServletResponse response = new LocalHttpServletResponse(baos);
			
			if (location.contains(SANDBOX_CONTEXT)) {
				request.setAttribute(SANDBOX_CONTEXT, true);
			}
			if (location.contains(DEBUG_CONTEXT)) {
				request.setAttribute(DEBUG_CONTEXT, true);
			}
			
			Map<Object, Object> executionContext = new HashMap<Object, Object>();
			// type
			IScriptExecutor scriptExecutor = EngineUtils.createExecutorByAlias(alias, request);
			// location/module
			scriptExecutor.executeServiceModule(request, response, module, executionContext);
			return new String(baos.toByteArray(), "UTF-8");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = e.getMessage();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = e.getMessage();
		}
		return message;
	}

}
