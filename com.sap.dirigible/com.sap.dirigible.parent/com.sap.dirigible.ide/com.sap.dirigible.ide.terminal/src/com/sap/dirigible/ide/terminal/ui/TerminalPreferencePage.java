package com.sap.dirigible.ide.terminal.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.ext.conf.ConfigurationStore;
import com.sap.dirigible.repository.ext.conf.IConfigurationStore;

public class TerminalPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final String CONF_NAME_TERMINAL = "terminal";
	private static final String CONF_PATH_IDE = "/ide";
	private static final String LIMIT_TIMEOUT = "limitTimeout";
	private static final String LIMIT_ENABLED = "limitEnabled";
	private static final long serialVersionUID = -877187045002896492L;
	
	private PreferenceStore preferenceStore;

	private BooleanFieldEditor enableTimeLimitField;
	private IntegerFieldEditor limitTimeoutField;
	
	public static PreferenceStore getTerminalPreferenceStore() {
		try {
			PreferenceStore preferenceStore = new PreferenceStore();
			byte[] bytes = getConfigurationSettings();
			preferenceStore.load(new ByteArrayInputStream(bytes));
			return preferenceStore;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public TerminalPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		this.preferenceStore = new PreferenceStore();
		try {
			byte[] bytes = getConfigurationSettings();
			preferenceStore.load(new ByteArrayInputStream(bytes));
			setPreferenceStore(preferenceStore);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void createFieldEditors() {
		
		try {
			byte[] bytes = getConfigurationSettings();
			preferenceStore.load(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		enableTimeLimitField = new BooleanFieldEditor(
				LIMIT_ENABLED,
				"&Enable Time Limit",
		 		getFieldEditorParent());
			enableTimeLimitField.setPreferenceStore(getPreferenceStore());
			enableTimeLimitField.load();
		addField(enableTimeLimitField);
	
		limitTimeoutField = new IntegerFieldEditor(
				LIMIT_TIMEOUT,
				"&Limit Timeout:",
		 		getFieldEditorParent());
			limitTimeoutField.setPreferenceStore(getPreferenceStore());
			limitTimeoutField.load();
		addField(limitTimeoutField);

	}

	private static byte[] getConfigurationSettings() throws IOException {
		IConfigurationStore configurationStorage = getConfigurationStore();
		
		byte[] bytes = configurationStorage.getGlobalSettingsAsBytes(CONF_PATH_IDE, CONF_NAME_TERMINAL);
		if (bytes.length == 0) {
			Properties properties = new Properties(); 
			properties.put(LIMIT_ENABLED, Boolean.TRUE.toString());
			properties.put(LIMIT_TIMEOUT, "30");
			configurationStorage.setGlobalSettings(CONF_PATH_IDE, CONF_NAME_TERMINAL, properties);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			properties.store(baos, "Default Settings Created");
			bytes = baos.toByteArray();
		}
		return bytes;
	}

	private static IConfigurationStore getConfigurationStore() {
		IRepository repository = RepositoryFacade.getInstance().getRepository();
		IConfigurationStore configurationStorage = 
				new ConfigurationStore(repository);
		return configurationStorage;
	}

	@Override
	public void init(IWorkbench workbench) {
		super.initialize();
	}
	
	private void storeValues() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			enableTimeLimitField.store();
			limitTimeoutField.store();
			preferenceStore.save(baos, "Chanaged by " + CommonParameters.getUserName());
			getConfigurationStore().setGlobalSettingsAsBytes(CONF_PATH_IDE, CONF_NAME_TERMINAL, baos.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void performApply() {
		super.performApply();
		storeValues();
	}

	@Override
	public boolean performOk() {
		storeValues();
		return super.performOk();
	}
	

	// TODO - Defaults?
	
}
