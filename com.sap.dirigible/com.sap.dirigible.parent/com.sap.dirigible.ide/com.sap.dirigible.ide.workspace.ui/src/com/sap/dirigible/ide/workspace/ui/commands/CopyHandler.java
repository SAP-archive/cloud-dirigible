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

import java.util.SortedSet;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IResource;

import com.sap.dirigible.ide.repository.ui.command.Clipboard;

public class CopyHandler extends AbstractClipboardHandler {

	protected void execute(ExecutionEvent event, SortedSet<IResource> resources) {
		if (resources.size() == 0) {
			return;
		}

		Clipboard clipboard = Clipboard.getInstance();
		clipboard.clear();
		clipboard.setCommand(COPY);

		for (IResource resource : resources) {
			clipboard.add(resource);
		}
	}

}
