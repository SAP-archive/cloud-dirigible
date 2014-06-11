package com.sap.dirigible.ide.db.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DatabasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String N_A = "n/a";
	
	private static final long serialVersionUID = 4250022466641459908L;
	
	public DatabasePreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}
	
	@Override
	protected void createFieldEditors() {
		

	}

	@Override
	public void init(IWorkbench workbench) {
		super.initialize();
	}

}
