package com.sap.dirigible.ide.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.repository.ext.conf.IConfigurationStore;

public class RepositoryUserPreferenceStore extends
		AbstractRepositoryPreferenceStore {

	private static final long serialVersionUID = 4602966805779348296L;


	public RepositoryUserPreferenceStore(String path, String name) {
		super(path, name);
	}

	@Override
	protected byte[] loadSettings(IConfigurationStore configurationStorage)
			throws IOException {
		byte[] bytes = configurationStorage.getUserSettingsAsBytes(getPath(), getName(), CommonParameters.getUserName());
		return bytes;
	}


	@Override
	protected void saveSettingd(ByteArrayOutputStream baos) throws IOException {
		getConfigurationStore().setUserSettingsAsBytes(getPath(), getName(), baos.toByteArray(), CommonParameters.getUserName());
	}


}
