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

package com.sap.dirigible.ide.template.ui.html.wizard;

import java.util.HashMap;
import java.util.Map;

import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;

public class WikiTemplateGenerator extends TemplateGenerator {

	private static final String LOG_TAG = "WIKI_GENERATOR"; //$NON-NLS-1$

	private WikiTemplateModel model;

	public WikiTemplateGenerator(WikiTemplateModel model) {
		this.model = model;
	}

	@Override
	protected Map<String, Object> prepareParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pageTitle", model.getPageTitle()); //$NON-NLS-1$
		return parameters;
	}

	@Override
	protected GenerationModel getModel() {
		return model;
	}

	@Override
	protected String getLogTag() {
		return LOG_TAG;
	}

}
