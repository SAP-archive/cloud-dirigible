package com.sap.dirigible.ide.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sap.dirigible.repository.ext.conf.IConfigurationStore;

public class RepositoryGlobalPreferenceStore extends
		AbstractRepositoryPreferenceStore {

	private static final long serialVersionUID = 4602966805779348296L;


	public RepositoryGlobalPreferenceStore(String path, String name) {
		super(path, name);
	}

	@Override
	protected byte[] loadSettings(IConfigurationStore configurationStorage)
			throws IOException {
		byte[] bytes = configurationStorage.getGlobalSettingsAsBytes(getPath(), getName());
		return bytes;
	}


	@Override
	protected void saveSettingd(ByteArrayOutputStream baos) throws IOException {
		getConfigurationStore().setGlobalSettingsAsBytes(getPath(), getName(), baos.toByteArray());
	}


}
