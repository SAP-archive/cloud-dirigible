package com.sap.dirigible.runtime.java.dynamic.compilation;

public class InMemoryCompilationException extends RuntimeException {

	private static final long serialVersionUID = -7902267851162966022L;

	public InMemoryCompilationException(
			InMemoryDiagnosticListener diagnosticListener) {
		this(diagnosticListener.getErrors());
	}
	
	public InMemoryCompilationException(String message){
		super(message);
	}
	
	public InMemoryCompilationException(Throwable cause){
		super(cause);
	}
}
