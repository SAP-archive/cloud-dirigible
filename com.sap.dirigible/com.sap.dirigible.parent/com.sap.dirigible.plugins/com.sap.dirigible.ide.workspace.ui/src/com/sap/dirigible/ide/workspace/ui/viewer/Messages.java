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

package com.sap.dirigible.ide.workspace.ui.viewer;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sap.dirigible.ide.workspace.ui.viewer.messages"; //$NON-NLS-1$
	public static String WorkspaceContentProvider_COULD_NOT_DETERMINE_IF_CONTAINER_HAS_CHILDREN;
	public static String WorkspaceContentProvider_COULD_NOT_GET_THE_CONTAINER_S_CHILDREN;
	public static String WorkspaceDragSourceListener_WE_HAVE_AN_UNKNOWN_ELEMENT_IN_THE_SELECTION;
	public static String WorkspaceDropTargetListener_COULD_NOT_MOVE_RESOURCE;
	public static String WorkspaceDropTargetListener_NULL_TRANSFER_DATA;
	public static String WorkspaceDropTargetListener_TRYING_TO_MOVE_A_RESOURCE_TO_ITSELF;
	public static String WorkspaceDropTargetListener_UNKNOWN_DROP_TARGET;
	public static String WorkspaceTransfer_COULD_NOT_PERSIST_TRANSFER_DATA;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
