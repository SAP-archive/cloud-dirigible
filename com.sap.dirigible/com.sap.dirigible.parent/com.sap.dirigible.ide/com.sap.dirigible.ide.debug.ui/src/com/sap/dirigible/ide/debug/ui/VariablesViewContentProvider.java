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

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sap.dirigible.repository.ext.debug.VariableValue;
import com.sap.dirigible.repository.ext.debug.VariableValuesMetadata;

public class VariablesViewContentProvider implements ITreeContentProvider {
	private static final long serialVersionUID = -7946214629624017357L;
	private VariableValuesMetadata metadata;

	public VariableValuesMetadata getVariablesMetaData() {
		return metadata;
	}

	public void setVariablesMetaData(VariableValuesMetadata metadata) {
		this.metadata = metadata;
	}

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
		Object[] elements = null;
		if (metadata != null) {
			List<VariableValue> variableValueList = metadata.getVariableValueList();
			if (variableValueList != null) {
				elements = variableValueList.toArray(new VariableValue[variableValueList.size()]);
			}
		}
		if (elements == null) {
			elements = new VariableValue[] {};
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
