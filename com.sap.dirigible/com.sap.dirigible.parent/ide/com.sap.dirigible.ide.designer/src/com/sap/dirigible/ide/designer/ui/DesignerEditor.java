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

package com.sap.dirigible.ide.designer.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;

import com.sap.dirigible.ide.editor.text.editor.ContentProviderException;
import com.sap.dirigible.ide.editor.text.editor.TextEditor;
import com.sap.dirigible.repository.logging.Logger;

public class DesignerEditor extends TextEditor {

	private static final String EMPTY_HTML = "<!DOCTYPE html><html><head></head><body></body></html>"; //$NON-NLS-1$

	private static final String ERROR = Messages.DesignerEditor_Error;

	private static final String CANNOT_LOAD_DOCUMENT = Messages.DesignerEditor_Cannot_load_document;

	private static final Logger logger = Logger
			.getLogger(DesignerEditor.class);

	private DesignerEditorWidget text = null;

	/**
	 * {@inheritDoc}
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		final DesignerEditor designerEditor = this;
		IEditorInput input = getEditorInput();
		text = new DesignerEditorWidget(parent);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		try {
			String content = getContentProvider(input).getContent(input);
			if (content == null
					|| "".equals(content)) {
				content = EMPTY_HTML;
			}
			text.setText(content);
		} catch (ContentProviderException e) {
			logger.error(CANNOT_LOAD_DOCUMENT, e);
			MessageDialog.openError(null, ERROR, CANNOT_LOAD_DOCUMENT);
		}

		text.setListener(new IDesignerEditorWidgetListener() {

			@Override
			public void dirtyStateChanged(boolean dirty) {
				designerEditor.setDirty(dirty);
			}

			@Override
			public void save() {
				doSave(null);
			}
		});
	}

	@Override
	protected String getEditorContents() {
		return text != null ? text.getText() : null;
	}

	@Override
	protected Control getEditorControl() {
		return text;
	}

	@Override
	protected void setDirty(boolean b) {
		super.setDirty(b);
		if (text != null) {
			text.setDirty(b);
		}
	}

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}
}
