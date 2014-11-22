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

package com.sap.dirigible.ide.workspace.impl.event;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;

public class ResourceChangeEvent implements IResourceChangeEvent {

	private final IResource resource;

	private final int type;

	public ResourceChangeEvent(IResource resource, int type) {
		this.resource = resource;
		this.type = type;
	}

	public int getBuildKind() {
		return 0;
		// throw new UnsupportedOperationException();
	}

	public IResourceDelta getDelta() {
		return null;
		// throw new UnsupportedOperationException();
	}

	public IResource getResource() {
		return resource;
	}

	public Object getSource() {
		throw new UnsupportedOperationException();
	}

	public int getType() {
		return type;
	}

	public IMarkerDelta[] findMarkerDeltas(String type, boolean includeSubtypes) {
		throw new UnsupportedOperationException();
	}

}
