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

package com.sap.dirigible.ide.workspace.ui.shared;

public class ValidationStatus implements IValidationStatus {

	private static enum Type {
		OK, ERROR, WARNING
	}

	private final Type type;

	private final String message;

	private ValidationStatus(Type type, String message) {
		this.type = type;
		this.message = message;
	}

	public boolean isOK() {
		return type == Type.OK;
	}

	public boolean hasErrors() {
		return (type == Type.ERROR);
	}

	public boolean hasWarnings() {
		return (type == Type.WARNING);
	}

	public String getMessage() {
		return message;
	}

	public static ValidationStatus createOk() {
		return new ValidationStatus(Type.OK, null);
	}

	public static ValidationStatus createWarning(String message) {
		return new ValidationStatus(Type.WARNING, message);
	}

	public static ValidationStatus createError(String message) {
		return new ValidationStatus(Type.ERROR, message);
	}

}
