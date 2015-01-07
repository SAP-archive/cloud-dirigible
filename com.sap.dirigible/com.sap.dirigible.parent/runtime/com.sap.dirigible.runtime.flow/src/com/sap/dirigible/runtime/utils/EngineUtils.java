package com.sap.dirigible.runtime.utils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.runtime.command.CommandExecutor;
import com.sap.dirigible.runtime.command.CommandServlet;
import com.sap.dirigible.runtime.flow.FlowExecutor;
import com.sap.dirigible.runtime.flow.FlowServlet;
import com.sap.dirigible.runtime.groovy.GroovyExecutor;
import com.sap.dirigible.runtime.groovy.GroovyServlet;
import com.sap.dirigible.runtime.js.JavaScriptExecutor;
import com.sap.dirigible.runtime.js.JavaScriptServlet;

public class EngineUtils {
	
	public static Object executeFlow(HttpServletRequest request,
			HttpServletResponse response, Map<Object, Object> executionContext,
			Object inputOutput, String module) throws IOException {
		FlowServlet flowServlet = new FlowServlet();
		FlowExecutor flowExecutor = flowServlet.createExecutor(request);
		inputOutput = flowExecutor.executeServiceModule(request, response, inputOutput, module, executionContext);
		return inputOutput;
	}

	public static Object executeCommand(HttpServletRequest request,
			HttpServletResponse response, Map<Object, Object> executionContext,
			Object inputOutput, String module) throws IOException {
		CommandServlet commandServlet = new CommandServlet();
		CommandExecutor commandExecutor = commandServlet.createExecutor(request);
		inputOutput = commandExecutor.executeServiceModule(request, response, inputOutput, module, executionContext);
		return inputOutput;
	}

	public static Object executeJavascript(HttpServletRequest request,
			HttpServletResponse response, Map<Object, Object> executionContext,
			Object inputOutput, String module) throws IOException {
		JavaScriptServlet javaScriptServlet = new JavaScriptServlet();
		JavaScriptExecutor javaScriptExecutor = javaScriptServlet.createExecutor(request);
		inputOutput = javaScriptExecutor.executeServiceModule(request, response, inputOutput, module, executionContext);
		return inputOutput;
	}
	
	public static Object executeGroovy(HttpServletRequest request,
			HttpServletResponse response, Map<Object, Object> executionContext,
			Object inputOutput, String module) throws IOException {
		GroovyServlet groovyServlet = new GroovyServlet();
		GroovyExecutor groovyExecutor = groovyServlet.createExecutor(request);
		inputOutput = groovyExecutor.executeServiceModule(request, response, inputOutput, module, executionContext);
		return inputOutput;
	}

	
//	public static Object executeJava(HttpServletRequest request,
//			HttpServletResponse response, Map<Object, Object> executionContext,
//			Object inputOutput, FlowStep flowStep) throws IOException {
//		JavaServlet javaServlet = new JavaServlet();
//		JavaExecutor javaExecutor = javaServlet.createExecutor(request);
//		inputOutput = javaExecutor.executeServiceModule(request, response, inputOutput, module, executionContext);
//		return inputOutput;
//	}

}
