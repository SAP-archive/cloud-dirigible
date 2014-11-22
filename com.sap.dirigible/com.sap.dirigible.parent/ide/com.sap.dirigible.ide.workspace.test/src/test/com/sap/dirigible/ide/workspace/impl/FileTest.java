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

package test.com.sap.dirigible.ide.workspace.impl;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.dirigible.ide.workspace.impl.File;
import com.sap.dirigible.ide.workspace.impl.Workspace;
import com.sap.dirigible.ide.workspace.impl.WorkspaceRoot;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;

public class FileTest {
	private static final String TASK_APPENDING_FILE_CONTENTS = "appending file contents";
	private static final String TASK_FILE_CONTENT_CHANGE = "file content change";
	private static final int TASK_TOTAL_WORK_UNKNOWN = IProgressMonitor.UNKNOWN;
	private static final String TASK_FILE_CREATION = "file creation";
	@Mock
	private IPath path;
	@Mock
	private Workspace workspace;
	@Mock
	private InputStream source;
	@Mock
	private IProgressMonitor progressMonitor;
	@Mock
	private IStatus pathValidationStatus;
	@Mock
	private WorkspaceRoot root;
	@Mock
	private IRepository repository;
	@Mock
	private IResource resource;
	@Mock
	private IProject project;

	private File file;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(workspace.validatePath(anyString(), anyInt())).thenReturn(pathValidationStatus);
		when(workspace.getLocation()).thenReturn(path);
		when(workspace.getRepository()).thenReturn(repository);
		when(workspace.getRoot()).thenReturn(root);
		when(root.getLocation()).thenReturn(path);
		when(root.getProject(anyString())).thenReturn(project);
		when(path.append(any(IPath.class))).thenReturn(path);
		when(path.append(anyString())).thenReturn(path);
		when(path.removeLastSegments(anyInt())).thenReturn(path);
		when(repository.getResource(anyString())).thenReturn(resource);

		file = new File(path, workspace);
	}

	@Test(expected = CoreException.class)
	public void creatingFileWithInvalidPathShouldThrowException() throws Exception {
		when(pathValidationStatus.isOK()).thenReturn(false);

		file.create(source, false, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_FILE_CREATION, TASK_TOTAL_WORK_UNKNOWN);
		verify(progressMonitor, times(1)).done();
	}

	@Test(expected = CoreException.class)
	public void creatingFileThatAlreadyExistsShouldThrowException() throws Exception {
		when(pathValidationStatus.isOK()).thenReturn(true);
		when(workspace.hasResource(any(IPath.class))).thenReturn(true);
		file.create(source, false, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_FILE_CREATION, TASK_TOTAL_WORK_UNKNOWN);
		verify(progressMonitor, times(1)).done();
	}

	@Test(expected = CoreException.class)
	public void creatingFileWithoutParentShouldThrowException() throws Exception {
		IFolder parentFolder = Mockito.mock(IFolder.class);

		when(pathValidationStatus.isOK()).thenReturn(true);
		when(workspace.hasResource(any(IPath.class))).thenReturn(false);
		when(path.segmentCount()).thenReturn(2);
		when(root.getFolder(any(IPath.class))).thenReturn(parentFolder);

		file.create(source, false, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_FILE_CREATION, TASK_TOTAL_WORK_UNKNOWN);
		verify(progressMonitor, times(1)).done();
	}

	@Test(expected = CoreException.class)
	public void creatingFileInClosedParentProjectShouldThrowsException() throws Exception {
		IProject parentProject = Mockito.mock(IProject.class);

		when(pathValidationStatus.isOK()).thenReturn(true);
		when(root.getProject(anyString())).thenReturn(parentProject);
		when(path.segmentCount()).thenReturn(1);
		when(parentProject.exists()).thenReturn(true);

		file.create(source, false, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_FILE_CREATION, TASK_TOTAL_WORK_UNKNOWN);
		verify(progressMonitor, times(1)).done();
	}

	@Test
	public void creatingFile() throws Exception {
		IProject parentProject = Mockito.mock(IProject.class);

		when(pathValidationStatus.isOK()).thenReturn(true);
		when(root.getProject(anyString())).thenReturn(parentProject);
		when(path.segmentCount()).thenReturn(1);
		when(parentProject.exists()).thenReturn(true);
		when(parentProject.isOpen()).thenReturn(true);

		file.create(source, false, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_FILE_CREATION, TASK_TOTAL_WORK_UNKNOWN);
		verify(workspace, times(1)).notifyResourceChanged(any(IResourceChangeEvent.class));
		verify(progressMonitor, times(1)).done();
	}

	@Test(expected = CoreException.class)
	public void settingContentOfFileThatDoNotExistsShouldThrowException() throws Exception {
		file.setContents(source, 0, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_FILE_CONTENT_CHANGE,
				TASK_TOTAL_WORK_UNKNOWN);
		verify(progressMonitor, times(1)).done();
	}

	@Test
	public void settingContentOfFile() throws Exception {
		when(project.isOpen()).thenReturn(true);
		when(resource.exists()).thenReturn(true);

		file.setContents(source, 0, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_FILE_CONTENT_CHANGE,
				TASK_TOTAL_WORK_UNKNOWN);
		verify(workspace, times(1)).notifyResourceChanged(any(IResourceChangeEvent.class));
		verify(progressMonitor, times(1)).done();
	}

	@Test(expected = CoreException.class)
	public void appendingContentToNotExistingFileShouldThrowException() throws Exception {

		file.appendContents(source, 0, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_APPENDING_FILE_CONTENTS,
				TASK_TOTAL_WORK_UNKNOWN);
		verify(progressMonitor, times(1)).done();
	}

	@Test
	public void appendingContentToFile() throws Exception {
		when(project.isOpen()).thenReturn(true);
		when(resource.exists()).thenReturn(true);
		when(resource.getContent()).thenReturn(new byte[1024]);
		
		file.appendContents(source, 0, progressMonitor);

		verify(progressMonitor, times(1)).beginTask(TASK_APPENDING_FILE_CONTENTS,
				TASK_TOTAL_WORK_UNKNOWN);
		verify(workspace, times(1)).notifyResourceChanged(any(IResourceChangeEvent.class));
		verify(progressMonitor, times(1)).done();

	}

	@Test
	public void whenUsingUnsupportedMethodExceptionShouldBeThrown() throws Exception {
		try {
			file.createLink(path, 0, progressMonitor);
			fail("file.createLink(IPath, int, IProgressMonitor) should be unsupported");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		try {
			file.createLink(new URI(""), 0, progressMonitor);
			fail("file.createLink(IPath, int, IProgressMonitor) should be unsupported");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		try {
			IFileState fileState = Mockito.mock(IFileState.class);
			file.setContents(fileState, 0, progressMonitor);
			fail("file.setContents(IFileState, int, IProgressMonitor) should be unsupported");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
	}
}
