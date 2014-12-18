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

public class PublishException extends Exception {

	private static final long serialVersionUID = 1L;

	public PublishException() {
		super();
	}

	public PublishException(String message) {
		super(message);
	}

	public PublishException(Throwable ex) {
		super(ex);
	}

	public PublishException(String message, Throwable ex) {
		super(message, ex);
	}

}
