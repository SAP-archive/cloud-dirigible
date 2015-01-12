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

package com.sap.dirigible.ide.template.ui.is.wizard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.template.ui.common.TemplateGenerator;
import com.sap.dirigible.repository.api.ICommonConstants;

public class IntegrationServiceTemplateGenerator extends TemplateGenerator {

	private static final String LOG_TAG = "INTEGRATION_SERVICE_GENERATOR"; //$NON-NLS-1$

	private IntegrationServiceTemplateModel model;

	public IntegrationServiceTemplateGenerator(
			IntegrationServiceTemplateModel model) {
		this.model = model;
	}

	@Override
	protected Map<String, Object> prepareParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", model.getId()); //$NON-NLS-1$
		parameters.put("endpointAddress", model.getEndpointAddress()); //$NON-NLS-1$
//		parameters.put("parameterName", model.getParameterName()); //$NON-NLS-1$
		parameters.put("projectName", model.getProjectName()); //$NON-NLS-1$
		String fileNameNoExtension = model.getFileNameNoExtension();
		parameters.put("fileNameNoExtension", fileNameNoExtension); //$NON-NLS-1$
		String fileNameNoExtensionTitle = fileNameNoExtension;
		if (fileNameNoExtension != null && fileNameNoExtension.length() > 1) {
			char[] chars = fileNameNoExtension.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			fileNameNoExtensionTitle = new String(chars);
		}
		parameters.put("fileNameNoExtensionTitle", fileNameNoExtensionTitle); //$NON-NLS-1$
//		parameters.put("originalEndpoint", model.getOriginalEndpoint()); //$NON-NLS-1$
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

	@Override
	protected byte[] afterGeneration(byte[] bytes) {
		String content = new String(bytes);
		content = content.replace("\\$", "$"); //$NON-NLS-1$ //$NON-NLS-2$
		content = content.replace("\\{", "{"); //$NON-NLS-1$ //$NON-NLS-2$
		content = content.replace("\\}", "}"); //$NON-NLS-1$ //$NON-NLS-2$
		content = content.replace("\\.", "."); //$NON-NLS-1$ //$NON-NLS-2$
		byte[] result = content.getBytes();
		return result;
	}

	@Override
	public void generate() throws Exception {
		super.generate();
		IPath targetLocationPath = new Path(getModel().getTargetLocation());
		String projectName = getModel().getProjectName();
		while ((!projectName.equals(targetLocationPath.lastSegment()))
				|| targetLocationPath.segmentCount() == 0) {
			targetLocationPath = targetLocationPath.removeLastSegments(1);
		}
		String targetLocationPathJavaScriptServices = targetLocationPath
				.append("/" + ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES) //$NON-NLS-1$
				.toString();

		if ("/com/sap/dirigible/ide/template/ui/is/templates/shielding-js.flow" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {

			generateFile(
					"/com/sap/dirigible/ide/template/ui/is/templates/javascript-sync.js", //$NON-NLS-1$
					targetLocationPathJavaScriptServices,
					model.getEndpointAddress()
							+ CommonParameters.JAVASCRIPT_SERVICE_EXTENSION);
		}
		
		if ("/com/sap/dirigible/ide/template/ui/is/templates/trigger-to-javascript.job" //$NON-NLS-1$
				.equals(model.getTemplate().getLocation())) {

			generateFile(
					"/com/sap/dirigible/ide/template/ui/is/templates/javascript-async.js", //$NON-NLS-1$
					targetLocationPathJavaScriptServices,
					model.getEndpointAddress()
							+ CommonParameters.JAVASCRIPT_SERVICE_EXTENSION);
		}

	}

}
