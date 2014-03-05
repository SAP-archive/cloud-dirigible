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

package com.sap.dirigible.ide.workspace.ui.commands;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AbstractClipboardHandler extends AbstractHandler {

	public static final String CUT = "WORKSPACE_CUT"; //$NON-NLS-1$
	public static final String COPY = "WORKSPACE_COPY"; //$NON-NLS-1$
	public static final String PASTE = "WORKSPACE_PASTE"; //$NON-NLS-1$

	// TODO: Must determine when this command is enabled or not.

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			execute(event, (IStructuredSelection) selection);
		}
		return null;
	}

	public void execute(ExecutionEvent event, IStructuredSelection selection) {
		Comparator<IResource> comparator = new ResourceComparator();
		SortedSet<IResource> resources = new TreeSet<IResource>(comparator);
		for (Object element : selection.toArray()) {
			if (element instanceof IResource) {
				resources.add((IResource) element);
			}
		}
		execute(event, resources);
	}

	protected abstract void execute(ExecutionEvent event,
			SortedSet<IResource> resources);

	private class ResourceComparator implements Comparator<IResource> {

		@Override
		public int compare(IResource o1, IResource o2) {
			int segmentCount1 = o1.getFullPath().segmentCount();
			int segmentCount2 = o2.getFullPath().segmentCount();
			if (segmentCount1 == segmentCount2) {
				String path1 = o1.getFullPath().toString();
				String path2 = o2.getFullPath().toString();
				return path2.compareTo(path1);
			} else {
				return segmentCount2 - segmentCount1;
			}
		}

	}

}
