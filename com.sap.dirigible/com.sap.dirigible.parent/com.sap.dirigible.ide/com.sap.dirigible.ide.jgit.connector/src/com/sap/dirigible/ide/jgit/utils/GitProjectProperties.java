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

package com.sap.dirigible.ide.jgit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sap.dirigible.ide.common.CommonParameters;

public class GitProjectProperties {
	private final Properties gitProperties;
	public static final String PROJECT_GIT_PROPERTY = "git.property";
	public static final String PROPERTY_LAST_COMMIT_SHA = "last.commit.sha";
	public static final String PROPERTY_GIT_REPOSITORY_URL = "git.repository.url";
	public static final String DB_DIRIGIBLE_USERS_S_GIT_S_REPOSITORY = CommonParameters.DB_DIRIGIBLE_ROOT + "users/%s/git/%s";
	public static final String DB_DIRIGIBLE_USERS_S_WORKSPACE = CommonParameters.DB_DIRIGIBLE_ROOT + "users/%s/workspace/";
	public static final String GIT_PROPERTY_FILE_LOCATION = DB_DIRIGIBLE_USERS_S_GIT_S_REPOSITORY
			+ "/" + PROJECT_GIT_PROPERTY;

	public GitProjectProperties(String URL, String SHA) {
		gitProperties = new Properties();
		gitProperties.setProperty(GitProjectProperties.PROPERTY_GIT_REPOSITORY_URL, URL);
		gitProperties.setProperty(GitProjectProperties.PROPERTY_LAST_COMMIT_SHA, SHA);
	}

	public GitProjectProperties(InputStream in) throws IOException {
		gitProperties = new Properties();
		load(in);
	}

	public void load(InputStream in) throws IOException {
		gitProperties.load(in);
	}

	public String getSHA() {
		return gitProperties.getProperty(GitProjectProperties.PROPERTY_LAST_COMMIT_SHA);
	}

	public String getURL() {
		return gitProperties.getProperty(GitProjectProperties.PROPERTY_GIT_REPOSITORY_URL);
	}

	public GitProjectProperties setSHA(String SHA) {
		gitProperties.setProperty(GitProjectProperties.PROPERTY_LAST_COMMIT_SHA, SHA);
		return this;
	}

	public GitProjectProperties setURL(String URL) {
		gitProperties.setProperty(GitProjectProperties.PROPERTY_GIT_REPOSITORY_URL, URL);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(GitProjectProperties.PROPERTY_GIT_REPOSITORY_URL + "=" + getURL() + "\n");
		result.append(GitProjectProperties.PROPERTY_LAST_COMMIT_SHA + "=" + getSHA());
		return result.toString();
	}

	public byte[] getContent() {
		return toString().getBytes();
	}
}
