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

package com.sap.dirigible.ide.template.ui.ed.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

import com.sap.dirigible.repository.ext.extensions.ExtensionDefinition;
import com.sap.dirigible.repository.ext.extensions.ExtensionManager;
import com.sap.dirigible.repository.ext.extensions.ExtensionPointDefinition;
import com.sap.dirigible.repository.logging.Logger;

public class ExtensionsContentProvider implements ITreeContentProvider {
	private static final long serialVersionUID = -1334098123002372113L;

	private static final Logger logger = Logger.getLogger(ExtensionsContentProvider.class);

	private ExtensionManager extensionManager;
	private Shell shell;

	public ExtensionsContentProvider(ExtensionManager extensionManager, Shell shell) {
		this.extensionManager = extensionManager;
		this.shell = shell;
	}

	@Override
	public void dispose() {
		//
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		//
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<ExtensionPointDefinition> elements = new ArrayList<ExtensionPointDefinition>();
		try {
			for (String nextExtensionPoint : extensionManager.getExtensionPoints()) {
				elements.add(extensionManager.getExtensionPoint(nextExtensionPoint));
			}

		} catch (Exception e) {
			logger.error(ExtensionsView.EXTENSIONS_ERROR, e);
			MessageDialog.openError(shell, ExtensionsView.EXTENSIONS_ERROR, e.getMessage());
		}
		return elements.toArray(new ExtensionPointDefinition[elements.size()]);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		List<ExtensionDefinition> children = new ArrayList<ExtensionDefinition>();
		if (parentElement instanceof ExtensionPointDefinition) {
			try {
				ExtensionPointDefinition extensionPoint = (ExtensionPointDefinition) parentElement;
				String[] extensions = extensionManager.getExtensions(extensionPoint.getLocation());
				for (String nextExtension : extensions) {
					children.add(extensionManager.getExtension(nextExtension,
							extensionPoint.getLocation()));
				}
			} catch (Exception e) {
				logger.error(ExtensionsView.EXTENSIONS_ERROR, e);
				MessageDialog.openError(shell, ExtensionsView.EXTENSIONS_ERROR, e.getMessage());
			}
		}
		return children.toArray(new ExtensionDefinition[children.size()]);
	}

	@Override
	public Object getParent(Object element) {
		ExtensionPointDefinition parent = null;
		if (element instanceof ExtensionDefinition) {
			try {
				parent = extensionManager.getExtensionPoint(((ExtensionDefinition) element)
						.getExtensionPoint());
			} catch (Exception e) {
				logger.error(ExtensionsView.EXTENSIONS_ERROR, e);
				MessageDialog.openError(shell, ExtensionsView.EXTENSIONS_ERROR, e.getMessage());
			}
		}
		return parent;

	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof ExtensionPointDefinition;
	}

}
