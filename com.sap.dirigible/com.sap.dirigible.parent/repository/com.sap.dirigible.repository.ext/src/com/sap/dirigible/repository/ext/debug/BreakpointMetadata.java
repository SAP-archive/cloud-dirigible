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

package com.sap.dirigible.repository.ext.debug;

public class BreakpointMetadata extends DebugSessionMetadata implements Comparable<BreakpointMetadata> {
	
	private static final String SLASH = "/";
	private static final String ROW_D_PATH_S = "[row: %d | full path: %s]";
	private String fullPath;
	private Integer row;
	
	public BreakpointMetadata(String sessionId, String executionId, String userId) {
		super(sessionId, executionId, userId);
	}

	public BreakpointMetadata(String sessionId, String executionId, String userId, String fullPath, Integer row) {
		super(sessionId, executionId, userId);
		this.fullPath = fullPath;
		this.row = row;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public String getFileName() {
		return fullPath.substring(fullPath.lastIndexOf(SLASH) + 1);
	}

	@Override
	public int compareTo(BreakpointMetadata that) {
		int result = 0;
		if (that != null) {
			result = this.getFullPath().compareTo(that.getFullPath());
			if (result == 0) {
				result = this.getRow().compareTo(that.getRow());
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullPath == null) ? 0 : fullPath.hashCode());
		result = prime * result + ((row == null) ? 0 : row.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BreakpointMetadata other = (BreakpointMetadata) obj;
		if (fullPath == null) {
			if (other.fullPath != null)
				return false;
		} else if (!fullPath.equals(other.fullPath))
			return false;
		if (row == null) {
			if (other.row != null)
				return false;
		} else if (!row.equals(other.row))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String message = String.format(ROW_D_PATH_S, row, fullPath);
		return message;
	}

}
