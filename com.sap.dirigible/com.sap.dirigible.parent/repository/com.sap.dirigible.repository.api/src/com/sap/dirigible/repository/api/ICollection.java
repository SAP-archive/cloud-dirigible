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

package com.sap.dirigible.repository.api;

import java.io.IOException;
import java.util.List;

/**
 * The <code>ICollection</code> interface represents a collection in the
 * repository.
 * 
 */
public interface ICollection extends IEntity {

	/**
	 * Returns a list of all the child collections held by this collection.
	 */
	public List<ICollection> getCollections() throws IOException;

	/**
	 * Returns a list containing the names of all the child collections directly
	 * contained within this collection.
	 */
	public List<String> getCollectionsNames() throws IOException;

	/**
	 * Creates a new collection with the specified name in this collection.
	 * <p>
	 * The name should not contain any slashes.
	 * <p>
	 * The change is persisted to the backend.
	 */
	public ICollection createCollection(String name) throws IOException;

	/**
	 * Returns the collection with the specified name contained in this
	 * collection.
	 * <p>
	 * The returned collection is just a representation. It may not exist on the
	 * backend.
	 * 
	 * @return an {@link ICollection} instance.
	 */
	public ICollection getCollection(String name);

	/**
	 * Removes the collection with the specified name contained in this
	 * collection.
	 * <p>
	 * The name should not contain any slashes.
	 * <p>
	 * The change is persisted to the backend.
	 */
	public void removeCollection(String name) throws IOException;

	/**
	 * Removes the child collection represented by the parameter.
	 */
	public void removeCollection(ICollection collection) throws IOException;

	/**
	 * Returns a list of all the resources held by this collection.
	 */
	public List<IResource> getResources() throws IOException;

	/**
	 * Returns a list containing the names of all the resources directly
	 * contained in this collection.
	 */
	public List<String> getResourcesNames() throws IOException;

	/**
	 * Returns the resource with the specified name contained in this
	 * collection.
	 * <p>
	 * The returned resource is just a representation. It may not exist on the
	 * backend.
	 */
	public IResource getResource(String name) throws IOException;

//	/**
//	 * Creates a new empty resource with the specified name in this collection.
//	 * <p>
//	 * Changes are persisted to the backend.
//	 */
//	public IResource createResource(String name) throws IOException;
//
//	/**
//	 * Creates a new resource in this collection with the specified name and
//	 * content.
//	 * <p>
//	 * Changes are persisted to the backend.
//	 */
//	public IResource createResource(String name, byte[] content)
//			throws IOException;

	/**
	 * Removes the resource with the specified name from this collection.
	 * <p>
	 * Changes are persisted to the backend.
	 */
	public void removeResource(String name) throws IOException;

	/**
	 * Removes the child resource represented by the parameter.
	 * <p>
	 * Changes are persisted to the backend.
	 */
	public void removeResource(IResource resource) throws IOException;

	/**
	 * List the children of this collection
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<IEntity> getChildren() throws IOException;

	/**
	 * Create resource under this collection by specifying the binary flag
	 * 
	 * @param name
	 * @param content
	 * @param isBinary
	 * @param contentType
	 * @return
	 * @throws IOException
	 */
	public IResource createResource(String name, byte[] content,
			boolean isBinary, String contentType) throws IOException;

}
