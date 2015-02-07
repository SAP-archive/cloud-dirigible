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

package com.sap.dirigible.runtime.wiki;

import java.io.IOException;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.web.WebExecutor;

public class WikiExecutor extends WebExecutor {
	
	private static final Logger logger = Logger.getLogger(WikiExecutor.class);


	public WikiExecutor(IRepository repository, String... rootPaths) {
		super(repository, rootPaths);
	}

	@Override
	protected byte[] preprocessContent(byte[] rawContent, IEntity entity)
			throws IOException {
		return WikiProcessor.processContent(rawContent, entity);
	}

	@Override
	protected String getModuleType(String path) {
		return ICommonConstants.ARTIFACT_TYPE.WIKI_CONTENT;
	}
}
