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

package com.sap.dirigible.ide.jgit.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.sap.dirigible.ide.logging.Logger;

public class CommandHandlerUtils {
	private static final String UNKNOWN_SELECTION_TYPE = Messages.CommandHandlerUtils_UNKNOWN_SELECTION_TYPE;

	public static IProject[] getProjects(ISelection selection, Logger logger) {
		if (!(selection instanceof IStructuredSelection)) {
			logger.error(UNKNOWN_SELECTION_TYPE);
			return new IProject[0];
		}
		final List<IProject> result = new ArrayList<IProject>();
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		for (Object element : structuredSelection.toArray()) {
			if (element instanceof IProject) {
				result.add((IProject) element);
			}
		}
		return result.toArray(new IProject[0]);
	}
}
