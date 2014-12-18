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

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.content.IContentTypeMatcher;

import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.impl.event.ResourceChangeEvent;

@SuppressWarnings("deprecation")
public class Project extends Container implements IProject {

	private static final String METHOD_NOT_SUPPORTED = Messages.Project_METHOD_NOT_SUPPORTED;
	private static final String COULD_NOT_CREATE_PROJECT = Messages.Project_COULD_NOT_CREATE_PROJECT;
	private static final String PROJECT_ALREADY_EXIST = Messages.Project_PROJECT_ALREADY_EXIST;
	
	private static final Logger logger = Logger.getLogger(Project.class);
	
	/**
	 * Use some persistence method.
	 */
	private boolean opened = true; // FIXME: false

	public Project(IPath path, Workspace workspace) {
		super(path, workspace);
	}

	@Override
	public void build(int arg0, IProgressMonitor arg1) throws CoreException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void build(int arg0, String arg1, Map arg2, IProgressMonitor arg3)
			throws CoreException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void close(IProgressMonitor arg0) throws CoreException {
		opened = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void create(IProgressMonitor monitor) throws CoreException {
		monitor = Resource.monitorWrapper(monitor);
		try {
			monitor.beginTask("project creation", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
			IStatus pathValidation = workspace.validatePath(path.toString(),
					PROJECT);
			if (!pathValidation.isOK()) {
				throw new CoreException(pathValidation);
			}
			if (exists()) {
				throw new CoreException(createErrorStatus(PROJECT_ALREADY_EXIST));
			}
			try {
				getEntity().create();
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
				throw new CoreException(createErrorStatus(String.format(COULD_NOT_CREATE_PROJECT, ex.getMessage()))); // NOPMD
			}
			workspace.notifyResourceChanged(new ResourceChangeEvent(this,
					ResourceChangeEvent.POST_CHANGE));
		} finally {
			monitor.done();
		}
	}

	@Override
	public void create(IProjectDescription description, IProgressMonitor monitor)
			throws CoreException {
		throw new UnsupportedOperationException(METHOD_NOT_SUPPORTED);
	}

	@Override
	public void create(IProjectDescription description, int updateFlags,
			IProgressMonitor monitor) throws CoreException {
		throw new UnsupportedOperationException(METHOD_NOT_SUPPORTED);
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
	public void delete(int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		workspace.notifyResourceChanged(new ResourceChangeEvent(this,
				ResourceChangeEvent.PRE_DELETE));
		super.delete(updateFlags, monitor);
	}

	@Override
	public IContentTypeMatcher getContentTypeMatcher() throws CoreException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IProjectDescription getDescription() throws CoreException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public IFile getFile(String name) {
		IPath resourcePath = this.path.append(name);
		return new File(resourcePath, workspace);
	}

	/**
	 * {@inheritDoc}
	 */
	public IFolder getFolder(String name) {
		IPath resourcePath = this.path.append(name);
		return new Folder(resourcePath, workspace);
	}

	@Override
	public IProjectNature getNature(String arg0) throws CoreException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IPathVariableManager getPathVariableManager() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public IPath getPluginWorkingLocation(IPluginDescriptor plugin) {
		return getWorkingLocation(plugin.getUniqueIdentifier());
	}

	/**
	 * {@inheritDoc}
	 */
	public IProject[] getReferencedProjects() throws CoreException {
		return new IProject[0]; // This project never references projects
	}

	/**
	 * {@inheritDoc}
	 */
	public IProject[] getReferencingProjects() {
		return new IProject[0]; // This project is never referenced
	}

	/**
	 * {@inheritDoc}
	 */
	public IPath getWorkingLocation(String id) {
		// We do not support working area
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasNature(String arg0) throws CoreException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNatureEnabled(String arg0) throws CoreException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOpen() {
		return opened;
	}

	/**
	 * {@inheritDoc}
	 */
	public void move(IProjectDescription description, boolean force,
			IProgressMonitor monitor) throws CoreException {
		final int flags = force ? FORCE : IResource.NONE;
		move(description, flags, monitor);
	}

	/**
	 * {@inheritDoc}
	 */
	public void open(IProgressMonitor monitor) throws CoreException {
		open(IResource.NONE, monitor);
	}

	/**
	 * {@inheritDoc}
	 */
	public void open(int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		opened = true;
	}

	@Override
	public void setDescription(IProjectDescription arg0, IProgressMonitor arg1)
			throws CoreException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDescription(IProjectDescription arg0, int arg1,
			IProgressMonitor arg2) throws CoreException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void loadSnapshot(int arg0, URI arg1, IProgressMonitor arg2)
			throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveSnapshot(int arg0, URI arg1, IProgressMonitor arg2)
			throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void build(IBuildConfiguration config, int kind,
			IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBuildConfiguration getActiveBuildConfig() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBuildConfiguration getBuildConfig(String configName)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBuildConfiguration[] getBuildConfigs() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBuildConfiguration[] getReferencedBuildConfigs(String configName,
			boolean includeMissing) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasBuildConfig(String configName) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

}
