package com.sap.dirigible.ide.workspace.dual;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import com.sap.dirigible.ide.common.CommonParameters;

public class WorkspaceLocator {

	public static IWorkspace getWorkspace() {
		CommonParameters.initSystemParameters();
		return ResourcesPlugin.getWorkspace();
	}
	
	public static IWorkspace getWorkspace(String user) {
		return getWorkspace();
	}
	
	public static String getRepositoryPathForWorkspace(IWorkspace workspace) {
		return workspace.getRoot().getLocation().toString();
	}
	
}
