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

package com.sap.dirigible.ide.repository.ui.viewer;

import org.eclipse.swt.graphics.Image;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IResource;

public class ArtifactLabelProvider extends AbstractArtifactLabelProvider {

	private static final long serialVersionUID = 7784009188278209963L;

	@Override
	public String getText(Object element) {
		if (element instanceof IEntity) {
			return getEntityText((IEntity) element);
		}
		if (element instanceof RepositoryViewerResolveNode) {
			return getResolveNodeText((RepositoryViewerResolveNode) element);
		}
		return null;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IEntity) {
			return getEntityImage((IEntity) element);
		}
		return null;
	}

	protected String getEntityText(IEntity entity) {
		final String name = entity.getName();
		if (name.isEmpty()) {
			return REPOSITORY_ROOT;
		}
		return name;
	}

	protected String getResolveNodeText(RepositoryViewerResolveNode node) {
		return node.getText();
	}

	protected Image getEntityImage(IEntity entity) {
		if (entity instanceof IResource) {
			final String resourceName = ((IResource) entity).getName()
					.toLowerCase();
			return getResourceImage(resourceName);
		}
		if (entity instanceof ICollection) {
			if (entity.getName().isEmpty()) {
				return getRepositoryRootImage((ICollection) entity);
			}
			return getCollectionImage((ICollection) entity);
		}
		return createImage(TYPE_UNKNOWN_ICON_URL);
	}

	protected Image getCollectionImage(ICollection collection) {
		if (collection != null) {
			String name = collection.getName();
			return getCollectionImageByName(name);
		}
		return createImage(TYPE_COLLECTION_ICON_URL);
	}

	protected Image getCollectionImageByName(String name) {
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.DATA_STRUCTURES)) {
			return createImage(TYPE_COLLECTION_DS_ICON_URL);
		}
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.EXTENSION_DEFINITIONS)) {
			return createImage(TYPE_COLLECTION_EXT_ICON_URL);
		}
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.INTEGRATION_SERVICES)) {
			return createImage(TYPE_COLLECTION_IS_ICON_URL);
		}
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES)) {
			return createImage(TYPE_COLLECTION_SS_ICON_URL);
		}
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.SECURITY_CONSTRAINTS)) {
			return createImage(TYPE_COLLECTION_SEC_ICON_URL);
		}
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.TEST_CASES)) {
			return createImage(TYPE_COLLECTION_TEST_ICON_URL);
		}
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.WEB_CONTENT)) {
			return createImage(TYPE_COLLECTION_WEB_ICON_URL);
		}
		if (name.equals(ICommonConstants.ARTIFACT_TYPE.WIKI_CONTENT)) {
			return createImage(TYPE_COLLECTION_WIKI_ICON_URL);
		}
		return createImage(TYPE_COLLECTION_ICON_URL);
	}

	protected Image getRepositoryRootImage(ICollection collection) {
		return createImage(TYPE_REPOSITORY_ROOT_ICON_URL);
	}

}
