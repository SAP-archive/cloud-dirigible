/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.ide.common;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepositoryPaths;

public class CommonUtils {

	public static String formatToIDEPath(String folder, String runtimePath) {
		StringBuilder path = new StringBuilder(runtimePath);
		int metaFolderIndex = runtimePath.indexOf(ICommonConstants.SEPARATOR,
				runtimePath.indexOf(ICommonConstants.SEPARATOR) + 1);
		if (metaFolderIndex != -1) {
			path.insert(metaFolderIndex, ICommonConstants.SEPARATOR + folder);
		}
		return path.toString();
	}

	public static String formatToRuntimePath(String folder, String idePath) {
		StringBuilder path = new StringBuilder(idePath);
		int indexOfWorkspace = path.indexOf(IRepositoryPaths.WORKSPACE_FOLDER_NAME);
		int indexOfSlash = path.indexOf(ICommonConstants.SEPARATOR, indexOfWorkspace);
		path.replace(0, indexOfSlash, ICommonConstants.EMPTY_STRING);
		int indexOfFolder = path.indexOf(folder);
		path.replace(indexOfFolder, indexOfFolder + folder.length() + 1,
				ICommonConstants.EMPTY_STRING);
		return path.toString();
	}
	
	public static String replaceNonAlphaNumericCharacters(String text) {
		return text.replaceAll("[^\\w]", "");
	}

	public static String getFileNameNoExtension(String fileName) {
		String result = fileName;
		if (fileName != null && fileName.indexOf('.') > 0) {
			result = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return result;
	}

}
