package com.sap.dirigible.runtime.logger;

import org.slf4j.LoggerFactory;

public class LoggerStub extends Logger {

	public static Logger getLogger(String name) {
		return new LoggerStub(LoggerFactory.getLogger(name), java.util.logging.Logger.getLogger(name));
	}

	public static Logger getLogger(Class<?> clazz) {
		return new LoggerStub(LoggerFactory.getLogger(clazz), java.util.logging.Logger.getLogger(clazz
				.getCanonicalName()));
	}
	
	protected LoggerStub(org.slf4j.Logger logger1,
			java.util.logging.Logger logger2) {
		super(logger1, logger2);
		super.printInSystemOutput = true;
	}
}
