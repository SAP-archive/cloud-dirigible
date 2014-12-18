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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.dirigible.ide.repository.ui.view.IRefreshableView;

public class RefreshHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		refreshActivePart(event);
		return null;
	}

	public static void refreshActivePart(ExecutionEvent event) {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part == null) {
			return;
		}
		Object adapter = part.getAdapter(IRefreshableView.class);
		if (adapter instanceof IRefreshableView) {
			((IRefreshableView) adapter).refresh();
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}