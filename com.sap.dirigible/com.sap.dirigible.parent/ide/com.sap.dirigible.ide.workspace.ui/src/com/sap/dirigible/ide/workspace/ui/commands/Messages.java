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

package com.sap.dirigible.ide.workspace.ui.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.workspace.ui.commands.messages"; //$NON-NLS-1$
	public static String DeleteHandler_ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEM;
	public static String DeleteHandler_ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEMS_D;
	public static String DeleteHandler_CONFIRM_DELETE;
	public static String DeleteHandler_DELETE_ERROR;
	public static String DeleteHandler_SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_DELETED;
	public static String OpenHandler_BINARY_FILES_ARE_NOT_SUPPORTED;
	public static String OpenHandler_COULD_NOT_OPEN_ONE_OR_MORE_FILES;
	public static String OpenHandler_OPEN_FAILURE;
	public static String OpenHandler_OPEN_FAILURE2;
	public static String OpenHandler_VIEW_THEM_VIA_REGISTRY_AFTER_ACTIVATION;
	public static String PasteHandler_PASTE_ERROR;
	public static String PasteHandler_SELECT_TARGET_FOLDER;
	public static String PasteHandler_SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_PASTED;
	public static String RenameCommandHandler_RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_A_SINGLE_RESOURCE_AT_A_TIME;
	public static String RenameCommandHandler_RENAME_OPERATION_CANCELLED_CAN_ONLY_RENAME_INSTANCES_OF_I_RESOURCE;
	public static String UploadDataHandler_CANNOT_CLOSE_INPUT_STREAM_TO_AN_UPLOADED_FILE;
	public static String UploadDataHandler_CANNOT_STORE_DATA_FROM;
	public static String UploadDataHandler_NO_FILES_SPECIFIED;
	public static String UploadDataHandler_REASON;
	public static String UploadDataHandler_SUCCESSFULLY_IMPORTED_FILE;
	public static String UploadDataHandler_UPLOAD_DATA_RESULT;
	public static String UploadDataHandler_UPLOADING;
	public static String UploadDataWizard_WINDOW_TITLE;
	public static String UploadDataWizardPage_BROWSE;
	public static String UploadDataWizardPage_FILES;
	public static String UploadDataWizardPage_INFO_MSG;
	public static String UploadDataWizardPage_PAGE_DESCRIPTION;
	public static String UploadDataWizardPage_PAGE_TITLE;
	public static String UploadDataWizardPage_UPLOAD_DATA;
	public static String UploadHandler_CANNOT_CLOSE_INPUT_STREAM_TO_AN_UPLOADED_FILE;
	public static String UploadHandler_CANNOT_DELETE_FILE;
	public static String UploadHandler_CANNOT_SAVE_UPLOADED_FILE;
	public static String UploadHandler_CANNOT_UPLOAD;
	public static String UploadHandler_DO_YOU_WANT_TO_OVERRIDE_IT;
	public static String UploadHandler_FILE_S_ALREADY_EXISTS;
	public static String UploadHandler_REASON;
	public static String UploadHandler_UPLOAD_ERROR;
	public static String UploadHandler_UPLOAD_FILE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
