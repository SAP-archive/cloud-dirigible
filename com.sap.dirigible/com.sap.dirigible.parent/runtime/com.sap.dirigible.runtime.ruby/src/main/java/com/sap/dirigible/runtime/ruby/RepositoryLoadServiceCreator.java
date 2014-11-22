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

package com.sap.dirigible.runtime.ruby;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig.LoadServiceCreator;
import org.jruby.runtime.load.LoadService;

import com.sap.dirigible.repository.api.IRepository;

public class RepositoryLoadServiceCreator implements LoadServiceCreator {

	private IRepository repository;
	private String root;
	private String secondaryRoot;

	RepositoryLoadServiceCreator(IRepository repository, String root, String secondaryRoot) {
		this.repository = repository;
		this.root = root;
		this.secondaryRoot = secondaryRoot;
	}

	@Override
	public LoadService create(Ruby runtime) {
		return new RepositoryLoadService(runtime, this);
	}

	public IRepository getRepository() {
		return repository;
	}

	public String getRoot() {
		return root;
	}
	
	public String getSecondaryRoot() {
		return secondaryRoot;
	}
}
