package com.sap.dirigible.runtime.content;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public class ContentExporterServlet extends HttpServlet {

	private static final String COM_SAP_DIRIGIBLE_RUNTIME = "com.sap.dirigible.runtime"; //$NON-NLS-1$

	private static final String COULD_NOT_INITIALIZE_REPOSITORY = Messages
			.getString("ContentInitializerServlet.COULD_NOT_INITIALIZE_REPOSITORY"); //$NON-NLS-1$
	private static final long serialVersionUID = 6468050094756163896L;
	private static final String REPOSITORY_ATTRIBUTE = "com.sap.dirigible.services.content.repository"; //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(ContentExporterServlet.class);

	private static final String SYSTEM_USER = "SYSTEM"; //$NON-NLS-1$
	private static final String DEFAULT_PATH_FOR_EXPORT = IRepositoryPaths.REGISTRY_DEPLOY_PATH;
	private static final String DEFAULT_PATH_FOR_IMPORT = IRepositoryPaths.REGISTRY_IMPORT_PATH; //$NON-NLS-1$

	// Folder in the generated zip-file (com.sap.dirigible.runtime*.zip) for
	// exported content
	private static final String EXPORTED_PATH = "exported/"; //$NON-NLS-1$
	private static final String EXPORTED_CONTENT = "content"; //$NON-NLS-1$
	private static final String ZIP = ".zip"; //$NON-NLS-1$

	private static final String GUID = "guid"; //$NON-NLS-1$

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ContentExporterServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logit("Import Servlet Init"); //$NON-NLS-1$
		initRepository();
		try {
			checkAndImportContent(SYSTEM_USER);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		logit("Import Servlet Init finished successfuly."); //$NON-NLS-1$
	}

	/**
	 * Helper method.
	 * 
	 * @return
	 * @throws IOException
	 */
	private void initRepository() throws ServletException {
		try {
			final IRepository repository = RepositoryFacade.getInstance().getRepository(null);
			getServletContext().setAttribute(REPOSITORY_ATTRIBUTE, repository);
		} catch (Exception ex) {
			logit("Exception in initRepository(): " + ex.getMessage()); //$NON-NLS-1$
			throw new ServletException(COULD_NOT_INITIALIZE_REPOSITORY, ex);
		}
	}

	/**
	 * Helper method.
	 * 
	 * @return
	 * @throws IOException
	 */
	private IRepository getRepository() throws IOException {
		final IRepository repository = (IRepository) getServletContext().getAttribute(
				REPOSITORY_ATTRIBUTE);
		if (repository == null) {
			try {
				initRepository();
			} catch (ServletException e) {
				logit("Exception in getRepository(): " + e.getMessage()); //$NON-NLS-1$
				throw new IOException(e);
			}
		}
		return repository;
	}

	/**
	 * Check if there is any content, and deploy it into Dirigible registry.
	 * 
	 * @throws IOException
	 */
	private void checkAndImportContent(String user) throws IOException {
		logit("checkAndImporContent: Entering"); //$NON-NLS-1$
		if (existsContentToImport()) {
			// Clean registry from eventual old content
			ICollection collection = getRepository().getCollection(DEFAULT_PATH_FOR_IMPORT);
			if (collection.exists()) {
				collection.delete();
				collection.create();
			}
			// Get all files under CONTENT folder. Possibility to import
			// multiple content files. TODO - is it necessary?
			Set<String> paths = getServletContext().getResourcePaths(
					IRepository.SEPARATOR + EXPORTED_PATH); //$NON-NLS-1$
			logit("resource paths:" + paths); //$NON-NLS-1$
			for (Iterator<String> iterator = paths.iterator(); iterator.hasNext();) {
				String pathToContent = (String) iterator.next();
				logit("Path to content: " + pathToContent); //$NON-NLS-1$
				if (!pathToContent.endsWith(ZIP)) {
					continue;
				}
				InputStream content = getServletContext().getResourceAsStream(pathToContent);
				importZipAndUpdate(content);
				logit(" Successfully imported " + pathToContent); //$NON-NLS-1$
			}
		} else {
			logit(" Nothing to import. Folder" + IRepository.SEPARATOR + EXPORTED_PATH //$NON-NLS-1$
					+ " was not found in deployable."); //$NON-NLS-1$

		}
	}

	/**
	 * Import input stream as a content into repository and execute db updates.
	 * TODO - can be used with POST request to import only a zip
	 * 
	 * @param content
	 */
	private void importZipAndUpdate(InputStream content) {
		try {
			// 1. Import content.zip into repository
			getRepository().importZip(new ZipInputStream(content), DEFAULT_PATH_FOR_IMPORT);

			// 2. Post import actions
			ContentPostImportUpdater contentPostImportUpdater = new ContentPostImportUpdater(
					getRepository());
			contentPostImportUpdater.update();

		} catch (NamingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Check if the path exists in current dirigible.runtime.war
	 * 
	 * @return
	 */
	private boolean existsContentToImport() {
		Set<String> paths = getServletContext().getResourcePaths(IRepository.SEPARATOR);
		return paths.contains(IRepository.SEPARATOR + EXPORTED_PATH);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// put guid in the session
		request.getSession().setAttribute(GUID, createGUID()); //$NON-NLS-1$

		byte[] zippedContent = getContentFromRepository();

		sendZip(request, response, zippedContent);
	}

	/**
	 * Return the constructed zip in servlet response
	 * 
	 * @param response
	 * @param tmpFile
	 * @param request
	 */
	private void sendZip(HttpServletRequest request, HttpServletResponse response, byte[] content) {
		String fileName = null;

		fileName = defaultFileName(request) + ".zip";

		response.setContentType("application/zip"); //$NON-NLS-1$
		response.setHeader("Content-Disposition", "attachment;filename=\"" //$NON-NLS-1$ //$NON-NLS-2$
				+ fileName + "\""); //$NON-NLS-1$
		try {
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			ByteArrayInputStream fis = new ByteArrayInputStream(content);
			int len;
			byte[] buf = new byte[1024];
			while ((len = fis.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}
			bos.close();
			fis.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private String defaultFileName(HttpServletRequest request) {
		String fileName;
		String guid = "".equals(getGUID(request)) ? "" : "." //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ getGUID(request);
		fileName = COM_SAP_DIRIGIBLE_RUNTIME + guid;
		return fileName;
	}

	/**
	 * Extract the Dirigible project as a zip from the repository.
	 * 
	 * @param request
	 * @return
	 */
	private byte[] getContentFromRepository() {
		byte[] zippedContent = null;
		try {
			IRepository repository = getRepository();
			zippedContent = repository.exportZip(DEFAULT_PATH_FOR_EXPORT, true);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return zippedContent;
	}

	/**
	 * Log.
	 * 
	 * @param message
	 */
	private void logit(String message) {
		logger.info(message);
	}

	/**
	 * Create guid. Currently timestamp.
	 * 
	 * @return
	 */
	private String createGUID() {
		// SimpleDateFormat sdfDate = new
		// SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd-HHmmss"); //$NON-NLS-1$
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	private String getGUID(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(GUID);
	}

}