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

package com.sap.dirigible.ide.publish;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public interface IPublisher {

	public void publish(IProject project) throws PublishException;
	
	public void activate(IProject project) throws PublishException;
	
	public void activateFile(IFile file) throws PublishException;

	// returns the name of the folder recognizable by the specific publisher
	public String getFolderType();

	public boolean recognizedFile(IFile file);

	public String getPublishedLocation(IFile file);

	public String getPublishedEndpoint(IFile file);
	
	public String getPublishedContainerMapping(IFile file);
	
	public String getActivatedLocation(IFile file);

	public String getActivatedEndpoint(IFile file);

	public String getActivatedContainerMapping(IFile file);

	public boolean isAutoActivationAllowed();

	public String getDebugEndpoint(IFile file);

}
