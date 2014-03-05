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

package com.sap.dirigible.ide.publish;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.sap.dirigible.ide.common.ExtensionPointUtils;

public final class PublishManager {

	private static final String PUBLISHER_EXTENSION_HAS_AN_INVALID_IMPLEMENTING_CLASS_CONFIGURED = Messages
			.getString("PublishManager.PUBLISHER_EXTENSION_HAS_AN_INVALID_IMPLEMENTING_CLASS_CONFIGURED"); //$NON-NLS-1$

	private static final String COULD_NOT_CREATE_PUBLISHER_INSTANCE = Messages
			.getString("PublishManager.COULD_NOT_CREATE_PUBLISHER_INSTANCE"); //$NON-NLS-1$

	private static final String EXTENSION_POINT_0_COULD_NOT_BE_FOUND = Messages
			.getString("PublishManager.EXTENSION_POINT_0_COULD_NOT_BE_FOUND"); //$NON-NLS-1$

	private static final String PUBLISHER_EXTENSION_POINT_ID = "com.sap.dirigible.ide.publish.publisher"; //$NON-NLS-1$

	private static final String PUBLISHER_ELEMENT_NAME = "publisher"; //$NON-NLS-1$

	private static final String PUBLISHER_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	static List<IPublisher> publishers = null;

	/**
	 * Returns a list {@link IPublisher}
	 * 
	 * @return a list of {@link IPublisher} or <code>null</code> such is not
	 *         found that can handle the specified project type.
	 */
	public static List<IPublisher> getPublishers() {

		synchronized (PublishManager.class) {
			if (publishers == null) {
				publishers = new ArrayList<IPublisher>();
				final IExtensionPoint extensionPoint = ExtensionPointUtils.getExtensionPoint(PUBLISHER_EXTENSION_POINT_ID);
				if (extensionPoint == null) {
					throw new PublishManagerException(format(
							EXTENSION_POINT_0_COULD_NOT_BE_FOUND,
							PUBLISHER_EXTENSION_POINT_ID));
				}
				final IConfigurationElement[] publisherElements = getPublisherElements(extensionPoint
						.getExtensions());

				String publisherName = null;
				try {
					for (int i = 0; i < publisherElements.length; i++) {
						publisherName = (String) publisherElements[i].getAttribute(PUBLISHER_CLASS_ATTRIBUTE);
						publishers.add(createPublisher(publisherElements[i]));
					}
				} catch (CoreException ex) {
					throw new PublishManagerException(
							String.format(COULD_NOT_CREATE_PUBLISHER_INSTANCE, publisherName), ex);
				}
			}
		}
		return publishers;
	}

	private static IConfigurationElement[] getPublisherElements(
			IExtension[] extensions) {
		final List<IConfigurationElement> result = new ArrayList<IConfigurationElement>();
		for (IExtension extension : extensions) {
			for (IConfigurationElement element : extension
					.getConfigurationElements()) {
				if (PUBLISHER_ELEMENT_NAME.equals(element.getName())) {
					result.add(element);
				}
			}
		}
		return result.toArray(new IConfigurationElement[0]);
	}

	private static IPublisher createPublisher(
			IConfigurationElement publisherElement) throws CoreException {
		final Object publisher = publisherElement
				.createExecutableExtension(PUBLISHER_CLASS_ATTRIBUTE);
		if (!(publisher instanceof IPublisher)) {
			throw new PublishManagerException(
					PUBLISHER_EXTENSION_HAS_AN_INVALID_IMPLEMENTING_CLASS_CONFIGURED);
		}
		return (IPublisher) publisher;
	}

	private PublishManager() {
		super();
	}

}
