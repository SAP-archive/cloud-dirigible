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

package com.sap.dirigible.ide.publish.ui.command;

import org.eclipse.core.resources.IProject;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.publish.PublishManager;

/**
 * Handler for the Publish command.
 * 
 */
public class ActivateCommandHandler extends PublishCommandHandler {

	@Override
	protected void publishProject(IProject project) throws PublishException {
		PublishManager.activateProject(project);
	}
	
	@Override
	protected String getStatusMessage() {
		return StatusLineManagerUtil.ARTIFACT_HAS_BEEN_ACTIVATED;
	}
	
}
