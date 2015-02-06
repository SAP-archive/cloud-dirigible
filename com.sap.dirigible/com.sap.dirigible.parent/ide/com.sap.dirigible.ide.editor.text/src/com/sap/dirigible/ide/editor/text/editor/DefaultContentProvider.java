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

package com.sap.dirigible.ide.editor.text.editor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.editor.text.input.ContentEditorInput;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.logging.Logger;

public class DefaultContentProvider implements IContentProvider,
		IExecutableExtension {

	private static final String CANNOT_SAVE_FILE_CONTENTS = Messages.DefaultContentProvider_CANNOT_SAVE_FILE_CONTENTS;
	private static final String CANNOT_READ_FILE_CONTENTS = Messages.DefaultContentProvider_CANNOT_READ_FILE_CONTENTS;
	private static final String WE_SHOULD_NEVER_GET_HERE = Messages.DefaultContentProvider_WE_SHOULD_NEVER_GET_HERE;
	private static final Logger LOGGER = Logger
			.getLogger(DefaultContentProvider.class);

	@Override
	public String getContent(IEditorInput input)
			throws ContentProviderException {
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			return readFile(fileInput.getFile());
		}
		if (input instanceof ContentEditorInput) {
			ContentEditorInput contentInput = (ContentEditorInput) input;
			return new String(contentInput.getContent());
		}
		throw new IllegalStateException(WE_SHOULD_NEVER_GET_HERE);
	}

	@Override
	public void save(IProgressMonitor monitor, IEditorInput input,
			String content, boolean overwrite) throws ContentProviderException {
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			writeFile(fileInput.getFile(), content.getBytes());
			activateIfNeeded();
		}
	}

	private void activateIfNeeded() {
		// TODO Auto-generated method stub
		
	}

	protected static final String readFile(IFile file)
			throws ContentProviderException {
		try {
			BufferedReader in = null;
			if (file.getClass().getCanonicalName().equals("com.sap.dirigible.ide.workspace.impl.File")) {
				in = new BufferedReader(new InputStreamReader(
						file.getContents()));
			} else {
				IResource resource = getFromRepository(file);  
				in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resource.getContent())));
			}
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = in.readLine()) != null) {
				builder.append(line);
				builder.append("\n"); //$NON-NLS-1$
			}
			return builder.toString();
		} catch (Exception ex) {
			LOGGER.error(CANNOT_READ_FILE_CONTENTS, ex);
			throw new ContentProviderException(CANNOT_READ_FILE_CONTENTS, ex);
		}
	}

	private static IResource getFromRepository(IFile file) {
		IRepository repository = RepositoryFacade.getInstance().getRepository();
		
		String resourcePath = 
				IRepositoryPaths.DB_DIRIGIBLE_USERS + CommonParameters.getUserName() +
				IRepositoryPaths.SEPARATOR +
				IRepositoryPaths.WORKSPACE_FOLDER_NAME + file.getFullPath();
		IResource resource = repository.getResource(resourcePath);
		return resource;
	}

	protected static final void writeFile(IFile file, byte[] content)
			throws ContentProviderException {
		try {
			if (file.getClass().getCanonicalName().equals("com.sap.dirigible.ide.workspace.impl.File")) {
			file.setContents(new ByteArrayInputStream(content), false, false,
					null);
			} else {
				IResource resource = getFromRepository(file);
				resource.setContent(content);
			}
		} catch (CoreException ex) {
			LOGGER.error(CANNOT_SAVE_FILE_CONTENTS, ex);
			throw new ContentProviderException(CANNOT_SAVE_FILE_CONTENTS, ex);
		} catch (IOException e) {
			LOGGER.error(CANNOT_SAVE_FILE_CONTENTS, e);
			throw new ContentProviderException(CANNOT_SAVE_FILE_CONTENTS, e);
		}
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		//
	}
}
