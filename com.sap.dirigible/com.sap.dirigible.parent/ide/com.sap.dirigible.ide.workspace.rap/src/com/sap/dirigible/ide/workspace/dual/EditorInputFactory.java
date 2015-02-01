package com.sap.dirigible.ide.workspace.dual;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

import com.sap.dirigible.repository.api.ICommonConstants.ARTIFACT_TYPE;
import com.sap.rap.ui.shared.editor.SourceFileEditorInput;

public class EditorInputFactory {
	
	public static FileEditorInput createInput(IFile file, int row, String contentType) {
		SourceFileEditorInput input = new SourceFileEditorInput(file);
		input.setRow(row);
		breakpointsSupported(file, contentType, input);
		readonlyEnabled(file, contentType, input);
		return input;
	}
	
	static void readonlyEnabled(IFile file, String contentType,
			SourceFileEditorInput input) {
//		input.setReadOnly(true);
	}

	static void breakpointsSupported(IFile file, String contentType,
			SourceFileEditorInput input) {
		if (file.getRawLocation().toString().contains(ARTIFACT_TYPE.SCRIPTING_SERVICES)
				&& contentType != null
				&& contentType.contains("javascript")) {
			input.setBreakpointsEnabled(true);
		}
	}


}
