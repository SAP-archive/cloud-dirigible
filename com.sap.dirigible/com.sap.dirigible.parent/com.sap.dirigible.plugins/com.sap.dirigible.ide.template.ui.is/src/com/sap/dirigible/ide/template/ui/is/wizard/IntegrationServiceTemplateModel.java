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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.sap.dirigible.ide.template.ui.common.GenerationModel;
import com.sap.dirigible.ide.ui.common.validation.IValidationStatus;
import com.sap.dirigible.ide.ui.common.validation.ValidationStatus;
import com.sap.dirigible.repository.api.ICommonConstants;

public class IntegrationServiceTemplateModel extends GenerationModel {

	private static final String TARGET_LOCATION_IS_NOT_ALLOWED = Messages.IntegrationServiceTemplateModel_TARGET_LOCATION_IS_NOT_ALLOWED;
	private String routeId;
	private String endpointAddress;
	private String parameterName;
	private String originalEndpoint;

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getEndpointAddress() {
		return endpointAddress;
	}

	public void setEndpointName(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getOriginalEndpoint() {
		return originalEndpoint;
	}

	public void setOriginalEndpoint(String originalEndpoint) {
		this.originalEndpoint = originalEndpoint;
	}

	@Override
	public IValidationStatus validate() {
		IValidationStatus locationStatus = validateLocation();
		if (locationStatus.hasErrors()) {
			return locationStatus;
		}
		IValidationStatus templateStatus = validateTemplate();
		if (locationStatus.hasErrors()) {
			return locationStatus;
		}

		return ValidationStatus.getValidationStatus(locationStatus,
				templateStatus);
	}

	public IValidationStatus validateLocation() {
		IValidationStatus status;
		try {
			status = validateLocationGeneric();
			if (status.hasErrors()) {
				return status;
			}
			IPath location = new Path(getTargetLocation())
					.append(getFileName());
			// TODO - more precise test for the location ../WebContent/...
			if (location.toString().indexOf(
					ICommonConstants.ARTIFACT_TYPE.INTEGRATION_SERVICES) == -1) {
				return ValidationStatus
						.createError(TARGET_LOCATION_IS_NOT_ALLOWED);
			}
		} catch (Exception e) {
			// temp workaround due to another bug - context menu is not context
			// aware => target location and name are null (in the first page of
			// the wizard)
			return ValidationStatus.createError(""); //$NON-NLS-1$
		}
		return status;
	}

}
