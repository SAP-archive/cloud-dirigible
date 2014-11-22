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

/**
 * The {@link IEntity} interface is a base interface for {@link ICollection} and
 * {@link IResource}. <br>
 * It provides methods that are common for both interfaces.
 * 
 */
public interface IEntity {

	/**
	 * Returns the repository that holds this resource.
	 */
	public IRepository getRepository();

	/**
	 * Returns the name of this entity.
	 * <p>
	 * This is equal to the content of the path after the last slash in it.
	 */
	public String getName();

	/**
	 * Returns the path of this entity.
	 * <p>
	 * The result may not be <code>null</code>.
	 * <p>
	 * Example: /db/users/test.txt <br>
	 * Example: /db/articles
	 */
	public String getPath();

	/**
	 * Returns the collection that holds this entity.
	 * <p>
	 * The result could be <code>null</code> should there be no parent (i.e.
	 * this is the root).
	 */
	public ICollection getParent();

	/**
	 * Returns an instance of <code>IEntityInformation</code> through which one
	 * can obtain information on the current entity.
	 * <p>
	 * This method may not return <code>null</code>, however, the contents of
	 * the returned {@link IEntityInformation} may return <code>null</code>
	 * indicating that a given information is not available.
	 * 
	 * @throws IOException
	 *             if for some reason a connection to the backend could not be
	 *             achieved.
	 */
	public IEntityInformation getInformation() throws IOException;

	/**
	 * Forces this entity to be created at its current path.
	 * <p>
	 * Whether the entity will be created as a resource or a collection depends
	 * on whether it is an instance of {@link IResource} or {@link ICollection}.
	 * 
	 * @throws IOException
	 *             if for some reason a connection to the backend could not be
	 *             achieved, or if an entity with this path already exists on
	 *             the backend.
	 */
	public void create() throws IOException;

	/**
	 * Removes this entity from the repository.
	 * <p>
	 * If no such resource exists, this method does nothing.
	 * 
	 * @throws IOException
	 *             if for some reason a connection to the backend could not be
	 *             achieved.
	 */
	public void delete() throws IOException;

	/**
	 * Changes the name of this entity to the specified value.
	 */
	public void renameTo(String name) throws IOException;

	/**
	 * Moves this entity to a collection at the specified <code>path</code>.
	 * <p>
	 * If this entity is of type {@link ICollection}, then all child entities
	 * are copied too.
	 */
	public void moveTo(String path) throws IOException;

	/**
	 * Copies this entity to a collection at the specified <code>path</code>.
	 * <p>
	 * If this entity is of type {@link ICollection}, then all child entities
	 * are copied too.
	 */
	public void copyTo(String path) throws IOException;

	/**
	 * Returns whether this entity is valid and exists on the backend.
	 * 
	 * @throws IOException
	 *             if for some reason a connection to the backend could not be
	 *             achieved.
	 */
	public boolean exists() throws IOException;

	/**
	 * Returns whether this entity is empty.
	 * <p>
	 * If the entity is a collection, implementations should check to see if it
	 * has any child entities.
	 * <p>
	 * If the entity is a resource, implementations should check to see if it
	 * has any content. <br>
	 * <i><strong>Note:</strong> Calling this method on a resource can be
	 * slow.</i>
	 * 
	 * @throws IOException
	 *             if for some reason a connection to the backend could not be
	 *             achieved.
	 */
	public boolean isEmpty() throws IOException;
}
