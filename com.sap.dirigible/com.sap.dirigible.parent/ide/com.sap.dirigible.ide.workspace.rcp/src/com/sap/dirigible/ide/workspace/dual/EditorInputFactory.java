package com.sap.dirigible.ide.workspace.dual;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.part.FileEditorInput;

public class EditorInputFactory {
	
	public static FileEditorInput createInput(IFile file, int row, String contentType) {
		Workspace workspace=(Workspace)ResourcesPlugin.getWorkspace();
		IFile fileRcp = workspace.getRoot().getFile(file.getFullPath());
		FileEditorInput input = new FileEditorInput(fileRcp);
		return input;
	}

}
