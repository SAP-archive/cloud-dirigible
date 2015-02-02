package com.sap.dirigible.ide.workspace.dual;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class WorkspaceLocator {

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	public static IWorkspace getWorkspace(String user) {
		return ResourcesPlugin.getWorkspace();
	}
	
	public static String getRepositoryPathForWorkspace(IWorkspace workspace) {
		return workspace.getRoot().getLocation().toString();
	}
	
}
