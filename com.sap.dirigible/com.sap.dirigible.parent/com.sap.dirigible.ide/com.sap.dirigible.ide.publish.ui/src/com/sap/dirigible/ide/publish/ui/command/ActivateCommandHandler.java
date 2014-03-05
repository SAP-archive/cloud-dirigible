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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.sap.dirigible.ide.common.status.StatusLineManagerUtil;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.publish.PublishManager;
import com.sap.dirigible.ide.publish.IPublisher;

/**
 * Handler for the Publish command.
 * 
 */
public class ActivateCommandHandler extends PublishCommandHandler {

	protected void publishProject(IProject project) throws PublishException {
		final List<IPublisher> publishers = PublishManager.getPublishers();

		for (Iterator<IPublisher> iterator = publishers.iterator(); iterator
				.hasNext();) {
			IPublisher publisher = (IPublisher) iterator.next();
			publisher.activate(project);
		}
	}
	
	@Override
	protected String getStatusMessage() {
		return StatusLineManagerUtil.ARTIFACT_HAS_BEEN_ACTIVATED;
	}
	
}
