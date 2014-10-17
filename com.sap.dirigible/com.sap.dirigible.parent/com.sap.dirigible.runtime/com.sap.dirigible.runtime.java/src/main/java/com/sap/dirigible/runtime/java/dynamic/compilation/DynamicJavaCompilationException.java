package com.sap.dirigible.runtime.java.dynamic.compilation;

public class DynamicJavaCompilationException extends RuntimeException {

	private static final long serialVersionUID = -7902267851162966022L;

	public DynamicJavaCompilationException(
			DynamicJavaCompilationDiagnosticListener diagnosticListener) {
		this(diagnosticListener.getErrors());
	}
	
	public DynamicJavaCompilationException(String message){
		super(message);
	}
	
	public DynamicJavaCompilationException(Throwable cause){
		super(cause);
	}
}
