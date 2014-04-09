package com.sap.dirigible.repository.ext.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
	
	public static String getUser(HttpServletRequest request) {
		String user = "GUEST"; // shared one //$NON-NLS-1$
		try {
			if ((request != null) && (request.getUserPrincipal() != null)) {
				user = request.getUserPrincipal().getName();
			}
		} catch (Exception e) {
			// TODO - do nothing
		}
		return user;
	}

}
