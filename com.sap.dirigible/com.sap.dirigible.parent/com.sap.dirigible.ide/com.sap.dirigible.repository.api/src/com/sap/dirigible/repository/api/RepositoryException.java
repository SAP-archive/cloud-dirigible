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

/**
 * The {@link RepositoryException} is thrown in situations when a user is trying
 * to use any of the repository API in a way that is invalid or not expected.<br>
 * One such example is when passing <code>null</code> to a method when it does
 * not expect it. This would likely result in a {@link IllegalArgumentException}
 * or a {@link NullPointerException} depending on the implementation's code
 * conventions. The first exception type is preferred though.
 * 
 */
public class RepositoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RepositoryException() {
		super();
	}

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(Throwable ex) {
		super(ex);
	}

	public RepositoryException(String message, Throwable ex) {
		super(message, ex);
	}

}
