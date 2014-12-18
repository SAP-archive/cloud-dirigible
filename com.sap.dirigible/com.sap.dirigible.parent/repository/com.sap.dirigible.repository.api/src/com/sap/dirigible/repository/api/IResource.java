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
 * The <code>IResource</code> interface represents a resource located in the
 * repository.
 * 
 */
public interface IResource extends IEntity {

	public static final String CONTENT_TYPE_DEFAULT = "text/plain"; //$NON-NLS-1$

	/**
	 * Returns the content of the resource as a byte array.
	 */
	public byte[] getContent() throws IOException;

	/**
	 * Sets this resource's content.
	 * 
	 * @param content
	 */
	public void setContent(byte[] content) throws IOException;

	/**
	 * Sets this resource's content.
	 * 
	 * @param content
	 * @param isBinary
	 * @param contentType
	 * @throws IOException
	 */
	public void setContent(byte[] content, boolean isBinary, String contentType)
			throws IOException;

	/**
	 * Getter for binary flag
	 * 
	 * @return
	 */
	public boolean isBinary();

	/**
	 * Getter for the content type
	 * 
	 * @return
	 */
	public String getContentType();

	/**
	 * Retrieve all the kept versions of a given resource
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<IResourceVersion> getResourceVersions() throws IOException;

	/**
	 * Retrieve a particular version of a given resource
	 * 
	 * @param version
	 * @return
	 * @throws IOException
	 */
	public IResourceVersion getResourceVersion(int version) throws IOException;

}
