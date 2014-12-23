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

package com.sap.dirigible.runtime.flow;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.registry.AbstractRegistryServiceServlet;

public class FlowRegistryServlet extends AbstractRegistryServiceServlet {

    private static final long serialVersionUID = -8255379751142002763L;
    
    public static final String INTEGRATION_FOLDER = ICommonConstants.ARTIFACT_TYPE.INTEGRATION_SERVICES + IRepository.SEPARATOR;

    @Override
    protected String getServletMapping() {
        return "/flow/";
    }

    @Override
    protected String getFileExtension() {
        return ".flow";
    }

    @Override
    protected String getRequestProcessingFailedMessage() {
        return "Flow execution failed.";
    }
    
    protected String getServicesFolder() {
		return INTEGRATION_FOLDER;
	}

}
