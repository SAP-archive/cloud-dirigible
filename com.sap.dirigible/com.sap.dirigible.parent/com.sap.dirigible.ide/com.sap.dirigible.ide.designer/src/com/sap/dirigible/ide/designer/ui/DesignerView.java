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

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.sap.dirigible.ide.ui.widget.extbrowser.ExtendedBrowser;

public class DesignerView extends ViewPart {

	private static final String REFRESH = Messages.DesignerView_REFRESH;

	private static final String WYSIWYG_URL = "/wysiwyg/wysiwyg.html"; //$NON-NLS-1$
	
	private static final URL DIRIGIBLE_REFRESH_ICON_URL = DesignerView.class
			.getResource("/resources/refresh.png"); //$NON-NLS-1$

	private final ISelectionListener selectionListener = new SelectionListenerImpl();

	private ExtendedBrowser browser = null;

	private final ResourceManager resourceManager;

	public DesignerView() {
		super();
		resourceManager = new LocalResourceManager(
				JFaceResources.getResources());
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		attachSelectionListener(site);
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);

		final Composite holder = new Composite(parent, SWT.NONE);
		holder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		holder.setLayout(new GridLayout(3, false));

		Button goButton = new Button(holder, SWT.PUSH);
		goButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		goButton.setToolTipText(REFRESH);
		goButton.setImage(createImage(DIRIGIBLE_REFRESH_ICON_URL));
		goButton.addSelectionListener(new SelectionAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5958451675316067391L;

			public void widgetSelected(SelectionEvent e) {
				browser.refresh();
			}
		});

		browser = new ExtendedBrowser(parent, SWT.NONE);
		browser.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		browser.setUrl(RWT.getRequest().getContextPath() + WYSIWYG_URL);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	@Override
	public void dispose() {
		detachSelectionListener(getSite());
		browser = null;
		super.dispose();
	}

	private void attachSelectionListener(IWorkbenchPartSite site) {
		if (site == null) {
			return;
		}
		final ISelectionService selectionService = getSelectionService(site);
		if (selectionService != null) {
			selectionService.addSelectionListener(selectionListener);
		}
	}

	private void detachSelectionListener(IWorkbenchPartSite site) {
		if (site == null) {
			return;
		}
		final ISelectionService selectionService = getSelectionService(site);
		if (selectionService != null) {
			selectionService.removeSelectionListener(selectionListener);
		}
	}

	private ISelectionService getSelectionService(IWorkbenchPartSite site) {
		final IWorkbenchWindow window = site.getWorkbenchWindow();
		if (window == null) {
			return null;
		}
		return window.getSelectionService();
	}

	private void handleElementSelected(Object element) {
		// if (element instanceof IAdaptable) {
		// final IAdaptable adaptable = (IAdaptable)element;
		// final IDocumentable documentable =
		// (IDocumentable)adaptable.getAdapter(IDocumentable.class);
		// if (documentable != null) {
		// openDocumentation(documentable);
		// }
		// } else if (element instanceof IDocumentable) {
		// openDocumentation((IDocumentable)element);
		// } else {
		// final IAdapterManager manager = Platform.getAdapterManager();
		// final IDocumentable documentable =
		// (IDocumentable)manager.loadAdapter(element,
		// IDocumentable.class.getName());
		// if (documentable != null) {
		// openDocumentation(documentable);
		// }
		// }
	}

	// private void openDocumentation(IDocumentable documentable) {
	// if (documentable.getDocumentationUrl() != null) {
	// browser.setUrl(documentable.getDocumentationUrl());
	// } else if (documentable.getDocumentationHtml() != null) {
	// browser.setContent(documentable.getDocumentationHtml());
	// } else {
	// browser.setUrl(null);
	// }
	//
	// // XXX: Create integration adapters for the following types
	// // if (element instanceof IServiceDefinitionItem) {
	// // IServiceDefinitionItem serviceDefinitionItem =
	// (IServiceDefinitionItem) element;
	// // pageUrl = serviceDefinitionItem.getDocumentationUrl();
	// // } else if (element instanceof IServiceEndpointItem) {
	// // IServiceEndpointItem serviceEndpointItem = (IServiceEndpointItem)
	// element;
	// // pageUrl =
	// serviceEndpointItem.getServiceDefinitionItem().getDocumentationUrl();
	// // }
	// }

	private class SelectionListenerImpl implements ISelectionListener {

		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				selectionChanged((IStructuredSelection) selection);
			}
		}

		private void selectionChanged(IStructuredSelection selection) {
			Object element = selection.getFirstElement();
			if (element != null) {
				handleElementSelected(element);
			}
		}

	}

	private Image createImage(URL imageURL) {
		// TODO - cached?
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		return resourceManager.createImage(imageDescriptor);
	}

}
