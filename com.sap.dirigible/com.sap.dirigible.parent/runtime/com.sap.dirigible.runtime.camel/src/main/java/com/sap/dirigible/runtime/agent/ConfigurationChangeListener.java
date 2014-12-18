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

package com.sap.dirigible.runtime.agent;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.runtime.logger.Logger;

public class ConfigurationChangeListener {

	private static final Logger logger = Logger.getLogger(ConfigurationChangeListener.class.getCanonicalName());

	public void onMessage(String content, HttpServletRequest request)
			throws IOException {

		logger.debug("entering: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "onMessage"); //$NON-NLS-1$

		try {
			RuntimeBridgeUtils.pushMessage(content, request);
		} catch (Exception e) {
			logger.error("onMessage", e); //$NON-NLS-1$
			throw new IOException(e);
		}

		logger.debug("exiting: " + this.getClass().getCanonicalName() + " -> " //$NON-NLS-1$ //$NON-NLS-2$
				+ "onMessage"); //$NON-NLS-1$

	}

}
