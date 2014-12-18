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

package com.sap.dirigible.runtime.esb;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.BusFactory;
import org.apache.cxf.resource.ResourceManager;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.AbstractHTTPServlet;
import org.apache.cxf.transport.servlet.ServletContextResourceResolver;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.servicelist.ServiceListGeneratorServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.sap.dirigible.runtime.logger.Logger;

/**
 * Joined CXFNonSpringServlet and CXFServlet with customizations on getting the
 * new instance of bus when ApplicationContext has bean refreshed
 * 
 */
public class CXFServlet extends AbstractHTTPServlet {

	private static final long serialVersionUID = 2807601779782132367L;
	private static final Logger logger = Logger.getLogger(CXFServlet.class.getCanonicalName());

	private DestinationRegistry destinationRegistry;
	private Bus bus;
	private ServletController controller;
	private ClassLoader loader;
	private boolean busCreated;
	private XmlWebApplicationContext createdContext;
	private ApplicationContext externalApplicationContext;

	public CXFServlet() {
	}

	public CXFServlet(DestinationRegistry destinationRegistry) {
		this.destinationRegistry = destinationRegistry;
	}

	@Override
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
	}

	/**
	 * Initialize the CXF Bus
	 * 
	 * @param sc
	 */
	private void initBus(ServletConfig sc) {
		// always call load bus here w/o check for null
		loadBus(sc);

		loader = bus.getExtension(ClassLoader.class);
		ResourceManager resourceManager = bus
				.getExtension(ResourceManager.class);
		resourceManager.addResourceResolver(new ServletContextResourceResolver(
				sc.getServletContext()));
		// always get destination registry here w/o check for null
		this.destinationRegistry = getDestinationRegistryFromBus(this.bus);
		// always create servlet controller here w/o check for null
		this.controller = createServletController(sc);
	}

	/**
	 * Retrieve the Destination Registry form the Bus
	 * 
	 * @param bus
	 * @return
	 */
	private static DestinationRegistry getDestinationRegistryFromBus(Bus bus) {
		DestinationFactoryManager dfm = bus
				.getExtension(DestinationFactoryManager.class);
		try {
			DestinationFactory df = dfm
					.getDestinationFactory("http://cxf.apache.org/transports/http/configuration"); //$NON-NLS-1$
			if (df instanceof HTTPTransportFactory) {
				HTTPTransportFactory transportFactory = (HTTPTransportFactory) df;
				return transportFactory.getRegistry();
			}
		} catch (BusException e) {
			// why are we throwing a busexception if the DF isn't found?
		}
		return null;
	}

	/**
	 * Load the bUs depending on the location of the context file
	 * 
	 * @param sc
	 */
	protected void loadBus(ServletConfig sc) {
		externalApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(sc.getServletContext());
		String configLocation = sc.getInitParameter("config-location"); //$NON-NLS-1$
		if (configLocation == null) {
			try {
				InputStream is = sc.getServletContext().getResourceAsStream(
						"/WEB-INF/cxf-servlet.xml"); //$NON-NLS-1$
				if (is != null && is.available() > 0) {
					is.close();
					configLocation = "/WEB-INF/cxf-servlet.xml"; //$NON-NLS-1$
				}
			} catch (Exception ex) {
				// ignore
			}
		}
		if (configLocation != null) {
			externalApplicationContext = createSpringContext(
					externalApplicationContext, sc, configLocation);
		}
		if (externalApplicationContext != null) {
			setBus(externalApplicationContext.getBean("cxf", Bus.class)); //$NON-NLS-1$
		} else {
			busCreated = true;
			setBus(BusFactory.newInstance().createBus());
		}
	}

	/**
	 * Create the CXF's own servlet controller
	 * 
	 * @param servletConfig
	 * @return
	 */
	private ServletController createServletController(
			ServletConfig servletConfig) {
		HttpServlet serviceListGeneratorServlet = new ServiceListGeneratorServlet(
				destinationRegistry, bus);
		ServletController newController = new ServletController(
				destinationRegistry, servletConfig, serviceListGeneratorServlet);
		return newController;
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	@Override
	protected void invoke(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		ClassLoader origLoader = Thread.currentThread().getContextClassLoader();
		try {
			if (loader != null) {
				Thread.currentThread().setContextClassLoader(loader);
			}

			externalApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(getServletConfig()
							.getServletContext());
			// lookup eventually refreshed (new) Bus
			Bus maybeNew = externalApplicationContext.getBean("cxf", Bus.class); //$NON-NLS-1$
			if (maybeNew != null && (!(maybeNew.equals(bus)))) {
				// it is actually new instance here
				destroy();
				initBus(getServletConfig());
				logger.info("Reloading CXF Bus at the Servlet"); //$NON-NLS-1$
			}

			BusFactory.setThreadDefaultBus(bus);
			controller.invoke(request, response);
		} finally {
			BusFactory.setThreadDefaultBus(null);
			Thread.currentThread().setContextClassLoader(origLoader);
		}
	}

	/**
	 * Release the old Bus
	 */
	public void destroy() {
		if (destinationRegistry != null) {
			for (String path : destinationRegistry.getDestinationsPaths()) {
				// clean up the destination in case the destination itself can
				// no
				// longer access the registry later
				AbstractHTTPDestination dest = destinationRegistry
						.getDestinationForPath(path);
				synchronized (dest) {
					destinationRegistry.removeDestination(path);
					dest.releaseRegistry();
				}
			}
			destinationRegistry = null;
		}
		destroyBus();
	}

	private ApplicationContext createSpringContext(ApplicationContext ctx,
			ServletConfig sc, String location) {
		XmlWebApplicationContext ctx2 = new XmlWebApplicationContext();
		createdContext = ctx2;
		ctx2.setServletConfig(sc);

		Resource r = ctx2.getResource(location);
		try {
			InputStream in = r.getInputStream();
			in.close();
		} catch (IOException e) {
			// ignore
			r = ctx2.getResource("classpath:" + location); //$NON-NLS-1$
			try {
				r.getInputStream().close();
			} catch (IOException e2) {
				// ignore
				r = null;
			}
		}
		try {
			if (r != null) {
				location = r.getURL().toExternalForm();
			}
		} catch (IOException e) {
			// ignore
		}
		if (ctx != null) {
			ctx2.setParent(ctx);
			String names[] = ctx.getBeanNamesForType(Bus.class);
			if (names == null || names.length == 0) {
				ctx2.setConfigLocations(new String[] {
						"classpath:/META-INF/cxf/cxf.xml", location }); //$NON-NLS-1$
			} else {
				ctx2.setConfigLocations(new String[] { location });
			}
		} else {
			ctx2.setConfigLocations(new String[] {
					"classpath:/META-INF/cxf/cxf.xml", location }); //$NON-NLS-1$
			createdContext = ctx2;
		}
		ctx2.refresh();
		return ctx2;
	}

	public void destroyBus() {
		if (busCreated) {
			// if we created the Bus, we need to destroy it. Otherwise, spring
			// will handle it.
			getBus().shutdown(true);
		}
		if (createdContext != null) {
			createdContext.close();
		}
	}
}
