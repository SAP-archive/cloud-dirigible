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

package com.sap.dirigible.ide.common.status;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class StatusLineManagerUtil {

	public static final String ARTIFACT_HAS_BEEN_CREATED = Messages.StatusLineManagerUtil_ARTIFACT_HAS_BEEN_CREATED;
	public static final String ARTIFACT_HAS_BEEN_ACTIVATED = Messages.StatusLineManagerUtil_ARTIFACT_HAS_BEEN_ACTIVATED;
	public static final String ARTIFACT_HAS_BEEN_PUBLISHED = Messages.StatusLineManagerUtil_ARTIFACT_HAS_BEEN_PUBLISHED;

	public static IStatusLineManager getDefaultStatusLineManager() {
		IStatusLineManager statusLineManager;
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getPartService().getActivePart();
		if (workbenchPart instanceof IViewPart) {
			statusLineManager = ((IViewPart) workbenchPart).getViewSite()
					.getActionBars().getStatusLineManager();
		} else if (workbenchPart instanceof IEditorPart) {
			statusLineManager = ((IEditorPart) workbenchPart).getEditorSite()
					.getActionBars().getStatusLineManager();
		} else {
			statusLineManager = new StatusLineManager();
		}
		return statusLineManager;
	}

	public static void setInfoMessage(String message) {
		getDefaultStatusLineManager().removeAll();
		getDefaultStatusLineManager().setMessage(
				JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO), message);
	}

	public static void setErrorMessage(String message) {
		getDefaultStatusLineManager().removeAll();
		getDefaultStatusLineManager().setErrorMessage(
				JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_ERROR), message);
	}

	public static void setWarningMessage(String message) {
		getDefaultStatusLineManager().removeAll();
		getDefaultStatusLineManager().setMessage(
				JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_WARNING),
				message);
	}

	/*
	 * Prevent instantiation.
	 */
	private StatusLineManagerUtil() {
		super();
	}
}
