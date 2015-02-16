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

package com.sap.dirigible.ide.scripts.publish;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.publish.AbstractPublisher;
import com.sap.dirigible.ide.publish.PublishException;
import com.sap.dirigible.ide.publish.IPublisher;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IResource;

public class TestCasesPublisher extends AbstractPublisher implements IPublisher {

	public TestCasesPublisher() {
		super();
	}

	@Override
	public void publish(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(
					project, getRegistryLocation());
			final IFolder sourceFolder = getSourceFolder(project,
					ICommonConstants.ARTIFACT_TYPE.TEST_CASES);
			copyAllFromTo(sourceFolder, targetContainer);
		} catch (Exception ex) {
			throw new PublishException(ex.getMessage(), ex);
		}
	}
	
	@Override
	public void activate(IProject project) throws PublishException {
		try {
			final ICollection targetContainer = getTargetProjectContainer(
					project, CommonParameters.getTestingContentSandbox());
			final IFolder sourceFolder = getSourceFolder(project,
					ICommonConstants.ARTIFACT_TYPE.TEST_CASES);
			copyAllFromTo(sourceFolder, targetContainer);
		} catch (Exception ex) {
			throw new PublishException(ex.getMessage(), ex);
		}
	}
	
	@Override
	protected String getSandboxLocation() {
		return CommonParameters.getTestingContentSandbox();
	}
	
	@Override
	public String getFolderType() {
		return ICommonConstants.ARTIFACT_TYPE.TEST_CASES;
	}

	@Override
	public boolean recognizedFile(IFile file) {
		if (checkFolderType(file)) {
			if (CommonParameters.JAVASCRIPT_SERVICE_EXTENSION.equals("." //$NON-NLS-1$
					+ file.getFileExtension())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getPublishedContainerMapping(IFile file) {
		return CommonParameters.TEST_CASES_CONTAINER_MAPPING;
	}
	
	@Override
	public String getActivatedContainerMapping(IFile file) {
		return CommonParameters.TEST_CASES_SANDBOX_MAPPING;
	}
	
	@Override
	public boolean isAutoActivationAllowed() {
		return true;
	}

	@Override
	protected String getRegistryLocation() {
		return ICommonConstants.TESTS_REGISTRY_PUBLISH_LOCATION;
	}

}
