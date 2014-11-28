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

package com.sap.dirigible.runtime.js.debug;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.sap.dirigible.runtime.logger.Logger;

public class InvocationErrorReporter implements ErrorReporter {

	private static final String MESSAGE_SOURCE_AT_LINE_LINESOURCE_LINEOFFSET = "%s, source: %s, at line: %s, line source: %s, lineOffset: %s";

	private static final Logger logger = Logger.getLogger(InvocationErrorReporter.class);

	@Override
	public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
		logger.error(String.format(MESSAGE_SOURCE_AT_LINE_LINESOURCE_LINEOFFSET, message,
				sourceName, line, lineSource, lineOffset));
	}

	@Override
	public EvaluatorException runtimeError(String message, String sourceName, int line,
			String lineSource, int lineOffset) {
		logger.error(String.format(MESSAGE_SOURCE_AT_LINE_LINESOURCE_LINEOFFSET, message,
				sourceName, line, lineSource, lineOffset));
		return new EvaluatorException(message, sourceName, line);
	}

	@Override
	public void warning(String message, String sourceName, int line, String lineSource,
			int lineOffset) {
		logger.warn(String.format(MESSAGE_SOURCE_AT_LINE_LINESOURCE_LINEOFFSET, message,
				sourceName, line, lineSource, lineOffset));
	}
}
