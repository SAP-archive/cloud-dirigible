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

package com.sap.dirigible.ide.workspace;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.rap.rwt.SingletonUtil;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.workspace.impl.Workspace;

/**
 * This class is similar to the
 * <code>org.eclipse.core.resources.ResourcesPlugin</code> class. It mimics most
 * of the methods.
 * <p>
 * What differs this class from the original is that it provides a unique
 * workspace for each of the users. This is required because of the milti-user
 * architecture of RAP.
 * 
 */
public class RemoteResourcesPlugin {

	public static final String PLUGIN_ID = "com.sap.dirigible.ide.workspace"; //$NON-NLS-1$

	public static final String GUEST_USER = "guest"; //$NON-NLS-1$

	/**
	 * Returns the workspace corresponding to this user session.
	 * <p>
	 * Once obtained, the {@link IWorkspace} instance can be used to create
	 * projects, directories, files, etc.
	 * <p>
	 * <strong>Node:</strong>Currently, the returned implementation will not
	 * support most of the methods and will throw an
	 * {@link UnsupportedOperationException} exception when that is the case.
	 * 
	 * @see UnsupportedOperationException
	 * @see IWorkspace
	 * @return an {@link IWorkspace} instance.
	 */
	public static IWorkspace getWorkspace() {
		String user = getUserName();
		return getWorkspace(user);
	}

	public static String getUserName() {
		return CommonParameters.getUserName();
	}

	/**
	 * Returns a {@link Workspace} instance for the specified user name.
	 * <p>
	 * <strong>Node:</strong>Currently, the returned implementation will not
	 * support most of the methods and will throw an
	 * {@link UnsupportedOperationException} exception when that is the case.
	 * 
	 * @see UnsupportedOperationException
	 * @see IWorkspace
	 * @param user
	 *            the user for which a workspace must be returned.
	 * @return a {@link Workspace} instance.
	 */
	public static IWorkspace getWorkspace(String user) {
		final Workspace result = (Workspace) SingletonUtil.getSessionInstance(Workspace.class);
		result.initialize(user);
		return result;
	}

	/**
	 * Always returns the shared workspace corresponding to the GUEST user.
	 * <p>
	 * Once obtained, the {@link IWorkspace} instance can be used to create
	 * projects, directories, files, etc.
	 * <p>
	 * <strong>Node:</strong>Currently, the returned implementation will not
	 * support most of the methods and will throw an
	 * {@link UnsupportedOperationException} exception when that is the case.
	 * 
	 * @see UnsupportedOperationException
	 * @see IWorkspace
	 * @return an {@link IWorkspace} instance.
	 */
	public static IWorkspace getSharedWorkspace() {
		return getWorkspace(GUEST_USER);
	}

}
