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

package com.sap.dirigible.ide.db.viewer.views;

import org.eclipse.core.runtime.IAdaptable;

public class TreeObject implements IAdaptable {

	private String name;
	private TreeParent parent;
	private TableDefinition tableDefinition = null;

	public TreeObject(String name) {
		this(name, null);
	}

	public TreeObject(String name, TableDefinition tableDefinition) {
		this.name = name;
		this.tableDefinition = tableDefinition;
	}

	public String getName() {
		return name;
	}

	public void setParent(TreeParent parent) {
		this.parent = parent;
	}

	public TreeParent getParent() {
		return parent;
	}

	public String toString() {
		return getName();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class key) {
		return null;
	}

	public TableDefinition getTableDefinition() {
		return tableDefinition;
	}

	@Override
	public int hashCode() {
		return name.hashCode()
				+ (tableDefinition != null ? tableDefinition.hashCode() : 0);
	}

	@Override
	public boolean equals(Object arg0) {
		if (!TreeObject.class.isInstance(arg0)) {
			return false;
		}
		TreeObject to = (TreeObject) arg0;
		if (to.getName() != null) {
			if (to.getName().equals(getName())) {
				if (to.getTableDefinition() == getTableDefinition()) {
					return true;
				} else {
					if (to.getTableDefinition() != null) {
						return to.getTableDefinition().equals(
								getTableDefinition());
					}
				}
			}
		}
		return super.equals(arg0);
	}

}
