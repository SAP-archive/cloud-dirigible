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

package com.sap.dirigible.ide.db.viewer.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

public class DbTableMetadataEditor extends MultiPageEditorPart {

	private static final String TABLE_DETAILS = Messages.DbTableMetadataEditor_TABLE_DETAILS;

	private static final long serialVersionUID = -1881524156909611914L;

	private TableDetailsEditorPage mainEditorPage;

	public DbTableMetadataEditor() {
		super();
	}

	public void doSave(final IProgressMonitor monitor) {
		mainEditorPage.setDirty(false);
	}

	public void doSaveAs() {
		//
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(final Class adapter) {
		Object result = super.getAdapter(adapter);
		return result;
	}

	public void init(final IEditorSite site, final IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(((DbEditorInput) input).getTableName());
	}

	protected void createPages() {
		mainEditorPage = new TableDetailsEditorPage();
		int index;
		try {
			index = addPage(mainEditorPage, getEditorInput());
			setPageText(index, TABLE_DETAILS);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
