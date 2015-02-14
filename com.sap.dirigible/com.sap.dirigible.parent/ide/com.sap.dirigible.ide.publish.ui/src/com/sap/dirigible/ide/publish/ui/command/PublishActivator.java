package com.sap.dirigible.ide.publish.ui.command;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.repository.logging.Logger;

public class PublishActivator implements BundleActivator {
	
	private static final Logger logger = Logger.getLogger(PublishActivator.class);

	@Override
	public void start(BundleContext arg0) throws Exception {
		try {
			if (CommonParameters.isRCP()) {
				new AutoActivateAction().init(null);
			}
		} catch (Exception e) {
			logger.error("Auto Activator has not been registered", e);
		}
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		//
	}

}
