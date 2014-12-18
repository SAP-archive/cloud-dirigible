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

package com.sap.dirigible.ide.ui.rap.menu.perspectives;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class PerspectivesContribution extends ContributionItem {

	private static final long serialVersionUID = -3527023849776289881L;
	private static final String KEY_PERSPECTIVE_DESCR = "k_p_descr"; //$NON-NLS-1$

	public PerspectivesContribution() {
		super();
	}

	public PerspectivesContribution(String id) {
		super(id);
	}

	@Override
	public void fill(Menu menu, int index) {
		final String activePerspectiveId = getActivePerspectiveId();
		final IPerspectiveDescriptor[] perspectives = getPerspectives();
		for (final IPerspectiveDescriptor descriptor : perspectives) {
			final MenuItem item = new MenuItem(menu, SWT.RADIO);
			item.setData(KEY_PERSPECTIVE_DESCR, descriptor);
			item.setText(descriptor.getLabel());
			final Image image = descriptor.getImageDescriptor().createImage();
			item.setImage(image);
			if (descriptor.getId().equals(activePerspectiveId)) {
				item.setSelection(true);
			}
			item.addSelectionListener(new SelectionAdapter() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 6280318786385936319L;

				public void widgetSelected(SelectionEvent e) {
					handlePerspectiveSelected(descriptor);
				}
			});
		}
	}

	private String getActivePerspectiveId() {
		final IWorkbenchPage page = getActivePage();
		if (page == null) {
			return null;
		}
		final IPerspectiveDescriptor descriptor = page.getPerspective();
		if (descriptor == null) {
			return null;
		}
		return descriptor.getId();
	}

	private IPerspectiveDescriptor[] getPerspectives() {
		return PlatformUI.getWorkbench().getPerspectiveRegistry()
				.getPerspectives();
	}

	private void handlePerspectiveSelected(IPerspectiveDescriptor descriptor) {
		final IWorkbenchPage page = getActivePage();
		if (page != null) {
			page.setPerspective(descriptor);
		}
	}

	private IWorkbenchPage getActivePage() {
		final IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (activeWindow == null) {
			return null;
		}
		return activeWindow.getActivePage();
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

}
