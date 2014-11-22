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

import java.util.ArrayList;

public class TreeParent extends TreeObject {
	private ArrayList<TreeObject> children;
	private IDbConnectionFactory connectionFactory;

	public TreeParent(String name, IDbConnectionFactory connectionFactory) {
		super(name);
		this.connectionFactory = connectionFactory;
		children = new ArrayList<TreeObject>();
	}

	public void addChild(TreeObject child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(TreeObject child) {
		children.remove(child);
		child.setParent(null);
	}

	public TreeObject[] getChildren() {
		return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public IDbConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	@Override
	public int hashCode() {
		int toCode = super.hashCode();
		if (children != null && children.size() > 0) {
			for (TreeObject to : children) {
				toCode += (to != null ? to.hashCode() : 0);
			}
		}
		return toCode;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!TreeParent.class.isInstance(arg0)) {
			return false;
		}
		TreeParent tp = (TreeParent) arg0;
		if (super.equals(arg0)) {
			boolean compareChildren = compareChildren(tp.getChildren(),
					getChildren());
			if (!compareChildren) {
				// uncomment to debug
				// System.out.println(tp.getName() + " vs " + getName() +" : "+
				// compareChildren);
			}
			return compareChildren;
		}
		return false;
	}

	private boolean compareChildren(TreeObject[] childrenHome,
			TreeObject[] childrenAway) {
		if (childrenAway != null && childrenHome != null) {
			if (childrenAway.length == childrenHome.length) {
				boolean f = false;
				for (TreeObject treeObjectA : childrenAway) {
					f = false;
					for (TreeObject treeObjectH : childrenHome) {
						if (TreeParent.class.isInstance(treeObjectH)
								&& TreeParent.class.isInstance(treeObjectA)) {
							f = treeObjectA.getName().equals(
									treeObjectH.getName());
						} else {
							f = treeObjectH != null
									&& treeObjectH.equals(treeObjectA);
						}
						if (f) {
							break;
						}
					}
					if (!f) {
						return false;
					}
				}
				return true;
			}
		} else {
			return childrenHome == childrenAway;
		}
		return false;
	}
}