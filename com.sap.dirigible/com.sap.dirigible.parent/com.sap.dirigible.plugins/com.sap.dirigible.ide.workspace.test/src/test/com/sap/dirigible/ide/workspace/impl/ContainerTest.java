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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.dirigible.ide.workspace.impl.Container;
import com.sap.dirigible.ide.workspace.impl.Workspace;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;

public class ContainerTest {
	@Mock
	private IPath path;
	@Mock
	private Workspace workspace;
	@Mock
	private IWorkspaceRoot root;
	@Mock
	private IRepository repository;
	@Mock
	private ICollection expectedEntity;
	@Mock
	private IPath location;

	private Container container;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(workspace.getRepository()).thenReturn(repository);
		when(workspace.getRoot()).thenReturn(root);
		when(workspace.getLocation()).thenReturn(location);

		when(repository.getCollection(anyString())).thenReturn(expectedEntity);
		when(root.getLocation()).thenReturn(path);
		when(root.toString()).thenReturn("");

		when(path.append(any(IPath.class))).thenReturn(path);
		when(path.append(anyString())).thenReturn(path);

		when(location.append(any(IPath.class))).thenReturn(location);
		when(location.append(anyString())).thenReturn(location);

		container = new Container(path, workspace);
	}

	@Test
	public void getEntity() {
		IEntity actualEntity = container.getEntity();

		assertNotNull(actualEntity);
		assertEquals(expectedEntity, actualEntity);
	}

	@Test
	public void exists() {
		boolean exists = container.exists(location);
		assertFalse(exists);

		when(workspace.hasResource(location)).thenReturn(true);

		exists = container.exists(location);
		assertTrue(exists);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFolderWithPathWithLessThanTwoSegmentsShouldThrowException() {
		container.getFolder(path);
	}

	@Test
	public void getFolder() {
		when(path.segmentCount()).thenReturn(2);

		IFolder folder = container.getFolder(path);

		assertNotNull(folder);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFileWithPathWithLessThanTwoSegmentsShouldThrowException() {
		container.getFile(path);
	}

	@Test
	public void getFile() {
		when(path.segmentCount()).thenReturn(2);

		IFile file = container.getFile(path);

		assertNotNull(file);
	}

	@Test
	public void findMemberByIPath() {
		IResource expectedMember = Mockito.mock(IResource.class);

		when(workspace.newResource(any(IPath.class))).thenReturn(expectedMember);
		when(expectedMember.exists()).thenReturn(true);

		IResource actualMember = container.findMember(path);

		assertNotNull(actualMember);
		assertEquals(expectedMember, actualMember);
	}

	@Test
	public void findMemberByString() {
		IResource expectedMember = Mockito.mock(IResource.class);

		when(workspace.newResource(any(IPath.class))).thenReturn(expectedMember);
		when(expectedMember.exists()).thenReturn(true);

		IResource actualMember = container.findMember("test");

		assertNotNull(actualMember);
		assertEquals(expectedMember, actualMember);
	}

	@Test
	public void whenFindingMemberThatDoNotExsitsShouldReturnNull() {
		IResource expectedMember = Mockito.mock(IResource.class);

		when(workspace.newResource(any(IPath.class))).thenReturn(expectedMember);
		when(workspace.newResource(any(IPath.class))).thenReturn(null);

		IResource actualMember = container.findMember(path);

		assertNull(actualMember);

		actualMember = container.findMember(path);

		assertNull(actualMember);

	}

	@Test
	public void emptyMembers() throws Exception {
		IResource[] members = container.members();

		assertNotNull(members);
		assertEquals(0, members.length);
	}

	@Test
	public void members() throws Exception {
		List<String> collectionsNames = new ArrayList<String>();
		collectionsNames.add("member1");
		collectionsNames.add("member2");

		when(expectedEntity.getCollectionsNames()).thenReturn(collectionsNames);

		IResource[] members = container.members(true);

		assertNotNull(members);
		assertEquals(2, members.length);
	}

	@Test(expected = CoreException.class)
	public void whenErrorOccursDuringExtractingMembersCoreExceptionShouldBeThrows()
			throws Exception {
		doThrow(new IOException()).when(expectedEntity).getCollectionsNames();

		container.members(true);

	}

	@Test
	public void getFilters() throws Exception {
		IResourceFilterDescription[] filterDescription = container.getFilters();
		assertNotNull(filterDescription);
	}

	@Test
	public void whenUsingUnsupportedMethodExceptionShouldBeThrown() throws Exception {
		try {
			container.createFilter(0, null, 0, null);
			fail("container.createFilter(int, FileInfoMatcherDescription, int, IProgressMonitor) should be unsupported");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		try {
			container.removeFilter(null, 0, null);
			fail("container.removeFilter(IResourceFilterDescription, int, IProgressMonitor) should be unsupported");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
	}

}