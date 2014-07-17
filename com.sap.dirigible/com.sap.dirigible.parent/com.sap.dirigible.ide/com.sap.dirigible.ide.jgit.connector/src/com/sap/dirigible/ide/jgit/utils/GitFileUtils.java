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

package com.sap.dirigible.ide.jgit.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.impl.Workspace;
import com.sap.dirigible.repository.api.ContentTypeHelper;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;

public class GitFileUtils {
	private static final String COULD_NOT_CREATE_TEMP_DIRECTORY = Messages.GitFileUtils_COULD_NOT_CREATE_TEMP_DIRECTORY;
	private static final String COULD_NOT_DELETE_TEMP_FILE = Messages.GitFileUtils_COULD_NOT_DELETE_TEMP_FILE;
	private static final String SLASH = "/"; //$NON-NLS-1$
	private static final String DOT_GIT = ".git"; //$NON-NLS-1$
	private static final int MINIMUM_URL_LENGTH = 25;

	public static boolean isValidRepositoryURI(String repositoryURI) {
		return (repositoryURI.endsWith(DOT_GIT)) && (repositoryURI.length() > MINIMUM_URL_LENGTH);
	}

	public static File createTempDirectory(String directory) throws IOException {
		String suffix = Long.toString(System.nanoTime());
		return createTempDirectory(directory, suffix);
	}

	public static File createTempDirectory(String directory, String suffix) throws IOException {
		final File temp = File.createTempFile(directory, suffix);
		if (!(temp.delete())) {
			throw new IOException(String.format(COULD_NOT_DELETE_TEMP_FILE, temp.getAbsolutePath()));
		}
		if (!(temp.mkdir())) {
			throw new IOException(String.format(COULD_NOT_CREATE_TEMP_DIRECTORY,
					temp.getAbsolutePath()));
		}
		return temp;
	}

	public static List<String> importProject(File gitDirectory, IRepository repository,
			String basePath, String dirigibleUser, GitProjectProperties properties)
			throws IOException {
		List<String> importedProjects = new ArrayList<String>(gitDirectory.listFiles().length);
		for (File file : gitDirectory.listFiles()) {
			String project = file.getName();
			if (file.isDirectory() && !project.equalsIgnoreCase(DOT_GIT)) {
				importProjectFromGitRepoToDGBWorkspace(file, repository, basePath + project);
				saveGitPropertiesFile(repository, properties, dirigibleUser, project);
				importedProjects.add(project);
			}
		}
		return importedProjects;
	}

	private static void importProjectFromGitRepoToDGBWorkspace(File gitRepositoryFile,
			IRepository dirigibleRepository, String path) throws IOException {
		if (gitRepositoryFile.isDirectory()) {
			for (File file : gitRepositoryFile.listFiles()) {
				importProjectFromGitRepoToDGBWorkspace(file, dirigibleRepository, path + SLASH
						+ file.getName());
			}
		}
		if (!gitRepositoryFile.isDirectory()) {
			dirigibleRepository.createResource(path).setContent(readFile(gitRepositoryFile));
		}
	}

	public static byte[] readFile(File file) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final InputStream in = new FileInputStream(file);
		try {
			IOUtils.copy(in, out);
		} finally {
			in.close();
		}
		return out.toByteArray();
	}

	public static void deleteDGBRepositoryProject(IProject project, String dirigibleUser)
			throws IOException {
		String projectPath = project.getFullPath().toString();
		String repositoryPath = String.format(GitProjectProperties.DB_DIRIGIBLE_USERS_S_WORKSPACE,
				dirigibleUser);
		String fullPath = repositoryPath + projectPath;
		Workspace workspace = (Workspace) RemoteResourcesPlugin.getWorkspace();
		IRepository repository = workspace.getRepository();
		repository.getCollection(fullPath).delete();
	}

	public static void saveGitPropertiesFile(IRepository repository,
			GitProjectProperties properties, String user, String project) throws IOException {
		String dirigibleGitFolderPath = String.format(
				GitProjectProperties.DB_DIRIGIBLE_USERS_S_GIT_S_REPOSITORY, user, project);
		if (repository.hasCollection(dirigibleGitFolderPath)) {
			ICollection propertiesFolder = repository.getCollection(dirigibleGitFolderPath);
			
			if (propertiesFolder.getResource(GitProjectProperties.PROJECT_GIT_PROPERTY).exists()) {
				IResource propertiesFile = propertiesFolder
						.getResource(GitProjectProperties.PROJECT_GIT_PROPERTY);
				propertiesFile.setContent(properties.getContent());
			} else {
				// IResource propertiesFile =
				propertiesFolder.createResource(GitProjectProperties.PROJECT_GIT_PROPERTY,
						properties.getContent(), false, ContentTypeHelper.DEFAULT_CONTENT_TYPE);
				// propertiesFile.setContent(properties.getContent());
			}
		} else {
			repository.createCollection(dirigibleGitFolderPath).createResource(
					GitProjectProperties.PROJECT_GIT_PROPERTY, properties.getContent(), false,
					ContentTypeHelper.DEFAULT_CONTENT_TYPE);
		}

	}

	public static void deleteProjectFolderFromDirectory(File parentDirectory, String selectedProject) {
		for (File next : parentDirectory.listFiles()) {
			if (next.getName().equals(selectedProject)) {
				deleteFiles(next);
				next.delete();
			}
		}
	}

	public static void deleteDirectory(File directory) {
		if (directory != null) {
			deleteFiles(directory);
			directory.delete();
		}
	}

	private static void deleteFiles(File directory) {
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				}
				if (!file.delete()) {
					file.deleteOnExit();
				}
			}
		}
	}

	public static void copyProjectToDirectory(IContainer source, File tempGitDirectory)
			throws IOException, CoreException {
		if (!source.exists()) {
			return;
		}
		for (org.eclipse.core.resources.IResource resource : source.members()) {
			if (resource instanceof IFolder) {
				copyProjectToDirectory((IFolder) resource, tempGitDirectory);

			}
			if (resource instanceof IFile) {
				IPath path = resource.getFullPath();
				StringBuilder resourceDirectory = new StringBuilder();
				for (int i = 0; i < path.segmentCount() - 1; i++) {
					resourceDirectory.append(SLASH + path.segment(i));
				}
				resourceDirectory.append(SLASH);
				new File(tempGitDirectory, resourceDirectory.toString()).mkdirs();
				String resourcePath = resource.getFullPath().toOSString();

				InputStream in = ((IFile) resource).getContents();
				File outputFile = new File(tempGitDirectory, resourcePath);

				FileOutputStream out = new FileOutputStream(outputFile);
				IOUtils.copy(in, out);

				in.close();
				out.flush();
				out.close();
			}
		}
	}

	public static GitProjectProperties getGitPropertiesForProject(final IProject selectedProject,
			String user) throws IOException {
		Workspace workspace = ((Workspace) selectedProject.getWorkspace());
		IRepository dirigibleRepository = workspace.getRepository();

		String projectName = selectedProject.getName();
		com.sap.dirigible.repository.api.IResource resource = dirigibleRepository
				.getResource(String.format(GitProjectProperties.GIT_PROPERTY_FILE_LOCATION, user,
						projectName));
		InputStream in = new ByteArrayInputStream(resource.getContent());
		GitProjectProperties gitProperties = new GitProjectProperties(in);
		return gitProperties;
	}
}
