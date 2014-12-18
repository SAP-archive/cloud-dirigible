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

package com.sap.dirigible.ide.workspace.impl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Control;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;

public class DefaultProgressMonitor implements IProgressMonitor {

	private final IStatusLineManager statusLineManager;

	public DefaultProgressMonitor() {
		this.statusLineManager = StatusLineManagerUtil
				.getDefaultStatusLineManager();
	}

	@Override
	public void beginTask(String name, int totalWork) {
		this.statusLineManager.setCancelEnabled(true);
		delegate().beginTask(name, totalWork);
		this.statusLineManager.update(true);
		if (statusLineManager instanceof SubStatusLineManager) {
			((SubStatusLineManager) statusLineManager).getParent().update(true);
		}
	}

	private IProgressMonitor delegate() {
		return statusLineManager.getProgressMonitor();
	}

	@Override
	public void done() {
		delegate().done();
		this.statusLineManager.update(true);
	}

	@Override
	public void internalWorked(double work) {
		delegate().internalWorked(work);
	}

	@Override
	public boolean isCanceled() {
		return delegate().isCanceled();
	}

	@Override
	public void setCanceled(boolean value) {
		delegate().setCanceled(value);
	}

	@Override
	public void setTaskName(String name) {
		delegate().setTaskName(name);
	}

	@Override
	public void subTask(String name) {
		delegate().subTask(name);
	}

	@Override
	public void worked(int work) {
		delegate().worked(work);
	}

	public Control getControl() {
		if (statusLineManager instanceof StatusLineManager)
			return ((StatusLineManager) statusLineManager).getControl();
		return null;
	}

	public void setMessage(final String message) {
		this.statusLineManager.setMessage(message);

	}

	/**
	 * Sets the message, along with a image corresponding to error/warning/info
	 * severity. If another argument is provided in severity, the method
	 * returns.
	 */
	public void setMessage(final String message, final int severity) {
		String imageCode = null;
		switch (severity) {
		case (IStatus.INFO):
			imageCode = Dialog.DLG_IMG_MESSAGE_INFO;
			break;
		case (IStatus.WARNING):
			imageCode = Dialog.DLG_IMG_MESSAGE_WARNING;
			break;
		case (IStatus.ERROR):
			imageCode = Dialog.DLG_IMG_MESSAGE_ERROR;
			break;
		}
		if (imageCode == null) {
			return;
		} else {
			this.statusLineManager.setMessage(
					JFaceResources.getImage(imageCode), message);
		}
	}

}
