package com.sap.dirigible.ide.workspace.dual;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.sap.dirigible.runtime.content.ContentInitializerServlet;


public class RCPWorkspaceActivator extends AbstractUIPlugin {

	private BundleContext context;
	
	public RCPWorkspaceActivator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.start(context);
		this.context = context;
		ContentInitializerServlet initializerServlet = new ContentInitializerServlet();
//		initializerServlet.initDefaultContent(null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.stop(context);
	}

}
