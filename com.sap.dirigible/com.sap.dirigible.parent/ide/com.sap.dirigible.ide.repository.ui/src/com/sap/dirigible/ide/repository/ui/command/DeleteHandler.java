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

package com.sap.dirigible.ide.repository.ui.command;

import java.io.IOException;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.repository.api.IEntity;

public class DeleteHandler extends AbstractHandler {

	// TODO: Must determine when this command is enabled or not.

	private static final String CONFIRM_DELETE = Messages.DeleteHandler_CONFIRM_DELETE;
	private static final String ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEMS_D = Messages.DeleteHandler_ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEMS_D;
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private static final String ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEM = Messages.DeleteHandler_ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEM;
	private static final String SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_DELETED = Messages.DeleteHandler_SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_DELETED;
	private static final String DELETE_ERROR = Messages.DeleteHandler_DELETE_ERROR;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			execute((IStructuredSelection) selection);
			RefreshHandler.refreshActivePart(event);
		}
		return null;
	}

	public void execute(IStructuredSelection selection) {
		Comparator<IEntity> comparator = new ResourceComparator();
		SortedSet<IEntity> resources = new TreeSet<IEntity>(comparator);
		for (Object element : selection.toArray()) {
			if (element instanceof IEntity) {
				resources.add((IEntity) element);
			}
		}
		execute(resources);
	}

	/*
	 * We require that all resources are sorted in such a way that we first
	 * delete the leaf-most elements and then handle elements closer to the
	 * root.
	 */
	private void execute(SortedSet<IEntity> resources) {
		if (resources.size() == 0) {
			return;
		}
		if (!confirmDelete(resources.size())) {
			return;
		}
		Throwable throwable = null;
		for (IEntity resource : resources) {
			try {
				resource.delete();
			} catch (IOException ex) {
				if (throwable == null) {
					throwable = ex;
				}
			}
		}
		if (throwable != null) {
			MessageDialog.openWarning(null, DELETE_ERROR,
					SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_DELETED);
		}
	}

	private static boolean confirmDelete(int count) {
		String message = EMPTY_STRING;
		if (count == 1) {
			message = ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEM;
		} else {
			message = String.format(
					ARE_YOU_SURE_YOU_WANT_TO_DELETE_SELECTED_ITEMS_D, count);
		}
		return MessageDialog.openConfirm(null, CONFIRM_DELETE, message);
	}

	private class ResourceComparator implements Comparator<IEntity> {

		@Override
		public int compare(IEntity o1, IEntity o2) {
			String path1 = o1.getPath().toString();
			String path2 = o2.getPath().toString();
			return path2.compareTo(path1);
		}

	}

}
