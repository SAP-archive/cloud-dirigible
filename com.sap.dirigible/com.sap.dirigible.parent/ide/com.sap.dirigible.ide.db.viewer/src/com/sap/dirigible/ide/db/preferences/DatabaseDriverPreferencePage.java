package com.sap.dirigible.ide.db.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.sap.dirigible.ide.common.CommonParameters;

public class DatabaseDriverPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final long serialVersionUID = -877187045002896492L;

	public DatabaseDriverPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}
	
	@Override
	protected void createFieldEditors() {
		Text text = null;

		StringFieldEditor databaseDriverNameField = new StringFieldEditor(
				CommonParameters.DATABASE_DRIVER_NAME,
				"&Driver Name:",
		 		getFieldEditorParent());
		text = databaseDriverNameField.getTextControl(getFieldEditorParent());
		text.setEditable(false);
		text.setText(CommonParameters.get(CommonParameters.DATABASE_DRIVER_NAME) != null ? CommonParameters.get(CommonParameters.DATABASE_DRIVER_NAME) : DatabasePreferencePage.N_A);
		addField(databaseDriverNameField);

		
		StringFieldEditor databaseMinorVersionField = new StringFieldEditor(
				CommonParameters.DATABASE_DRIVER_MINOR_VERSION,
				"&Minor Version:",
		 		getFieldEditorParent());
		text = databaseMinorVersionField.getTextControl(getFieldEditorParent());
		text.setEditable(false);
		text.setText(CommonParameters.get(CommonParameters.DATABASE_DRIVER_MINOR_VERSION) != null ? CommonParameters.get(CommonParameters.DATABASE_DRIVER_MINOR_VERSION) : DatabasePreferencePage.N_A);
		addField(databaseMinorVersionField);
		
		StringFieldEditor databaseMajorVersionField = new StringFieldEditor(
				CommonParameters.DATABASE_DRIVER_MAJOR_VERSION,
				"&Major Version:",
		 		getFieldEditorParent());
		text = databaseMajorVersionField.getTextControl(getFieldEditorParent());
		text.setEditable(false);
		text.setText(CommonParameters.get(CommonParameters.DATABASE_DRIVER_MAJOR_VERSION) != null ? CommonParameters.get(CommonParameters.DATABASE_DRIVER_MAJOR_VERSION) : DatabasePreferencePage.N_A);
		addField(databaseMajorVersionField);
		
		StringFieldEditor databaseConnectionClassNameField = new StringFieldEditor(
				CommonParameters.DATABASE_CONNECTION_CLASS_NAME,
				"&Connection Class Name:",
		 		getFieldEditorParent());
		text = databaseConnectionClassNameField.getTextControl(getFieldEditorParent());
		text.setEditable(false);
		text.setText(CommonParameters.get(CommonParameters.DATABASE_CONNECTION_CLASS_NAME) != null ? CommonParameters.get(CommonParameters.DATABASE_CONNECTION_CLASS_NAME) : DatabasePreferencePage.N_A);
		addField(databaseConnectionClassNameField);

	}

	@Override
	public void init(IWorkbench workbench) {
		super.initialize();
	}

}
