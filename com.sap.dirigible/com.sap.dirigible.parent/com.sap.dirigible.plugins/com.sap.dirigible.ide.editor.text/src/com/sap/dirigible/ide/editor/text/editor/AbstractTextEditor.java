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

package com.sap.dirigible.ide.editor.text.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.EditorPart;

import com.sap.dirigible.ide.logging.Logger;

public abstract class AbstractTextEditor extends EditorPart {

	private static final String COLON = ": "; //$NON-NLS-1$

	private static final String SAVE_ERROR = Messages.AbstractTextEditor_SAVE_ERROR;

	private static final String CANNOT_SAVE_DOCUMENT = Messages.AbstractTextEditor_CANNOT_SAVE_DOCUMENT;

	private static final Logger logger = Logger
			.getLogger(AbstractTextEditor.class);

	private boolean isDirty;

	@Override
	public void doSave(IProgressMonitor monitor) {
		IEditorInput input = getEditorInput();
		String contents = getEditorContents();
		IContentProvider contentProvider = getContentProvider(input);
		if (contentProvider != null && contents != null) {
			try {
				contentProvider.save(monitor, input, contents, true);
				setDirty(false);
			} catch (ContentProviderException e) {
				logger.error(CANNOT_SAVE_DOCUMENT, e);
				MessageDialog.openError(null, SAVE_ERROR, CANNOT_SAVE_DOCUMENT
						+ COLON + e.getMessage());
			}
		}

	}

	protected void setDirty(boolean b) {
		this.isDirty = b;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public void setFocus() {
		Control control = getEditorControl();
		if (control != null) {
			control.setFocus();
		}
	}

	protected abstract String getEditorContents();

	protected abstract Control getEditorControl();

	protected IContentProvider getContentProvider(IEditorInput input) {
		return ContentProviderFactory.getInstance().getContentProvider(input);
	}
}
