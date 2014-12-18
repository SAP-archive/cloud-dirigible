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

package com.sap.dirigible.ide.repository.ui.adapters;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.sap.dirigible.ide.repository.ui.viewer.ArtifactPropertySource;
import com.sap.dirigible.ide.repository.ui.viewer.RepositoryViewer;
import com.sap.dirigible.repository.api.IEntity;

/**
 * This factory allows for each selected entity in the {@link RepositoryViewer}
 * viewer, corresponding properties to be displayed in the Properties view.
 * 
 */
public class RepositoryPropertiesAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IEntity) {
			final IEntity entity = (IEntity) adaptableObject;
			if (adapterType.equals(IPropertySource.class)) {
				return new ArtifactPropertySource(entity);
			}
		}
		return null;
	}

	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IPropertySource.class };
	}

}
