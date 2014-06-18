package com.sap.dirigible.ide.common;

public class CommonUtils {

	public static String formatToIDEPath(String folder, String runtimePath) {
		StringBuilder path = new StringBuilder(runtimePath);
		int metaFolderIndex = runtimePath.indexOf(CommonParameters.SEPARATOR,
				runtimePath.indexOf(CommonParameters.SEPARATOR) + 1);
		if (metaFolderIndex != -1) {
			path.insert(metaFolderIndex, CommonParameters.SEPARATOR + folder);
		}
		return path.toString();
	}

	public static String formatToRuntimePath(String folder, String idePath) {
		StringBuilder path = new StringBuilder(idePath);
		int indexOfWorkspace = path.indexOf(CommonParameters.WORKSPACE_FOLDER_NAME);
		int indexOfSlash = path.indexOf(CommonParameters.SEPARATOR, indexOfWorkspace);
		path.replace(0, indexOfSlash, CommonParameters.EMPTY_STRING);
		int indexOfFolder = path.indexOf(folder);
		path.replace(indexOfFolder, indexOfFolder + folder.length() + 1,
				CommonParameters.EMPTY_STRING);
		return path.toString();
	}

}
