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

package com.sap.dirigible.runtime.registry;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.repository.api.IRepository;

public class PathUtils {
	
	public static String extractPath(HttpServletRequest request)
			throws IllegalArgumentException {
		String requestPath = request.getPathInfo();
		if (requestPath == null) {
			requestPath = IRepository.SEPARATOR; //$NON-NLS-1$
		}
		return requestPath;
	}
	
	public static String getHeadingUrl(final HttpServletRequest req, String mapping) {
        final String scheme = req.getScheme() + "://"; //$NON-NLS-1$
        final String serverName = req.getServerName();
        final String serverPort = (req.getServerPort() == 80) ? "" : ":" //$NON-NLS-1$ //$NON-NLS-2$
                + req.getServerPort();
        final String contextPath = req.getContextPath();
        return scheme + serverName + serverPort + contextPath
                + mapping;
    }

}
