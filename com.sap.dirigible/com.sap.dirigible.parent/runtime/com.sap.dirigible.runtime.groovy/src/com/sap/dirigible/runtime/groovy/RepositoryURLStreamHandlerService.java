package com.sap.dirigible.runtime.groovy;

import groovy.util.ResourceException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.service.url.AbstractURLStreamHandlerService;

public class RepositoryURLStreamHandlerService extends AbstractURLStreamHandlerService {
	
	@Override
	public URLConnection openConnection(URL u) throws IOException {
		try {
			RepositoryResourceConnector repositoryResourceConnector =
					new RepositoryResourceConnector();
			return repositoryResourceConnector.getResourceConnection(u.toString());
		} catch (ResourceException e) {
			throw new IOException(e);
		}
	}

}
