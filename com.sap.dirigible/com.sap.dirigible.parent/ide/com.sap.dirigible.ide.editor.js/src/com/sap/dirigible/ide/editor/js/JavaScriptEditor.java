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

package com.sap.dirigible.ide.editor.js;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

import com.sap.dirigible.ide.common.CommonUtils;
import com.sap.dirigible.ide.debug.model.DebugModel;
import com.sap.dirigible.ide.debug.model.DebugModelFacade;
import com.sap.dirigible.ide.editor.text.editor.ContentProviderException;
import com.sap.dirigible.ide.editor.text.editor.EditorMode;
import com.sap.dirigible.ide.editor.text.editor.TextEditor;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.logging.Logger;
import com.sap.rap.ui.shared.editor.SourceFileEditorInput;

public class JavaScriptEditor extends TextEditor {

	private static final String ERROR = Messages.JavaScriptEditor_ERROR;

	private static final String CANNOT_LOAD_DOCUMENT = Messages.JavaScriptEditor_CANNOT_LOAD_DOCUMENT;

	private static final Logger logger = Logger.getLogger(JavaScriptEditor.class);

	private EditorWidget text = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout());

		final JavaScriptEditor textEditor = this;
		final IEditorInput input = getEditorInput();
		text = new EditorWidget(parent, true);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		String fileName = null;
		try {
			if (input instanceof SourceFileEditorInput) {
				SourceFileEditorInput sfei = (SourceFileEditorInput) input;
				fileName = sfei.getName();
			}
			final String content = getContentProvider(input).getContent(input);
			if (input instanceof SourceFileEditorInput) {
				SourceFileEditorInput sfei = (SourceFileEditorInput) input;
				fileName = sfei.getName();
				text.setText(content, getMode(), sfei.isReadOnly(), sfei.isBreakpointsEnabled(),
						sfei.getRow());
			} else {
				text.setText(content, getMode(), false, false, 0);
			}

		} catch (final ContentProviderException e) {
			logger.error(CANNOT_LOAD_DOCUMENT, e);
			if (fileName != null) {
				MessageDialog.openError(null, ERROR, CANNOT_LOAD_DOCUMENT + " " + fileName);
			} else {
				MessageDialog.openError(null, ERROR, CANNOT_LOAD_DOCUMENT);
			}
		}

		text.setListener(new IEditorWidgetListener() {

			@Override
			public void dirtyStateChanged(final boolean dirty) {
				textEditor.setDirty(dirty);
				if (dirty) {
					// IEditorInput editorInput = getEditorInput();
					// if (editorInput instanceof FileEditorInput) {
					// String path = ((FileEditorInput)
					// editorInput).getPath().toString();
					// String formatedPath = formatServicePath(path);
					// DebugModel.getInstance(null).clearAllBreakpoints(formatedPath);
					// }
				}
			}

			@Override
			public void save() {
				doSave(null);
			}

			@Override
			public void setBreakpoint(final int row) {
				IEditorInput editorInput = getEditorInput();
				if (editorInput instanceof FileEditorInput) {
					DebugModel debugModel = DebugModelFacade.getActiveDebugModel();
					if (debugModel != null) {
						debugModel.setBreakpoint(CommonUtils.formatToRuntimePath(
								ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES,
								((FileEditorInput) editorInput).getPath().toString()), row);
					}
				}
			}

			@Override
			public void clearBreakpoint(final int row) {
				IEditorInput editorInput = getEditorInput();
				if (editorInput instanceof FileEditorInput) {
					DebugModel debugModel = DebugModelFacade.getActiveDebugModel();
					if (debugModel != null) {
						String formatToRuntimePath = CommonUtils.formatToRuntimePath(
								ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES,
								((FileEditorInput) editorInput).getPath().toString());
						debugModel.clearBreakpoint(formatToRuntimePath, row);
					}
				}
			}
		});
	}

	private EditorMode getMode() {
		final IEditorInput input = getEditorInput();
		final String name = input.getName();
		final String ext = name.substring(name.lastIndexOf('.') + 1);

		return EditorMode.getByExtension(ext);
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
	protected void setDirty(final boolean b) {
		super.setDirty(b);
		if (text != null) {
			text.setDirty(b);
		}
	}

	public void setDebugRow(final int row) {
		if (text != null) {
			text.setDebugRow(row);
		}
	}

}
