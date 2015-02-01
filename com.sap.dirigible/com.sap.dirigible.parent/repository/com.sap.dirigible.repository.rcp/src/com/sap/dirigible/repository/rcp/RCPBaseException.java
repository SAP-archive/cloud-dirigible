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

package com.sap.dirigible.repository.rcp;

import com.sap.dirigible.repository.api.RepositoryException;

/**
 * Main exception for the DB repository implementation
 * 
 */
public class RCPBaseException extends RepositoryException {

	private static final long serialVersionUID = 116149128529374300L;

	public RCPBaseException() {
		super();
	}

	public RCPBaseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RCPBaseException(String arg0) {
		super(arg0);
	}

	public RCPBaseException(Throwable arg0) {
		super(arg0);
	}

}
