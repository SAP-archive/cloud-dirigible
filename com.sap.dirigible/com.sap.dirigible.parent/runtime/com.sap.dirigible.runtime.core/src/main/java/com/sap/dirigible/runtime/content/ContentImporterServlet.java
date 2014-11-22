package com.sap.dirigible.runtime.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sap.dirigible.runtime.logger.Logger;

public class ContentImporterServlet extends BaseContentServlet {

	private static final long serialVersionUID = 5844468087553458293L;

	private static final Logger logger = Logger
			.getLogger(ContentImporterServlet.class);

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				List<FileItem> items = upload.parseRequest(request);
				logger.debug("Importing multiple content...");
				for (FileItem fileItem : items) {
					logger.debug("Importing " + fileItem.getFieldName());
					InputStream in = fileItem.getInputStream();
					importZipAndUpdate(in);
					logger.debug("Content imported.");
				}
			} catch (FileUploadException e) {
				logger.error(e.getMessage());
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.debug("Importing single content...");
			InputStream in = request.getInputStream();
			importZipAndUpdate(in);
			logger.debug("Content imported.");

		}
	}

}
