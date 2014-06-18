/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.runtime.logger;

import java.util.logging.Level;

import org.slf4j.LoggerFactory;

/**
 * This is the central class used for logging in the Dirigible project.
 * <p>
 * This class works as a proxy to a real logging library by delegating calls to
 * it. The idea is that if at any moment a new logging library is used,
 * modifications would be confined only to this class.
 * 
 */
public class Logger {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static boolean printInSystemOutput = true;

	/**
	 * Returns a {@link Logger} instance that is bound to the specified name.
	 * <p>
	 * Usually the <code>name</code> parameter should represent the name of the
	 * class that operates on the instance.
	 */
	public static Logger getLogger(String name) {
		return new Logger(LoggerFactory.getLogger(name), java.util.logging.Logger.getLogger(name));
	}

	/**
	 * Shorthand for getLogger(clazz.getName()).
	 */
	public static Logger getLogger(Class<?> clazz) {
		return new Logger(LoggerFactory.getLogger(clazz), java.util.logging.Logger.getLogger(clazz
				.getCanonicalName()));
	}

	private final java.util.logging.Logger delegate2;
	private final org.slf4j.Logger delegate3;

	private Logger(org.slf4j.Logger delegate3, java.util.logging.Logger delegate2) {
		this.delegate3 = delegate3;
		this.delegate2 = delegate2;
	}

	/**
	 * Log a severe error that would most likely lead to an application abort.
	 */
	public void fatal(String message) {
		delegate2.severe(message);
		delegate3.error(message);
		if (printInSystemOutput) {
			System.err.println(message);
		}
	}

	/**
	 * Same as {@link #fatal(String)} with the ability to specify an exception
	 * that caused the logging.
	 */
	public void fatal(String message, Throwable ex) {
		delegate2.log(Level.SEVERE, message, ex);
		delegate3.error(message, ex);
		if (printInSystemOutput) {
			System.err.println(message);
			ex.printStackTrace();
		}
	}

	/**
	 * Log an error that might still allow the application to continue to
	 * operate.
	 */
	public void error(String message) {
		delegate2.severe(message);
		delegate3.error(message);
		if (printInSystemOutput) {
			System.err.println(message);
		}
	}

	/**
	 * Same as {@link #error(String)} with the ability to specify an exception
	 * that caused the logging.
	 */
	public void error(String message, Throwable ex) {
		delegate2.log(Level.SEVERE, message, ex);
		delegate2.throwing(EMPTY_STRING, EMPTY_STRING, ex);
		delegate3.error(message, ex);
		delegate3.trace(message, ex);
		if (printInSystemOutput) {
			System.err.println(message);
			ex.printStackTrace();
		}
	}

	/**
	 * Log a potentially harmful situation.
	 */
	public void warn(String message) {
		delegate2.warning(message);
		delegate3.warn(message);
		if (printInSystemOutput) {
			System.err.println(message);
		}
	}

	/**
	 * Same as {@link #warn(String)} with the ability to specify an exception
	 * that caused the logging.
	 */
	public void warn(String message, Throwable ex) {
		delegate2.warning(message);
		delegate2.throwing(EMPTY_STRING, EMPTY_STRING, ex);
		delegate3.warn(message, ex);
		if (printInSystemOutput) {
			System.err.println(message);
			ex.printStackTrace();
		}
	}

	/**
	 * Log an event in the application or progress.
	 */
	public void info(String message) {
		delegate2.info(message);
		delegate3.info(message);
		if (printInSystemOutput) {
			System.out.println(message);
		}
	}

	/**
	 * Same as {@link #info(String)} with the ability to specify an exception
	 * that caused the logging.
	 */
	public void info(String message, Throwable ex) {
		delegate2.info(message);
		delegate2.throwing(EMPTY_STRING, EMPTY_STRING, ex);
		delegate3.info(message, ex);
		if (printInSystemOutput) {
			System.out.println(message);
			ex.printStackTrace();
		}
	}

	/**
	 * Log fine-grained information events.
	 */
	public void debug(String message) {
		delegate2.info(message);
		delegate3.debug(message);
		if (printInSystemOutput) {
			System.out.println(message);
		}
	}

	/**
	 * Same as {@link #debug(String)} with the ability to specify an exception
	 * that caused the logging.
	 */
	public void debug(String message, Throwable ex) {
		delegate2.info(message);
		delegate2.throwing(EMPTY_STRING, EMPTY_STRING, ex);
		delegate3.debug(message, ex);
		if (printInSystemOutput) {
			System.out.println(message);
			ex.printStackTrace();
		}
	}

	/**
	 * Log the most fine-grained events.
	 */
	public void trace(String message) {
		delegate2.info(message);
		delegate3.trace(message);
		if (printInSystemOutput) {
			System.err.println(message);
		}
	}

	/**
	 * Same as {@link #trace(String)} with the ability to specify an exception
	 * that caused the logging.
	 */
	public void trace(String message, Throwable ex) {
		delegate2.info(message);
		delegate2.throwing(EMPTY_STRING, EMPTY_STRING, ex);
		delegate3.trace(message, ex);
		if (printInSystemOutput) {
			System.err.println(message);
			ex.printStackTrace();
		}
	}

}
