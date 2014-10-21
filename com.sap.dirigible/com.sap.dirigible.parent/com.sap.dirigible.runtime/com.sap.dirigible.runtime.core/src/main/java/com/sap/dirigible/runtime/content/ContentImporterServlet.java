package com.sap.dirigible.runtime.content;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.runtime.logger.Logger;

public class ContentImporterServlet extends BaseContentServlet {
	
	private static final long serialVersionUID = 5844468087553458293L;
	
	private static final Logger logger = Logger.getLogger(ContentImporterServlet.class);
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("Importing content...");
		InputStream in = request.getInputStream();
		importZipAndUpdate(in);
		logger.debug("Content imported.");
	}
	
}
