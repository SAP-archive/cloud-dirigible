package com.sap.dirigible.ide.workspace.dual;

import org.eclipse.core.resources.IWorkspace;

import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.impl.Workspace;

public class WorkspaceLocator {
	
	public static IWorkspace getWorkspace() {
		return RemoteResourcesPlugin.getWorkspace();
	}

	public static IWorkspace getWorkspace(String user) {
		return (user == null) ? RemoteResourcesPlugin.getWorkspace() : RemoteResourcesPlugin.getWorkspace(user);
	}
	
	public static String getRepositoryPathForWorkspace(IWorkspace workspace) {
		Workspace workspaceRAP = (Workspace) WorkspaceLocator.getWorkspace();
		String root = workspaceRAP.getRepositoryPathForWorkspace(RemoteResourcesPlugin.getUserName());
		return root;
	}
}
