package com.sap.dirigible.repository.rcp;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import com.sap.dirigible.repository.api.IRepository;

public class RCPWorkspaceMapper {
	
	private static Map<String, String> prefixMap = Collections.synchronizedMap(new HashMap<String, String>());
	private static Map<String, String> prefixMapEquals = Collections.synchronizedMap(new HashMap<String, String>());
	
	public static String getMappedName(String repositoryName) throws IOException {
		String workspaceName = null;
		
		if (repositoryName != null) {
			check();
			
			for (Entry<String, String> prefix: prefixMapEquals.entrySet()) {
				if (repositoryName.equals(prefix.getKey())) {
//					workspaceName = repositoryName.replace(prefix.getKey(), prefix.getValue());
					workspaceName = prefix.getValue();
					break;
				}
			}
			
			if (workspaceName == null) {
				for (Entry<String, String> prefix: prefixMap.entrySet()) {
					if (repositoryName.startsWith(prefix.getKey())
							&& !prefix.getKey().equals("/")) {
						workspaceName = repositoryName.replace(prefix.getKey(), prefix.getValue());
						break;
					}
				}
			}
		}
		
		if (workspaceName == null) {
			workspaceName = repositoryName;
//			throw new IOException("No workspace mapping for file: " + repositoryName);
		}
		
		return workspaceName;
	}
	
	private static void check() throws IOException {
		if (prefixMap.isEmpty()) {
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			File workspaceDirectory = workspace.getRoot().getLocation().toFile();
			String workspaceRoot = "/";
			workspaceRoot = workspaceDirectory.getCanonicalPath();
			
			
			prefixMap.put("/db/dirigible/users/local/workspace", workspaceRoot);
			prefixMap.put(workspaceRoot + "/db/dirigible/users/local/workspace", workspaceRoot);
			
			prefixMap.put("/db/dirigible/registry", workspaceRoot + File.separator + "registry");
			prefixMap.put("/db/dirigible/sandbox", workspaceRoot + File.separator + "sandbox");
			prefixMap.put("/db/dirigible/templates", workspaceRoot + File.separator + "templates");
			
			prefixMapEquals.put("/", workspaceRoot);
			prefixMapEquals.put("/db", workspaceRoot + File.separator + "db");
			prefixMapEquals.put("/db/dirigible", workspaceRoot + File.separator + "db" + File.separator + "dirigible");
			prefixMapEquals.put("/db/dirigible/users", workspaceRoot + File.separator + "db" + File.separator + "dirigible" + File.separator + "users");
			prefixMapEquals.put("/db/dirigible/users/local", workspaceRoot + File.separator + "db" + File.separator + "dirigible" + File.separator + "users" + File.separator + "local" );
			
			prefixMapEquals.put("/db/dirigible/registry", workspaceRoot + File.separator + "registry");
			prefixMapEquals.put("/db/dirigible/sandbox", workspaceRoot + File.separator + "sandbox");
			prefixMapEquals.put("/db/dirigible/templates", workspaceRoot + File.separator + "templates");
		}

	}

}
