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

package com.sap.dirigible.ide.help.ui;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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

public abstract class AbstractHelpView extends ViewPart {

	private final ISelectionListener selectionListener = new SelectionListenerImpl();

	private ExtendedBrowser browser = null;

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

		browser = new ExtendedBrowser(parent, SWT.NONE);
		browser.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		browser.setUrl(getSiteUrl());
	}

	protected abstract String getSiteUrl();

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
		//
	}

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


}
