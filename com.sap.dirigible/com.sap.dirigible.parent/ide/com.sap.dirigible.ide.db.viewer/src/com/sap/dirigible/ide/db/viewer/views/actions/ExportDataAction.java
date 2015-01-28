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

package com.sap.dirigible.ide.db.viewer.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Event;

import com.sap.dirigible.ide.db.export.DataDownloadDialog;
import com.sap.dirigible.ide.db.export.DataExportServiceHandler;
import com.sap.dirigible.ide.db.viewer.views.TableDefinition;
import com.sap.dirigible.ide.db.viewer.views.TreeObject;

public class ExportDataAction extends Action {

	private static final long serialVersionUID = 1L;
	private static final String EXPORT_DATA = Messages.ExportDataAction_EXPORT_DATA;
	private static final String EXPORT_DATA_TOOLTIP = Messages.ExportDataAction_EXPORT_DATA_AS_DSV_FILE;
	
	private TreeViewer viewer;
	private String tableName;
	
	public ExportDataAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(EXPORT_DATA);
		setToolTipText(EXPORT_DATA_TOOLTIP);
	}
	
	public void runWithEvent(Event event) {
		ISelection selection = viewer.getSelection();
		Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
		
		if (TreeObject.class.isInstance(selectedElement)) {
			
			if (((TreeObject) selectedElement).getTableDefinition() != null) {
				
				TableDefinition tableDefinition = ((TreeObject) selectedElement).getTableDefinition();
				
				String tablePath = tableDefinition.getFqn();
				tableName = tablePath.substring(tablePath.lastIndexOf(".")+2, tablePath	.length()-1);
				
				DataDownloadDialog dataDownloadDialog = new DataDownloadDialog(
						event.display.getActiveShell());
				dataDownloadDialog.setURL(DataExportServiceHandler.getUrl(tableName));
				dataDownloadDialog.open();
			}
		}
	}
}
