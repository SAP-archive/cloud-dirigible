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

package com.sap.dirigible.ide.repository.ui.viewer;

public class RepositoryViewerResolveNode {

	private static final String CLICK_TO_RESOLVE = Messages.RepositoryViewerResolveNode_CLICK_TO_RESOLVE;

	private final Object parent;

	private final String text;

	public RepositoryViewerResolveNode() {
		this((Object) null);
	}

	public RepositoryViewerResolveNode(Object parent) {
		this(parent, CLICK_TO_RESOLVE); // TODO: I18N
	}

	public RepositoryViewerResolveNode(String text) {
		this(null, text);
	}

	public RepositoryViewerResolveNode(Object parent, String text) {
		this.parent = parent;
		this.text = text;
	}

	public Object getParent() {
		return parent;
	}

	public String getText() {
		return text;
	}

}
