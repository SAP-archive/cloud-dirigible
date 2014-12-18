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
import java.util.Iterator;
import java.util.SortedSet;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;

import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;

public class PasteHandler extends AbstractClipboardHandler {

	private static final String SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_PASTED = Messages.PasteHandler_SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_PASTED;
	private static final String PASTE_ERROR = Messages.PasteHandler_PASTE_ERROR;

	protected void execute(ExecutionEvent event, SortedSet<IEntity> resources) {
		if (resources.size() == 0) {
			return;
		}

		IRepository repository = RepositoryFacade.getInstance().getRepository();

		String targetReposiotryPath = resources.first().getPath().toString();

		Clipboard clipboard = Clipboard.getInstance();

		String command = clipboard.getCommand();

		Throwable throwable = null;
		if (CUT.equals(command) || COPY.equals(command)) {

			for (Iterator<?> iterator = clipboard.iterator(); iterator.hasNext();) {
				IEntity resource = (IEntity) iterator.next();
				String sourceRepositoryPath = resource.getPath().toString();
				try {
					byte[] data = repository.exportZip(sourceRepositoryPath,
							true);
					repository.importZip(data, targetReposiotryPath);
				} catch (IOException e) {
					if (throwable == null) {
						throwable = e;
					}
				}
				if (CUT.equals(command)) {
					try {
						resource.delete();
					} catch (IOException e) {
						if (throwable == null) {
							throwable = e;
						}
					}
				}
			}
		}

		if (throwable != null) {
			MessageDialog.openWarning(null, PASTE_ERROR,
					SOME_OR_ALL_OF_THE_FILES_COULD_NOT_BE_PASTED);
		}

		RefreshHandler.refreshActivePart(event);

	}

}
