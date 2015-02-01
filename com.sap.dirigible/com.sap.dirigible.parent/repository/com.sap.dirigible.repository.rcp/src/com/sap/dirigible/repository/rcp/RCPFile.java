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

import java.util.Arrays;

/**
 * Internal representation of a File/Resource kind of object
 * 
 */
public class RCPFile extends RCPObject {

	private boolean binary = false;

	private String contentType;

	public RCPFile(RCPRepository repository, boolean isBinary, String contentType) {
		super(repository);
		this.binary = isBinary;
		this.contentType = contentType;
	}

	public void delete() throws RCPBaseException {
		getRepository().getRepositoryDAO().removeFileByPath(getPath());
	}

	 public void rename(String newPath) throws RCPBaseException {
		 getRepository().getRepositoryDAO().renameFile(getPath(), newPath);
	 }

	public byte[] getData() throws RCPBaseException {
		return getRepository().getRepositoryDAO().getFileContent(this);
	}

	public void setData(byte[] content) throws RCPBaseException {
		byte[] old = getData();
		if (old != null) {
			if (Arrays.equals(old, content)) {
				return;
			}
		}
		getRepository().getRepositoryDAO().setFileContent(this, content);
	}

	public boolean isBinary() {
		return binary;
	}

	public String getContentType() {
		return contentType;
	}
}
