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

package com.sap.dirigible.runtime.filter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.metrics.AccessLogLocationsSynchronizer;
import com.sap.dirigible.runtime.metrics.AccessLogRecord;
import com.sap.dirigible.runtime.metrics.AccessLogRecordDAO;
import com.sap.dirigible.runtime.registry.PathUtils;

public class AccessLogFilter implements Filter {

	private static final Logger logger = Logger.getLogger(AccessLogFilter.class);

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		String location = PathUtils.extractPath(req);
		AccessLogRecord accessLogRecord = null;
		boolean logLocation = isAccessLogEnabled(location);
		if (logLocation) {
			String pattern = getAccessLogPattern(location);
			accessLogRecord = new AccessLogRecord(req, pattern);
		}
		try {
			chain.doFilter(request, response);
		} finally {
			if (logLocation) {
				try {
					accessLogRecord.setResponseStatus(((HttpServletResponse) response).getStatus());
					accessLogRecord
							.setResponseTime((int) (GregorianCalendar.getInstance().getTime().getTime() - accessLogRecord
									.getTimestamp().getTime()));

					AccessLogRecordDAO.insert(accessLogRecord);
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

	private boolean isAccessLogEnabled(String location) throws ServletException {
		logger.trace("isAccessLogEnabled: " + location);
		for (String accessLogLocation : AccessLogLocationsSynchronizer.getAccessLogLocations()) {
			if (location.startsWith(accessLogLocation)) {
				logger.debug("Access Log Enabled: " + location);
				return true;
			}
		}
		logger.debug("Access Log Not Enabled: " + location);
		return false;
	}

	private String getAccessLogPattern(String location) throws ServletException {
		logger.debug("entering getAccessLogPattern: " + location);
		for (String accessLogLocation : AccessLogLocationsSynchronizer.getAccessLogLocations()) {
			if (location.startsWith(accessLogLocation)) {
				logger.debug("Access Log for Location: " + location + " by pattern: "
						+ accessLogLocation);
				return accessLogLocation;
			}
		}
		logger.debug("exiting getAccessLogPattern: " + location);
		return null;
	}

}
