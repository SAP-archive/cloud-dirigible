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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.ui.IEditorInput;

import com.sap.dirigible.ide.logging.Logger;

public class ContentProviderFactory {

	private static final String CANNOT_READ_CONTENT_PROVIDER_EXTENSION_CLASS_0_INPUT_TYPE_1 = Messages.ContentProviderFactory_CANNOT_READ_CONTENT_PROVIDER_EXTENSION_CLASS_0_INPUT_TYPE_1;

	private static final String UNSUPPORTED_I_EDITOR_INPUT = Messages.ContentProviderFactory_UNSUPPORTED_I_EDITOR_INPUT;

	private static final Logger LOGGER = Logger
			.getLogger(ContentProviderFactory.class);

	private static final String EXT_POINT_ID = "com.sap.dirigible.ide.editor.text.contentProviders"; //$NON-NLS-1$

	private static ContentProviderFactory INSTANCE;

	private Map<String, IContentProvider> contentProviders;

	private ContentProviderFactory() {
		contentProviders = new HashMap<String, IContentProvider>();
		readExtensions();
	}

	public static ContentProviderFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ContentProviderFactory();
		}
		return INSTANCE;
	}

	public IContentProvider getContentProvider(IEditorInput input) {
		Class<? extends IEditorInput> inputClass = input.getClass();

		for (Entry<String, IContentProvider> entry : contentProviders
				.entrySet()) {
			// exact matches
			String entryInputClassName = entry.getKey();
			if (inputClass.getName().equals(entryInputClassName)) {
				return entry.getValue();
			}
		}

		for (Entry<String, IContentProvider> entry : contentProviders
				.entrySet()) {
			String entryInputClassName = entry.getKey();

			if (isInstanceOf(entryInputClassName, inputClass)) {
				return entry.getValue();
			}
		}

		LOGGER.warn(UNSUPPORTED_I_EDITOR_INPUT
				+ input.getClass().getCanonicalName());
		return null;
	}

	@SuppressWarnings("rawtypes")
	private boolean isInstanceOf(String objectClassName, Class clazz) {
		if (clazz.getName().equals(objectClassName)) {
			return true;
		}

		Class superclass = clazz.getSuperclass();
		if (superclass != null && isInstanceOf(objectClassName, superclass)) {
			return true;
		}

		for (Class intf : clazz.getInterfaces()) {
			if (isInstanceOf(objectClassName, intf)) {
				return true;
			}
		}

		return false;
	}

	private void readExtensions() {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IConfigurationElement[] configurationElements = registry
				.getConfigurationElementsFor(EXT_POINT_ID);

		for (IConfigurationElement configurationElement : configurationElements) {
			String inputType = configurationElement.getAttribute("inputTypes"); //$NON-NLS-1$

			try {
				contentProviders.put(inputType,
						(IContentProvider) configurationElement
								.createExecutableExtension("class")); //$NON-NLS-1$
			} catch (CoreException e) {
				String msg = MessageFormat
						.format(CANNOT_READ_CONTENT_PROVIDER_EXTENSION_CLASS_0_INPUT_TYPE_1,
								configurationElement.getAttribute("class"), inputType); //$NON-NLS-1$
				LOGGER.error(msg, e);
			}
		}
	}

}
