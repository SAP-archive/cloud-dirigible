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

package com.sap.dirigible.ide.workspace.impl;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class WorkspaceRoot extends Container implements IWorkspaceRoot {

	private static final String ONLY_PROJECTS_SHOULD_BE_CHILDREN_OF_THE_ROOT = Messages.WorkspaceRoot_ONLY_PROJECTS_SHOULD_BE_CHILDREN_OF_THE_ROOT;
	private static final String THE_PROJECT_PATH_MUST_HAVE_EXACTLY_ONE_SEGMENT = Messages.WorkspaceRoot_THE_PROJECT_PATH_MUST_HAVE_EXACTLY_ONE_SEGMENT;

	public WorkspaceRoot(IPath path, Workspace workspace) {
		super(path, workspace);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(boolean deleteContent, boolean force,
			IProgressMonitor monitor) throws CoreException {
		int flags = (deleteContent ? IResource.ALWAYS_DELETE_PROJECT_CONTENT
				: IResource.NEVER_DELETE_PROJECT_CONTENT)
				| (force ? FORCE : IResource.NONE);
		delete(flags, monitor);
	}

	@Override
	public IContainer[] findContainersForLocation(IPath arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IContainer[] findContainersForLocationURI(URI arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IContainer[] findContainersForLocationURI(URI arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFile[] findFilesForLocation(IPath arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFile[] findFilesForLocationURI(URI arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IFile[] findFilesForLocationURI(URI arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public IContainer getContainerForLocation(IPath location) {
		IWorkspaceRoot root = workspace.getRoot();
		if (!root.getLocation().isPrefixOf(location)) {
			return null;
		}
		IResource resource = workspace.newResource(location);
		if (resource instanceof IContainer) {
			return (IContainer) resource;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IFile getFileForLocation(IPath location) {
		IResource resource = workspace.newResource(location);
		if (resource instanceof IFile) {
			return (IFile) resource;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IProject getProject(String name) {
		// boolean segmentIsValid = Path.ROOT.isValidSegment(name);
		// if (!segmentIsValid) {
		// throw new IllegalArgumentException("Project name is not valid.");
		// }
		IPath resourcePath = path.append(name);
		if (resourcePath.segmentCount() != 1) {
			throw new IllegalArgumentException(
					THE_PROJECT_PATH_MUST_HAVE_EXACTLY_ONE_SEGMENT);
		}
		return new Project(resourcePath, workspace);
	}

	/**
	 * {@inheritDoc}
	 */
	public IProject[] getProjects() {
		return getProjects(IResource.NONE);
	}

	/**
	 * {@inheritDoc}
	 */
	public IProject[] getProjects(int memeberFlags) {
		try {
			IResource[] children = members();
			IProject[] projects = new IProject[children.length];
			try {
				System.arraycopy(children, 0, projects, 0, children.length);
			} catch (ArrayStoreException ex) {
				throw new IllegalStateException(
						ONLY_PROJECTS_SHOULD_BE_CHILDREN_OF_THE_ROOT, ex);
			}
			return projects;
		} catch (CoreException ex) {
			return new IProject[0];
		}
	}

}
