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

package com.sap.dirigible.ide.debug.ui;

import java.util.Iterator;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.debug.model.DebugModel;
import com.sap.dirigible.ide.debug.model.DebugModelFacade;

public class SessionsViewContentProvider implements ITreeContentProvider {
	private static final long serialVersionUID = -7946214629624017357L;

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
		String[] elements = new String[DebugModelFacade.getDebugModels().values().size()];
		int i=0;
		for (Iterator<DebugModel> iterator = DebugModelFacade.getDebugModels().values().iterator(); iterator.hasNext();) {
			DebugModel debugModel = iterator.next();
			
			StringBuilder label = new StringBuilder();
			label
				.append(debugModel.getUserId())
				.append(CommonParameters.DEBUG_SEPARATOR)
				.append(i+1)
				.append(CommonParameters.DEBUG_SEPARATOR)
				.append(debugModel.getExecutionId())
				.append(CommonParameters.DEBUG_SEPARATOR);
			elements[i++] = label.toString(); 
		}
		
		return elements;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

}
