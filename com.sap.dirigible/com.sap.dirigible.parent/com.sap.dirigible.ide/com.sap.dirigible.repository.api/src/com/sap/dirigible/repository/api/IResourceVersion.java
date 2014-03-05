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
import java.util.Date;

public interface IResourceVersion {

	/**
	 * Returns the path of this resource version
	 * <p>
	 * The result may not be <code>null</code>.
	 * <p>
	 * Example: /db/users/test.txt <br>
	 * Example: /db/articles
	 */
	public String getPath();

	/**
	 * Returns the version number
	 * 
	 * @return
	 */
	public int getVersion();

	/**
	 * Returns the content of the resource version as a byte array.
	 */
	public byte[] getContent() throws IOException;

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
	 * The creator of the entity
	 * 
	 * @return
	 */
	public String getCreatedBy();

	/**
	 * Timestamp of the creation of the entity
	 * 
	 * @return
	 */
	public Date getCreatedAt();

}
